package com.zuku.smartbill.zukufiber;

import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.maps.GoogleMap;

public abstract class OnCameraReallyIdleListener implements GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {
	private final Handler mHandler = new Handler(Looper.getMainLooper());
	private final long mDelay;
	private Runnable mCallback;

	public OnCameraReallyIdleListener(final long delay) {
		mDelay = delay;
	}

	@Override
	public final void onCameraMove() {
		removeCallbacks();
	}

	@Override
	public final void onCameraIdle() {
		removeCallbacks();
		mCallback = () -> {
			onCameraReallyIdle();
			mCallback = null;
		};
		mHandler.postDelayed(mCallback, mDelay);
	}

	public final void removeCallbacks() {
		if(mCallback != null) {
			mHandler.removeCallbacks(mCallback);
			mCallback = null;
		}
	}

	abstract public void onCameraReallyIdle();
}