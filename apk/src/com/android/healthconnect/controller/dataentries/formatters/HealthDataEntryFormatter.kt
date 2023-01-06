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
package com.android.healthconnect.controller.dataentries.formatters

import android.healthconnect.datatypes.ActiveCaloriesBurnedRecord
import android.healthconnect.datatypes.BasalMetabolicRateRecord
import android.healthconnect.datatypes.DistanceRecord
import android.healthconnect.datatypes.HeartRateRecord
import android.healthconnect.datatypes.HeightRecord
import android.healthconnect.datatypes.PowerRecord
import android.healthconnect.datatypes.Record
import android.healthconnect.datatypes.SpeedRecord
import android.healthconnect.datatypes.StepsCadenceRecord
import android.healthconnect.datatypes.StepsRecord
import android.healthconnect.datatypes.TotalCaloriesBurnedRecord
import com.android.healthconnect.controller.dataentries.FormattedDataEntry
import com.android.healthconnect.controller.shared.AppInfoReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthDataEntryFormatter
@Inject
constructor(
    private val appInfoReader: AppInfoReader,
    private val heartRateFormatter: HeartRateFormatter,
    private val stepsFormatter: StepsFormatter,
    private val stepsCadenceFormatter: StepsCadenceFormatter,
    private val basalMetabolicRateFormatter: BasalMetabolicRateFormatter,
    private val speedFormatter: SpeedFormatter,
    private val distanceFormatter: DistanceFormatter,
    private val powerFormatter: PowerFormatter,
    private val activeCaloriesBurnedFormatter: ActiveCaloriesBurnedFormatter,
    private val totalCaloriesBurnedFormatter: TotalCaloriesBurnedFormatter,
    private val heightFormatter: HeightFormatter
) {

    suspend fun format(record: Record): FormattedDataEntry {
        val appName = getAppName(record)
        return when (record) {
            is HeartRateRecord -> heartRateFormatter.format(record, appName)
            is StepsRecord -> stepsFormatter.format(record, appName)
            is StepsCadenceRecord -> stepsCadenceFormatter.format(record, appName)
            is BasalMetabolicRateRecord -> basalMetabolicRateFormatter.format(record, appName)
            is SpeedRecord -> speedFormatter.format(record, appName)
            is DistanceRecord -> distanceFormatter.format(record, appName)
            is PowerRecord -> powerFormatter.format(record, appName)
            is ActiveCaloriesBurnedRecord -> activeCaloriesBurnedFormatter.format(record, appName)
            is TotalCaloriesBurnedRecord -> totalCaloriesBurnedFormatter.format(record, appName)
            is HeightRecord -> heightFormatter.format(record, appName)
            else -> throw IllegalArgumentException("${record::class.java} Not supported!")
        }
    }

    private suspend fun getAppName(record: Record): String {
        return appInfoReader.getAppMetadata(record.metadata.dataOrigin.packageName).appName
    }
}
