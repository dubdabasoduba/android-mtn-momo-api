/*
 * Copyright 2023 - 2024, Benjamin Mwalimu
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
package io.rekast.sdk.sample.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import io.rekast.sdk.model.authentication.AccessToken
import io.rekast.sdk.sample.BuildConfig
import java.text.SimpleDateFormat
import java.util.Calendar
import org.apache.commons.lang3.StringUtils

/**
 * Utility object providing various helper functions for the MTN MOMO SDK sample application.
 *
 * This object contains methods for saving and retrieving API keys and access tokens,
 * as well as checking token expiration and formatting dates.
 */
object Utils {
    /**
     * Saves the provided API key in the shared preferences.
     *
     * @param context The context used to access shared preferences.
     * @param apiKey The API key to be saved.
     */
    fun saveApiKey(context: Context, apiKey: String) {
        val mSettings = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE)
        val editor = mSettings.edit()

        editor.putString("apiKey", apiKey)
        editor.apply()
    }

    /**
     * Retrieves the saved API key from shared preferences.
     *
     * @param context The context used to access shared preferences.
     * @return The saved API key as a String, or an empty string if not found.
     */
    fun getApiKey(context: Context): String {
        val mSettings = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE)
        return mSettings.getString("apiKey", "").toString()
    }

    /**
     * Saves the provided access token in the shared preferences along with its expiry date.
     *
     * @param context The context used to access shared preferences.
     * @param accessToken The access token to be saved.
     */
    fun saveAccessToken(context: Context, accessToken: AccessToken?) {
        val tokenExpiry = if (StringUtils.isNotBlank(accessToken!!.expiresIn)) {
            accessToken.expiresIn.toIntOrNull()
        } else { 1 }
        val cal = Calendar.getInstance()
        cal.add(Calendar.MINUTE, tokenExpiry!!)
        val oneHourAfter = cal.timeInMillis

        val mSettings = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE)
        val editor = mSettings.edit()

        editor.putString("accessToken", accessToken.accessToken)
        editor.putLong("expiryDate", oneHourAfter)
        editor.putString("tokenType", accessToken.tokenType)
        editor.apply()
    }

    /**
     * Retrieves the saved access token from shared preferences.
     *
     * @param context The context used to access shared preferences.
     * @return The saved access token as a String, or an empty string if expired or not found.
     */
    fun getAccessToken(context: Context): String {
        return if (expired(context)) {
            ""
        } else {
            val mSettings = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE)
            mSettings.getString("accessToken", "").toString()
        }
    }

    /**
     * Checks if the saved access token has expired.
     *
     * @param context The context used to access shared preferences.
     * @return True if the token is expired, false otherwise.
     */
    private fun expired(context: Context): Boolean {
        val mSettings = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE)
        val expiryTime = mSettings.getLong("expiryDate", 0)
        val currentTime = Calendar.getInstance().timeInMillis
        return currentTime > expiryTime
    }

    /**
     * Converts the given milliseconds to a formatted date string.
     *
     * @param milliseconds The time in milliseconds to convert.
     * @return A formatted date string in "yyyy-MM-dd" format.
     */
    fun convertToDate(milliseconds: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat.format(milliseconds)
    }
}
