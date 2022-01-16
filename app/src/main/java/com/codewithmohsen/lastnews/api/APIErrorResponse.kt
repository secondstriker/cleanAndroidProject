package com.codewithmohsen.lastnews.api

sealed class APIErrorResponse<E> {

    /**
     * Unauthenticated User (error 401)
     */
    data class Unauthenticated<E>(val error: E) : APIErrorResponse<E>()

    /**
     * Called for [400, 500) responses, except 401.
     * In this case, we have 400 - Bad Request. The request was unacceptable,
     * often due to a missing or misconfigured parameter.
     * And 429 - Too Many Requests. You made too many requests within a window of time and have been rate limited.
     * Back off for a while.
     **/
    data class ClientErrorResponse<E>(val error: E) : APIErrorResponse<E>()

    /**
     * Called for [500, 600)
     * In this case we have 500 - Server Error. Something went wrong on our side.
     **/
    data class ServerErrorResponse<E>(val error: E) : APIErrorResponse<E>()

    /**
     *  Unexpected
     **/
    data class UnexpectedErrorResponse<E>(val error: E) : APIErrorResponse<E>()
}