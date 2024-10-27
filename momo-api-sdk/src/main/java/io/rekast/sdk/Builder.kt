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
package io.rekast.sdk

import io.rekast.sdk.network.api.route.Routes
import io.rekast.sdk.repository.Repository

/**
 * Keys Builder. Creates a [Routes] payment load.
 * @param [apiUserId] for your application
 */

class Builder(private var apiUserId: String) {
    private lateinit var environment: String
    private lateinit var baseURL: String

    fun getBaseURL(baseURL: String): Builder {
        this.baseURL = baseURL
        return this
    }
    fun setEnvironment(environment: String): Builder {
        this.environment = environment
        return this
    }

    fun build(): Routes {
        val routes = Routes
        routes.repository = Repository(
            apiUserId,
            baseURL,
            environment
        )
        return routes
    }
}
