/*
 * Copyright 2023, Benjamin Mwalimu Mulyungi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.rekast.sdk.sample.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import io.rekast.sdk.sample.R
import io.rekast.sdk.sample.utils.DefaultDispatcherProvider
import javax.inject.Inject
import kotlin.getValue
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalMaterialApi
open class AppMainActivity() : AppCompatActivity() {
    @Inject
    lateinit var dispatcherProvider: DefaultDispatcherProvider
    lateinit var navHostFragment: NavHostFragment
    private val appMainViewModel by viewModels<AppMainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FragmentContainerView(this).apply { id = R.id.navigation_host })
        navHostFragment =
            NavHostFragment.create(R.navigation.navigation_graph)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navigation_host, navHostFragment)
            .setPrimaryNavigationFragment(navHostFragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        appMainViewModel.viewModelScope.launch(dispatcherProvider.io()) { appMainViewModel.checkUser() }
    }
}
