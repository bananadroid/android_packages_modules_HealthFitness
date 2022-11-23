/**
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * ```
 *      http://www.apache.org/licenses/LICENSE-2.0
 * ```
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.android.healthconnect.controller.tests.autodelete

import com.android.healthconnect.controller.autodelete.AutoDeleteRange
import com.android.healthconnect.controller.autodelete.autoDeleteRangeEnd
import com.android.healthconnect.controller.autodelete.autoDeleteRangeStart
import com.android.healthconnect.controller.autodelete.numberOfMonths
import com.android.healthconnect.controller.tests.utils.NOW
import com.android.healthconnect.controller.utils.TimeSource
import com.google.common.truth.Truth.assertThat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import org.junit.Assert.assertThrows
import org.junit.Test

class AutoDeleteTest {
    private val timeSource: TimeSource =
        object : TimeSource {
            override fun currentTimeMillis(): Long = NOW.toEpochMilli()
            override fun deviceZoneOffset(): ZoneId = ZoneOffset.UTC
            override fun currentLocalDateTime(): LocalDateTime =
                Instant.ofEpochMilli(currentTimeMillis())
                    .atZone(deviceZoneOffset())
                    .toLocalDateTime()
        }

    @Test
    fun numberOfMonths_rangeNever_throws() {
        val thrown =
            assertThrows(UnsupportedOperationException::class.java) {
                numberOfMonths(AutoDeleteRange.AUTO_DELETE_RANGE_NEVER)
            }
        assertThat(thrown)
            .hasMessageThat()
            .isEqualTo("Unable determine the number of months in the auto-delete range.")
    }

    @Test
    fun numberOfMonths_threeMonths_returnCorrectValue() {
        assertThat(numberOfMonths(AutoDeleteRange.AUTO_DELETE_RANGE_THREE_MONTHS)).isEqualTo(3)
    }

    @Test
    fun numberOfMonths_eighteenMonths_returnCorrectValue() {
        assertThat(numberOfMonths(AutoDeleteRange.AUTO_DELETE_RANGE_EIGHTEEN_MONTHS)).isEqualTo(18)
    }

    @Test
    fun deletionStart() {
        assertThat(autoDeleteRangeStart()).isEqualTo(Instant.EPOCH)
    }

    @Test
    fun deletionEnd_rangeNever_throws() {
        val thrown =
            assertThrows(UnsupportedOperationException::class.java) {
                autoDeleteRangeEnd(timeSource, AutoDeleteRange.AUTO_DELETE_RANGE_NEVER)
            }
        assertThat(thrown).hasMessageThat().isEqualTo("Invalid auto-delete range to delete data.")
    }

    @Test
    fun deletionEnd_threeMonths_returnCorrectValue() {
        assertThat(
                autoDeleteRangeEnd(timeSource, AutoDeleteRange.AUTO_DELETE_RANGE_THREE_MONTHS)
                    .toString())
            .isEqualTo("2022-07-20T00:00:00Z")
    }

    @Test
    fun deletionEnd_eighteenMonths_returnCorrectValue() {
        assertThat(
                autoDeleteRangeEnd(timeSource, AutoDeleteRange.AUTO_DELETE_RANGE_EIGHTEEN_MONTHS)
                    .toString())
            .isEqualTo("2021-04-20T00:00:00Z")
    }
}