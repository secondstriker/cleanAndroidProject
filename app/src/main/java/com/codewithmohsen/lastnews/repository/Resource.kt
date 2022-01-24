package com.codewithmohsen.lastnews.repository


/**
 * A generic class that holds a value with its status.
 * @param <T>
</T> */
data class Resource<out T>(val status: Status, val data: T?, val messageResource: Int?) {
    companion object {

        @JvmStatic
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }
        @JvmStatic
        fun <T> error(msg: Int, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }
        @JvmStatic
        fun <T> networkError(msg: Int, data: T?): Resource<T> {
            return Resource(Status.NETWORK_ERROR, data, msg)
        }
        @JvmStatic
        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
        @JvmStatic
        fun <T> longLoading(data: T?): Resource<T> {
            return Resource(Status.LONG_LOADING, data, null)
        }
        @JvmStatic
        fun <T> cancel(data: T?): Resource<T> {
            return Resource(Status.CANCEL, data, null)
        }
    }
}