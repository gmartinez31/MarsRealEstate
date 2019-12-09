/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

enum class MarsApiStatus { LOADING, ERROR, DONE }

/**The [ViewModel] that is attached to the [OverviewFragment]. */
class OverviewViewModel : ViewModel() {

    private val _status = MutableLiveData<MarsApiStatus>()
    val status: LiveData<MarsApiStatus>
        get() = _status

    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()
    val navigateToSelectedProperty: LiveData<MarsProperty>
        get() = _navigateToSelectedProperty

    private var vmJob = Job()
    private val coroutineScope = CoroutineScope(vmJob + Dispatchers.Main )


    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties(filter: MarsApiFilter) {
// we don't really need to do this manually anymore since we use coroutines to handle all of this
//        MarsApi.retrofitService.getProperties().enqueue(
//                object: Callback<List<MarsProperty>> {
//                    override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable) {
//                        _response.value = "Failure: " + t.message
//                    }
//
//                    override fun onResponse(call: Call<List<MarsProperty>>, response: Response<List<MarsProperty>>) {
//                        _response.value = "Success: ${response.body()?.size} Mars properties retrieved!"
//                    }
//                })
        coroutineScope.launch {
            /*
             * Calling await() on the Deferred object returns the result from the network call
             * when the value is ready. The await() method is non-blocking, so the Mars API
             * service retrieves the data from the network without blocking the current
             * thread—which is important because we're in the scope of the UI thread.
             */

            val getMarsPropsDeferred = MarsApi.retrofitService.getProperties(filter.value)
            try {
                _status.value = MarsApiStatus.LOADING
                val listResult = getMarsPropsDeferred.await()
                _status.value = MarsApiStatus.DONE
                _properties.value = listResult
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }

    }

    /*
     * Loading data should stop when the VM is destroyed, because the Fragment that uses this VM
     * will be gone. To stop loading when the VM is destroyed, we override onCleared() to cancel the job.
     */
    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }

    fun updateFilter(filter: MarsApiFilter) {
        getMarsRealEstateProperties(filter)
    }

    fun displayPropertyDetails(marsProperty: MarsProperty) {
        _navigateToSelectedProperty.value = marsProperty
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }
}
