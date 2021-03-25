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
import androidx.lifecycle.viewModelScope
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.launch

enum class MarsApiStatus { LOADING, ERROR, DONE }

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    private val _status = MutableLiveData<MarsApiStatus>()
    val status: LiveData<MarsApiStatus>
        get() = _status

    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    // When this LiveData changes to non-null (When the user taps a photo in the overview model),
    // it should trigger navigation to a fragment that shows details about the clicked item
    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()
    val navigateToSelectedProperty: LiveData<MarsProperty>
        get() = _navigateToSelectedProperty

    // Call getMarsRealEstateProperties() on init so we can display status immediately.
    init {
        // show all properties when the app first loads
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    fun updateFilter(filter: MarsApiFilter) {
        getMarsRealEstateProperties(filter)
    }

    fun displayPropertyDetails(marsProperty: MarsProperty) {
        _navigateToSelectedProperty.value = marsProperty
    }

    // need this to mark the navigation state to complete, and to avoid the navigation being
    // triggered again when the user returns from the detail view
    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    // Sets the value of the status LiveData to the Mars API status.
    private fun getMarsRealEstateProperties(filter: MarsApiFilter) {
        // A ViewModelScope is the built-in coroutine scope defined for each ViewModel in your app.
        // Any coroutine launched in this scope is automatically canceled if the ViewModel is cleared
        viewModelScope.launch {
            // the initial status while the coroutine is running and you're waiting for data
            _status.value = MarsApiStatus.LOADING
            try {
                // this creates and starts the network call on a background thread.
                _properties.value = MarsApi.retrofitService.getProperties(filter.value)
                _status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                // set the _properties LiveData to an empty list, to clear the RecyclerView.
                _properties.value = ArrayList()
            }
        }
    }
}
