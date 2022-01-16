package com.codewithmohsen.lastnews.repository

import com.codewithmohsen.lastnews.Config.LONG_LOADING_THRESHOLD
import com.codewithmohsen.lastnews.api.APIErrorResponse
import com.codewithmohsen.lastnews.api.ErrorModel
import com.codewithmohsen.lastnews.api.NetworkResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

abstract class BaseOnlineRepository<ApiModel: Any, ResultModel: Any> constructor(
    private val externalCoroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val _result = MutableStateFlow<Resource<ResultModel>>(Resource.loading(null))

    protected suspend fun fetch(): Unit = withContext(ioDispatcher) {

        val apiJob = async(ioDispatcher) { getData() }
        val longLoadingJob = async(ioDispatcher) { longLoading() }

        apiJob.await()
        longLoadingJob.await()
         apiJob.invokeOnCompletion { cause ->
             if (longLoadingJob.isActive)
                 longLoadingJob.cancel()
             if(apiJob.isCancelled) {
                 externalCoroutineScope.launch(ioDispatcher) {
                     setValue(Resource.cancel(_result.value.data))
                 }
             }
             //We can do anything with our externalCoroutineScope here for example analytics
         }
    }

    private suspend fun getData() {
        setValue(Resource.loading(_result.value.data))

        when (val result = apiCall()) {
            is NetworkResponse.APIError -> {
                when (result.apiErrorResponse) {
                    is APIErrorResponse.ClientErrorResponse -> {
                        setErrorValue(
                            result.apiErrorResponse.error.message,
                            null
                        )
                    }
                    is APIErrorResponse.ServerErrorResponse -> {
                        setErrorValue(
                            result.apiErrorResponse.error.message,
                            null
                        )
                    }
                    is APIErrorResponse.Unauthenticated -> {
                        setErrorValue(
                            result.apiErrorResponse.error.message,
                            null
                        )
                    }
                    is APIErrorResponse.UnexpectedErrorResponse -> {
                        setErrorValue(
                            result.apiErrorResponse.error.message,
                            null
                        )
                    }
                }

            }
            is NetworkResponse.Empty -> {
                setValue(Resource.success(bodyToResult(result.body)))
            }
            is NetworkResponse.NetworkError -> {
                setValue(Resource.error(result.exception.message, null))
            }
            is NetworkResponse.Success -> {
                setValue(Resource.success(bodyToResult(result.body)))
            }
            is NetworkResponse.UnknownError -> {
                setValue(Resource.error(result.throwable?.message, null))
            }
        }
    }

    private suspend fun setValue(resource: Resource<ResultModel>) {
        _result.emit(resource)
    }

    private suspend fun setErrorValue(message: String?, data: ResultModel?) {
        _result.emit(Resource.error(message, data))
    }

    private suspend fun longLoading() = withContext(ioDispatcher) {
        delay(LONG_LOADING_THRESHOLD)
        if(_result.value.status == Status.LOADING)
            setValue(Resource.longLoading(_result.value.data))
    }

    protected open fun getResultAsFlow() = _result.asStateFlow()
    abstract suspend fun apiCall(): NetworkResponse<ApiModel,  APIErrorResponse<ErrorModel>>
    abstract suspend fun bodyToResult(apiModel: ApiModel?): ResultModel
}