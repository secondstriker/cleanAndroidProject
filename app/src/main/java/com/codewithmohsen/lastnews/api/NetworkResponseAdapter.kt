package com.codewithmohsen.lastnews.api

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.*
import timber.log.Timber
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class NetworkResponseAdapter<S : Any, E : APIErrorResponse<ErrorBody>>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, ErrorBody>
) : CallAdapter<S, Call<NetworkResponse<S, E>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<NetworkResponse<S, E>> {
        return NetworkResponseCall(call, errorBodyConverter)
    }

    internal class NetworkResponseCall<S : Any, E : APIErrorResponse<ErrorBody>>(
        private val delegate: Call<S>,
        private val errorConverter: Converter<ResponseBody, ErrorBody>
    ) : Call<NetworkResponse<S, E>> {

        override fun enqueue(callback: Callback<NetworkResponse<S, E>>) {
            return delegate.enqueue(object : Callback<S> {
                override fun onResponse(call: Call<S>, response: Response<S>) {
                    val body = response.body()
                    val code = response.code()
                    val error = response.errorBody()

                    if (response.isSuccessful) {
                        Timber.d("API response successful.")
                        if (body != null) {
                            // API success
                            // No need to check 204, because we checked nullability of the body
                            // In this case we only have 200 for success but we follow general roles
                            check(code in 200..299)
                            Timber.d("API success")
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(NetworkResponse.Success(body))
                            )
                        } else {
                            Timber.d("API body is null")
                            // Response is successful but the body is null
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(NetworkResponse.Empty(null))
                            )
                        }
                    } else {
                        Timber.d("API response is not successful.")
                        val errorBody = convertToErrorBody(error)
                        Timber.d("Code: $code Error body: $errorBody")
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(
                                NetworkResponse.APIError(
                                    createApiErrorResponse(
                                        code,
                                        errorBody ?: ErrorBody(ERROR_MESSAGE_IS_NULL)
                                    )
                                )
                                        as NetworkResponse<S, E>
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<S>, throwable: Throwable) {
                    Timber.d("API onFailure.")
                    val networkResponse = when (throwable) {
                        is IOException -> NetworkResponse.NetworkError(throwable)
                        else -> NetworkResponse.UnknownError(throwable)
                    }
                    callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
                }
            })
        }

        private fun createApiErrorResponse(
            code: Int,
            errorBody: ErrorBody
        ): APIErrorResponse<ErrorBody> {
            return when (code) {
                401 -> APIErrorResponse.Unauthenticated(errorBody)
                in 400..499 -> APIErrorResponse.ClientErrorResponse(errorBody)
                in 500..599 -> APIErrorResponse.ServerErrorResponse(errorBody)
                else -> APIErrorResponse.UnexpectedErrorResponse(errorBody)
            }
        }

        /**
         * We use Kotlin reflection for converting ResponseBody to ErrorBody.
         */
        private fun convertToErrorBody(error: ResponseBody?): ErrorBody? {
            return when {
                error == null -> null
                error.contentLength() == 0L -> null
                else -> try {
                    //we use kotlin reflection for converting the body to ErrorBody Class
                    errorConverter.convert(error)
                } catch (ex: Exception) {
                    null
                }
            }
        }

        override fun isExecuted() = delegate.isExecuted

        override fun clone() = NetworkResponseCall<S, E>(delegate.clone(), errorConverter)

        override fun isCanceled() = delegate.isCanceled

        override fun cancel() = delegate.cancel()

        override fun execute(): Response<NetworkResponse<S, E>> {
            throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
        }

        override fun request(): Request = delegate.request()

        override fun timeout(): Timeout {
            return Timeout().timeout(Constants.REQUEST_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        }
    }

    companion object {
        const val ERROR_MESSAGE_IS_NULL = "Something went wrong."
    }
}
