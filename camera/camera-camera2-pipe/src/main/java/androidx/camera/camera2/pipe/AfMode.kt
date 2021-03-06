/*
 * Copyright 2020 The Android Open Source Project
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

package androidx.camera.camera2.pipe

import android.hardware.camera2.CameraMetadata

/**
 * An enum to match the CameraMetadata.CONTROL_AF_MODE_* constants.
 */
public enum class AfMode(public val value: Int) {
    OFF(CameraMetadata.CONTROL_AF_MODE_OFF),
    AUTO(CameraMetadata.CONTROL_AF_MODE_AUTO),
    MACRO(CameraMetadata.CONTROL_AF_MODE_MACRO),
    CONTINUOUS_VIDEO(CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_VIDEO),
    CONTINUOUS_PICTURE(CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE),
    EDOF(CameraMetadata.CONTROL_AF_MODE_EDOF);

    public companion object {
        @JvmStatic
        public fun fromIntOrNull(value: Int): AfMode? = values().firstOrNull { it.value == value }
    }
}