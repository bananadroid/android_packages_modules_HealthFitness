/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.healthconnect.controller.tests.recentaccess

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.android.healthconnect.controller.R
import com.android.healthconnect.controller.recentaccess.RECENT_APP_1
import com.android.healthconnect.controller.recentaccess.RecentAccessFragment
import com.android.healthconnect.controller.recentaccess.RecentAccessViewModel
import com.android.healthconnect.controller.tests.utils.launchFragment
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@HiltAndroidTest
class RecentAccessFragmentTest {

    @get:Rule val hiltRule = HiltAndroidRule(this)

    @BindValue
    val viewModel: RecentAccessViewModel = Mockito.mock(RecentAccessViewModel::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_RecentAccessFragment_displaysCorrectly() {
        Mockito.`when`(viewModel.recentAccessApps).then { MutableLiveData(listOf(RECENT_APP_1)) }

        launchFragment<RecentAccessFragment>(Bundle())
        onView(withText(R.string.today_header)).check(matches(isDisplayed()))
        onView(withText(R.string.recent_app_1)).check(matches(isDisplayed()))
    }
}