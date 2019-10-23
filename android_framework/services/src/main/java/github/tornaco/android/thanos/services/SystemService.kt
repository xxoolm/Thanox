package github.tornaco.android.thanos.services

import android.content.Context
import android.os.UserHandle
import github.tornaco.android.thanos.core.annotation.CallSuper
import github.tornaco.android.thanos.core.util.DevNull
import github.tornaco.android.thanos.core.util.Numbers
import github.tornaco.android.thanos.core.util.Rxs
import github.tornaco.android.thanos.core.util.Timber
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Action
import java.util.concurrent.TimeUnit

open class SystemService {

    protected var isSystemReady: Boolean = false

    protected var context: Context? = null

    protected var isNotificationPostReady: Boolean = false

    @CallSuper
    open fun onStart(context: Context) {
        Timber.i("onStart")
        this.context = context
    }

    @CallSuper
    open fun systemReady() {
        Timber.i("systemReady@" + serviceName())
        isSystemReady = true
        setNotificationReadyDelayed()
    }

    @CallSuper
    open fun shutdown() {
        Timber.i("shutdown")
    }

    protected open fun serviceName(): String {
        return javaClass.simpleName
    }

    protected fun dumpCallingUserId(msg: String) {
        if (isSystemReady) Timber.v("Calling userId: %s %s", msg, UserHandle.getCallingUserId())
    }

    protected fun isSystemUser(): Boolean {
        return isSystemReady && UserHandle.USER_SYSTEM == UserHandle.getCallingUserId()
    }

    /**
     * Execute in {@link #ThanosSchedulers.serverThread()}
     */
    protected fun executeInternal(runnable: Runnable) {
        Completable.fromRunnable(runnable)
            .subscribeOn(ThanosSchedulers.serverThread()).subscribe()
    }

    /**
     * Execute in {@link #ThanosSchedulers.serverThread()}
     */
    protected fun executeInternal(runnable: Runnable, delayMills: Long) {
        Completable.fromRunnable(runnable)
            .delay(delayMills, TimeUnit.MILLISECONDS)
            .subscribeOn(ThanosSchedulers.serverThread()).subscribe()
    }

    private fun setNotificationReadyDelayed() {
        DevNull.accept(
            Observable
                .empty<Any>()
                .delay(Numbers._9.toLong(), TimeUnit.SECONDS)
                .subscribe(
                    Rxs.EMPTY_CONSUMER,
                    Rxs.ON_ERROR_LOGGING,
                    Action {
                        Timber.d("isNotificationPostReady = true ...")
                        isNotificationPostReady = true
                    })
        )
    }
}