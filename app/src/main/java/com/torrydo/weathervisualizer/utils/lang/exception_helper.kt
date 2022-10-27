package com.torrydo.weathervisualizer.utils.lang

import android.util.Log
import com.torrydo.weathervisualizer.common.model.Resource
import com.torrydo.weathervisualizer.utils.Logger
import com.torrydo.weathervisualizer.utils.addLoggerPrefix
import kotlin.reflect.KClass


sealed class ActionState(data: Any? = null, exception: Exception? = null) {
    class Success(val data: Any? = null) : ActionState(data = data)
    class Error(val e: Exception) : ActionState(exception = e)
}

// use ---------------------------------------------------------------------------------------------

/**
 * if error appear, do nothing
 * */
inline fun tryOnly(crossinline mayErrorWork: () -> Unit) {
    try {
        mayErrorWork()
    } catch (e: Exception) {
    }
}

inline fun mayError(
    crossinline mayErrorWork: () -> Unit,
): ActionState {
    try {
        mayErrorWork()
    } catch (e: Exception) {
        return ActionState.Error(e)
    }

    return ActionState.Success()
}

// TODO: working on this
inline fun <T : Exception> ActionState.catch(
    exception: KClass<T>,
    onCatch: (data: T) -> Unit
): ActionState {
    if (this is ActionState.Error) {
        if (exception.isInstance(this.e).not()) return this
        val exc = try {
            this.e as T
        } catch (e: Exception) {
            Logger.e(e.stackTraceToString())
            return this
        }
        onCatch(exc)
    }

    return this
}

//inline fun CoroutineScope.mayError(
//    crossinline mayErrorWork: suspend () -> Unit,
//): ActionState {
//    try {
//        this.launch {
//            mayErrorWork()
//        }
//    } catch (e: Exception) {
//        if (e::class == CancellationException::class) throw e
//        return ActionState.Error(e)
//    }
//
//
//    return ActionState.Success()
//}

/**
 * if error appear, print to console
 * */
inline fun <T : Any> T.logIfError(
    tag: String? = javaClass.simpleName.toString(),
    crossinline mayErrorWork: () -> Unit,
): ActionState {
    try {
        mayErrorWork()
    } catch (e: Exception) {
        Log.e(tag.addLoggerPrefix(), e.message.toString())
        return ActionState.Error(e)
    }

    return ActionState.Success()
}

inline fun <T : Any, R : Any?> T.logIfErrorWith(
    tag: String? = javaClass.simpleName.toString(),
    crossinline mayErrorWork: () -> R,
): Resource<R> {
    return try {
        val rs = mayErrorWork()

        Resource.Success(rs)
    } catch (e: Exception) {
        Log.e(tag.addLoggerPrefix(), e.message.toString())

        Resource.Error(e)
    }
}

// extension ---------------------------------------------------------------------------------------

inline fun ActionState.onSuccess(crossinline onIfComplete: () -> Unit): ActionState {

    if (this is ActionState.Success) {
        onIfComplete()
    }
    return this
}

inline fun ActionState.onError(crossinline onIfError: (e: Exception) -> Unit): ActionState {

    if (this is ActionState.Error) {
        onIfError(this.e)
    }
    return this
}

// custom exception

class PermissionDeniedException : Exception() {
    override val message: String
        get() = "display-over-other-app permission IS NOT granted!"
}




