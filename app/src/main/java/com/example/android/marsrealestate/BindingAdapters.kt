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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.marsrealestate.network.MarsProperty
import com.example.android.marsrealestate.overview.PhotoGridAdapter

// The @BindingAdapter annotation tells data binding that you want this binding adapter executed
// when an XML item has the imageUrl attribute
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        // convert the URL string (from the XML) to a Uri object
        // use the HTTPS scheme, because the server you pull the images from requires that secure scheme
        val imgUri = it.toUri()
                .buildUpon()
                .scheme("https")
                .build()

        val options = RequestOptions().placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
        Glide.with(imgView.context)
                .load(imgUri)
                .apply(options)
                .into(imgView)
    }
}

// initialize the PhotoGridAdapter with the list of MarsProperty objects. Using a BindingAdapter to
// set the RecyclerView data causes data binding to automatically observe the LiveData for the list
// of MarsProperty objects. Then the binding adapter is called automatically when the MarsProperty
// list changes.
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<MarsProperty>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    // tell the RecyclerView when a new list is available
    adapter.submitList(data)
}