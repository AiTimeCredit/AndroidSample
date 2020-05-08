package com.android.common.http;

import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.NonNull;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * 基于{@link Observer}的观察者基类
 * <ul>
 * <li>{@link #onComplete()}: 事件队列完结。RxJava不仅把每个事件单独处理，还会把它们看做一个队列。RxJava规定，当不会再有新的{@link #onNext(Object)}发出时，需要触发{@link #onComplete()}方法作为标志。
 * <li>{@link #onError(Throwable)} : 事件队列异常。在事件处理过程中出异常时，{@link #onError(Throwable)}会被触发，同时队列自动终止，不允许再有事件发出。
 * <li>在一个正确运行的事件序列中,{@link #onComplete()}和{@link #onError(Throwable)}有且只有一个，并且是事件序列中的最后一个。需要注意的是，{@link #onComplete()}和{@link #onError(Throwable)}二者也是互斥的，即在队列中调用了其中一个，就不应该再调用另一个。
 * </ul>
 */
public abstract class BaseObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {

    @Override
    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
        if (DisposableHelper.setOnce(this, d)) {
            try {
                onStart();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                d.dispose();
                onError(ex);
            }
        }
    }

    @Override
    public void onNext(@io.reactivex.annotations.NonNull T t) {
        if (!isDisposed()) {
            try {
                if (t != null) {
                    doOnNext(t);
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                get().dispose();
                onError(e);
            }
        }
    }

    @Override
    public void onError(@io.reactivex.annotations.NonNull Throwable t) {
        if (!isDisposed()) {
            lazySet(DisposableHelper.DISPOSED);
            try {
                onStop();
                if (t != null) {
                    doOnError(t);
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(new CompositeException(t, e));
            }
        } else {
            RxJavaPlugins.onError(t);
        }
    }

    @Override
    public void onComplete() {
        if (!isDisposed()) {
            lazySet(DisposableHelper.DISPOSED);
            try {
                onStop();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
        }
    }

    @Override
    public void dispose() {
        DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }

    public boolean disposable(@NonNull CompositeDisposable disposable) {
        return disposable.add(this);
    }

    protected void onStart() {
    }

    protected void onStop() {
    }

    protected abstract void doOnNext(@NonNull T t);

    protected abstract void doOnError(@NonNull Throwable t);

}
