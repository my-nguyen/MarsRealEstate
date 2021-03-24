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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // the most recent response
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    // Call getMarsRealEstateProperties() on init so we can display status immediately.
    init {
        getMarsRealEstateProperties()
    }

    // Sets the value of the status LiveData to the Mars API status.
    private fun getMarsRealEstateProperties() {
        // call the Retrofit service and handle the returned JSON string
        MarsApi.retrofitService.getProperties().enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                // set the _response to the response body
                _response.value = response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // set the _response to a failure message
                _response.value = "Failure: " + t.message
            }
        })
    }
}