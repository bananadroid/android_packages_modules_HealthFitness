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
package android.healthconnect.datatypes;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Captures a description of how heavy a user's menstrual flow was (spotting, light, medium, or
 * heavy). Each record represents a description of how heavy the user's menstrual bleeding was.
 */
@Identifier(recordIdentifier = RecordTypeIdentifier.RECORD_TYPE_MENSTRUATION_FLOW)
public final class MenstruationFlowRecord extends InstantRecord {

    private final int mFlow;

    /**
     * @param metadata Metadata to be associated with the record. See {@link Metadata}.
     * @param time Start time of this activity
     * @param zoneOffset Zone offset of the user when the activity started
     * @param flow Flow of this activity
     */
    private MenstruationFlowRecord(
            @NonNull Metadata metadata,
            @NonNull Instant time,
            @NonNull ZoneOffset zoneOffset,
            @MenstruationFlowType.MenstruationFlowTypes int flow) {
        super(metadata, time, zoneOffset);
        Objects.requireNonNull(metadata);
        Objects.requireNonNull(time);
        Objects.requireNonNull(zoneOffset);
        mFlow = flow;
    }

    /**
     * @return menstruation flow
     */
    public int getFlow() {
        return mFlow;
    }

    /** Identifier for Menstruation Flow */
    public static final class MenstruationFlowType {
        public static final int FLOW_UNKNOWN = 0;
        public static final int FLOW_LIGHT = 1;
        public static final int FLOW_MEDIUM = 2;
        public static final int FLOW_HEAVY = 3;

        MenstruationFlowType() {}

        /** @hide */
        @IntDef({FLOW_UNKNOWN, FLOW_LIGHT, FLOW_MEDIUM, FLOW_HEAVY})
        @Retention(RetentionPolicy.SOURCE)
        public @interface MenstruationFlowTypes {}
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        MenstruationFlowRecord that = (MenstruationFlowRecord) o;
        return getFlow() == that.getFlow();
    }

    /** Returns a hash code value for the object. */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFlow());
    }

    /** Builder class for {@link MenstruationFlowRecord} */
    public static final class Builder {
        private final Metadata mMetadata;
        private final Instant mTime;
        private ZoneOffset mZoneOffset;
        private final int mFlow;

        /**
         * @param metadata Metadata to be associated with the record. See {@link Metadata}.
         * @param time Start time of this activity
         * @param flow How heavy the user's menstrual flow was. Optional field. Allowed values:
         *     {@link MenstruationFlowType}.
         */
        public Builder(
                @NonNull Metadata metadata,
                @NonNull Instant time,
                @MenstruationFlowType.MenstruationFlowTypes int flow) {
            Objects.requireNonNull(metadata);
            Objects.requireNonNull(time);
            mMetadata = metadata;
            mTime = time;
            mZoneOffset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
            mFlow = flow;
        }

        /** Sets the zone offset of the user when the activity happened */
        @NonNull
        public Builder setZoneOffset(@NonNull ZoneOffset zoneOffset) {
            Objects.requireNonNull(zoneOffset);
            mZoneOffset = zoneOffset;
            return this;
        }

        /**
         * @return Object of {@link MenstruationFlowRecord}
         */
        @NonNull
        public MenstruationFlowRecord build() {
            return new MenstruationFlowRecord(mMetadata, mTime, mZoneOffset, mFlow);
        }
    }
}