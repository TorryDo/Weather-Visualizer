package com.torrydo.weathervisualizer.common.model


import com.torrydo.weathervisualizer.utils.Logger
import kotlin.reflect.KClass


sealed class Resource<T>(val data: T? = null, val exception: Exception? = null) {

    class Success<T>(data: T?) : Resource<T>(data)

    class Error<T>(exception: Exception?, data: T? = null) : Resource<T>(data, exception)

}

inline fun <T> Resource<T>.onSuccess(onSuccess: (data: T?) -> Unit): Resource<T> {
    if (this is Resource.Success) onSuccess(data)
    return this
}

inline fun <T> Resource<T>.onError(onError: (exception: Exception?, data: T?) -> Unit): Resource<T> {
    if (this is Resource.Error) onError(exception, data)
    return this
}


fun <T> resourceError(message: String) = Resource.Error<T>(Exception(message))


fun compareResource(vararg rscArray: Resource<Nothing>): Resource<Nothing> {
    rscArray.forEach {
        if (it is Resource.Error) {
            return Resource.Error(it.exception)
        }
    }
    return Resource.Success(null)
}

// support functions -------------------------------------------------------------------------------

val successNothing by lazy { Resource.Success(data = null) }
//val errorNothing by lazy { Resource.Error<Nothing>(null) }

/**
 * catch given exception if error appear
 * */
inline fun <T, R : Exception> Resource<T>.catch(
    exception: KClass<R>,
    onCatch: (data: R) -> Unit
): Resource<T> {
    if (this is Resource.Error && this.exception != null) {
        if (exception.isInstance(this.exception)) {
            val exc = try {
                this.exception as R
            } catch (e: Exception) {
                Logger.e(e.stackTraceToString())
                return this
            }
            onCatch(exc)
        }
    }
    return this
}

/**
 * given exception will be ignored from 'onError' callback
 * */
inline fun <T, R : Exception> Resource<T>.onErrorCatch(
    exception: KClass<R>,
    onCatch: (data: R) -> Unit,
    onError: (exception: Exception?, data: T?) -> Unit
): Resource<T> {
    if (this is Resource.Error && this.exception != null) {
        if (exception.isInstance(this.exception)) {
            val exc = try {
                this.exception as R
            } catch (e: Exception) {
                Logger.e(e.stackTraceToString())
                return this
            }
            onCatch(exc)
        } else {
            onError(this.exception, this.data)
        }
    }
    return this
}

