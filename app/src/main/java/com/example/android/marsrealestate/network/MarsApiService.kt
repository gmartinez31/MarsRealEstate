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


// this file holds the network layer for the app; that is, this is the API the VM will use to communicate with the web service.

package com.example.android.marsrealestate.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = " https://android-kotlin-fun-mars-server.appspot.com/"


/*
 * Retrofit needs at least 2 things available to it to build a web services API:
 * (1) base URI and (2) a converter factory.
 * The converter tells Retrofit what do with the data it gets back from the web service.
 * In this case, we fetch a JSON response from the web service and return it as a String.
 * Retrofit has a ScalarsConverter that supports strings and other primitive types, so we call
 * addConverterFactory() on the builder with an instance of ScalarsConverterFactory.
 */
private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

/*
 * When this method is invoked, Retrofit appends the endpoint 'realestate' to the BASE_URL,
 * and creates a Call object. That Call object is used to start the HTTP request.
 */
interface MarsApiService {
    @GET("realestate") // <-- path/endpoint
    fun getProperties(): Call<String>
}


/*
 * The Retrofit create() method creates the Retrofit service itself with the MarsApiService interface.
 * Since this call is expensive, and the app only needs one Retrofit service instance, we expose the
 * service to the rest of the app using a public object called MarsApi and lazily initialize the Retrofit service there.
 * Each time the app calls MarsApi.retrofitService, it will get a singleton Retrofit object that implements MarsApiService.
 */
object MarsApi {
    val retrofitService : MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}