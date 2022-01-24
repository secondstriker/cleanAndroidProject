package com.codewithmohsen.lastnews.repository

import com.codewithmohsen.lastnews.Config.LONG_LOADING_THRESHOLD
import com.codewithmohsen.lastnews.R
import com.codewithmohsen.lastnews.api.APIErrorResponse
import com.codewithmohsen.lastnews.api.ErrorModel
import com.codewithmohsen.lastnews.api.NetworkResponse
import com.codewithmohsen.lastnews.di.CoroutinesScopesModule.ApplicationScope
import com.codewithmohsen.lastnews.di.IoDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

abstract class BaseOnlineRepository<ApiModel: Any, ResultModel: Any>(
    @ApplicationScope private val externalCoroutineScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private val _result = MutableStateFlow<Resource<ResultModel>>(Resource.loading(null))

    protected suspend fun fetch(refresh: Boolean) = withContext(ioDispatcher) {

        val apiJob = launch(ioDispatcher) { getData(refresh) }
        val longLoadingJob = launch(ioDispatcher) { longLoading() }
         apiJob.invokeOnCompletion { cause ->
             Timber.d("invokeOnCompletion")
             if (longLoadingJob.isActive) {
                 Timber.d("invokeOnCompletion long loading is cancelled.")
                 longLoadingJob.cancel()
             }
             if(apiJob.isCancelled) {
                 Timber.d("invokeOnCompletion main job is cancelled.")
                 externalCoroutineScope.launch(ioDispatcher) {
                     setValue(Resource.cancel(_result.value.data))
                 }
             }
             //We can do anything with our externalCoroutineScope here for example analytics
         }
        listOf(apiJob, longLoadingJob).joinAll()
    }

    private suspend fun getData(refresh: Boolean) {
        Timber.d("getData")
        if(refresh)
            setValue(Resource.loading(null))
        else
            setValue(Resource.loading(_result.value.data))

        when (val result = apiCall()) {
            is NetworkResponse.APIError -> {
                when (result.apiErrorResponse) {
                    is APIErrorResponse.ClientErrorResponse -> {
                        //
                        Timber.d("APIErrorResponse.ClientErrorResponse, " +
                                "the error is ${result.apiErrorResponse.error.message}")
                        setErrorValue(
                            R.string.api_error,
                            _result.value.data
                        )
                    }
                    is APIErrorResponse.ServerErrorResponse -> {
                        Timber.d("APIErrorResponse.ServerErrorResponse, " +
                                "the error is ${result.apiErrorResponse.error.message}")
                        setErrorValue(
                            R.string.server_error,
                            _result.value.data
                        )
                    }
                    is APIErrorResponse.Unauthenticated -> {
                        Timber.d("APIErrorResponse.Unauthenticated, " +
                                "the error is ${result.apiErrorResponse.error.message}")
                        setErrorValue(
                            R.string.unknown_error,
                            _result.value.data
                        )
                    }
                    is APIErrorResponse.UnexpectedErrorResponse -> {
                        Timber.d("APIErrorResponse.UnexpectedErrorResponse, " +
                                "the error is ${result.apiErrorResponse.error.message}")
                        setErrorValue(
                           R.string.unknown_error,
                            _result.value.data
                        )
                    }
                }

            }
            is NetworkResponse.Empty -> {
                Timber.d("NetworkResponse.Empty, the result is ${result.body}")
                setValue(Resource.success(bodyToResult(result.body)))
            }
            is NetworkResponse.NetworkError -> {
                Timber.d("NetworkResponse.NetworkError, the error is ${result.exception.message}")
                setValue(Resource.networkError(R.string.network_error, _result.value.data))
            }
            is NetworkResponse.Success -> {
                Timber.d("NetworkResponse.Success, the result is ${result.body}")
                setValue(Resource.success(bodyToResult(result.body)))
            }
            is NetworkResponse.UnknownError -> {
                Timber.d("NetworkResponse.UnknownError, the error is ${result.throwable?.message}")
                setErrorValue(R.string.unknown_error, _result.value.data)
            }
        }
    }

    private suspend fun setValue(resource: Resource<ResultModel>) {
        _result.emit(resource)
    }

    private suspend fun setErrorValue(messageResource: Int, data: ResultModel?) {
        _result.emit(Resource.error(messageResource, data))
    }

    private suspend fun longLoading() = withContext(ioDispatcher) {
        delay(LONG_LOADING_THRESHOLD)
        if(_result.value.status == Status.LOADING)
            setValue(Resource.longLoading(_result.value.data))
    }

    protected open fun getResultAsFlow() = _result.asStateFlow()
    protected abstract suspend fun apiCall(): NetworkResponse<ApiModel,  APIErrorResponse<ErrorModel>>
    protected abstract suspend fun bodyToResult(apiModel: ApiModel?): ResultModel
}