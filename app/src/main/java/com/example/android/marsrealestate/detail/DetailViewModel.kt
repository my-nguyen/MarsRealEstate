/*
 *  Copyright 2019, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.marsrealestate.detail

import android.app.Application
import androidx.lifecycle.*
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.network.MarsProperty

/**
 * The [ViewModel] that is associated with the [DetailFragment].
 */
class DetailViewModel(marsProperty: MarsProperty, app: Application) : AndroidViewModel(app) {
    // LiveData for the selected Mars property, to expose that information to the detail view
    private val _selectedProperty = MutableLiveData<MarsProperty>()
    val selectedProperty: LiveData<MarsProperty>
        get() = _selectedProperty

    val displayPropertyPrice = Transformations.map(selectedProperty) {
        val resId = when (it.isRental) {
            // If the property is a rental, the transformation chooses the appropriate string from the resources
            true -> R.string.display_price_monthly_rental
            false -> R.string.display_price
        }
        app.applicationContext.getString(resId, it.price)
    }

    val displayPropertyType = Transformations.map(selectedProperty) {
        val resId = when (it.isRental) {
            true -> R.string.type_rent
            false -> R.string.type_sale
        }
        val string = app.applicationContext.getString(resId)
        app.applicationContext.getString(R.string.display_type, string)
    }

    init {
        _selectedProperty.value = marsProperty
    }
}
