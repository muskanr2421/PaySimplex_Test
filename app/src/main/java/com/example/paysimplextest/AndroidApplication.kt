/**
 * Copyright (C) 2020 Fernando Cejas Open Source Project
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
package com.example.paysimplextest

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.mapbox.mapboxsdk.Mapbox
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class  AndroidApplication : Application(){
    val ACCESS_TOKEN = "pk.eyJ1IjoibXVza2FuciIsImEiOiJjbDk0MXJxNHgwNGdmM3BuMzV3aDJ3dW52In0.QbQ00p_Ki8-3ijsuQScxAA"

    override fun onCreate() {
        super.onCreate()
        Mapbox.getInstance(applicationContext, ACCESS_TOKEN)
    }

}
