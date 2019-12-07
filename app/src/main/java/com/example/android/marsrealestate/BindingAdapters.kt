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

package com.example.android.marsrealestate

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

// Binding adapters are extension methods that sit between a view and bound data to provide custom behavior when the data changes

// this annotation tells data binding that we want this binding adapter executed when an xml item has the defined "imageUrl" attribute
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        // a lot is going on here. Basically, the conversion from URL string (from XML) to a URI object happens here
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()

        // now we can use Glide to show the image by passing our URI object & view
        Glide.with(imgView.context)
                .load(imgUri)
                .into(imgView)
    }
}