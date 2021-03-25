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

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.databinding.FragmentOverviewBinding
import com.example.android.marsrealestate.network.MarsApiFilter

/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class OverviewFragment : Fragment() {

    // Lazily initialize viewModel, so that viewModel is created only when it is used
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this).get(OverviewViewModel::class.java)
    }

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentOverviewBinding.inflate(inflater)
        // val binding = GridViewItemBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        // Because we've set the lifecycle owner, any LiveData used in data binding will
        // automatically be observed for any changes, and the UI will be updated accordingly
        binding.lifecycleOwner = this
        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // This adds the PhotoGridAdapter.onClickListener object to the PhotoGridAdapter constructor,
        // and calls viewModel.displayPropertyDetails() with the passed-in MarsProperty object.
        // This triggers the LiveData in the view model for the navigation
        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })

        viewModel.navigateToSelectedProperty.observe(this, Observer {
            // The observer tests whether MarsProperty is not null
            if (null != it) {
                // if so, it gets the navigation controller from the fragment with findNavController()
                val directions = OverviewFragmentDirections.actionShowDetail(it)
                findNavController().navigate(directions)
                // tell the view model to reset the LiveData to the null state, so you won't
                // accidentally trigger navigation again when the app returns back to the OverviewFragment
                viewModel.displayPropertyDetailsComplete()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    /**
     * Inflates the overflow menu that contains filtering options.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val filter = when (item?.itemId) {
            R.id.show_rent_menu -> MarsApiFilter.SHOW_RENT
            R.id.show_buy_menu -> MarsApiFilter.SHOW_BUY
            else -> MarsApiFilter.SHOW_ALL
        }
        viewModel.updateFilter(filter)

        // Return true, because you've handled the menu item
        return true
    }
}
