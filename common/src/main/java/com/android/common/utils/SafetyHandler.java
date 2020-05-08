package com.android.common.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;

/**
 * 安全的Handler(解决Handler导致的内存泄漏)
 */
public final class SafetyHandler extends Handler {

    private final WeakReference<Delegate> mWeakReference;

    public static SafetyHandler create(@NonNull Delegate delegate) {
        return new SafetyHandler(delegate);
    }

    private SafetyHandler(@NonNull Delegate delegate) {
        this.mWeakReference = new WeakReference<>(delegate);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        Delegate delegate = getDelegate();
        if (delegate == null) {
            clear();
            return;
        }
        delegate.handleMessage(msg);
    }

    public Delegate getDelegate() {
        return mWeakReference.get();
    }

    /**
     * 清空当前Handler队列所有消息并移除WeakReference持有的Delegate引用
     */
    public void clear() {
        removeCallbacksAndMessages(null);
        mWeakReference.clear();
    }

    public interface Delegate {
        void handleMessage(@NonNull Message msg);
    }

}
