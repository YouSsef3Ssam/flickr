package com.youssef.flickr.framework.utils.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.youssef.flickr.framework.utils.Constants.debounceTime
import com.youssef.flickr.framework.utils.EspressoIdlingResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun Fragment.navigateTo(direction: NavDirections) {
    try {
        findNavController().navigateSafe(direction)
    } catch (e: IllegalStateException) {
        Timber.e(e)
    } catch (e: IllegalArgumentException) {
        Timber.e(e)
    }
}

fun NavController.navigateSafe(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}

fun CoroutineScope.launchIdling(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    EspressoIdlingResource.increment()
    val job = this.launch(context, start, block)
    job.invokeOnCompletion {
        EspressoIdlingResource.decrement()
    }
    return job
}

@FlowPreview
fun <T> MutableLiveData<T>.debounceFlow(scope: CoroutineScope, actionToTake: (T) -> Unit) {
    this.asFlow().debounce { debounceTime }
        .distinctUntilChanged()
        .onEach { query -> actionToTake(query) }
        .launchIn(scope)
}
