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
package io.rekast.sdk.callback

import io.rekast.sdk.model.ErrorResponse

/**
 * Handles exceptions and messages for the exceptions.
 *
 * @property errorResponse The error response associated with the exception.
 */
class MomoException : Exception {
    lateinit var errorResponse: ErrorResponse

    /**
     * Constructor for MomoException with a message.
     *
     * @param message The message associated with the exception.
     */
    constructor(message: String?) : super(message)

    /**
     * Constructor for MomoException with an ErrorResponse.
     *
     * @param errorResponse The error response associated with the exception.
     */
    constructor(errorResponse: ErrorResponse) : super("${errorResponse.code} : ${errorResponse.message}") {
        this.errorResponse = errorResponse
    }
}
