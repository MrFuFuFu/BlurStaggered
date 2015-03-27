package com.mrfu.blurview;

import android.app.Application;

/**
 * @author Mr.傅
 * 2015-3-26 下午3:22:56
 */
public class BlurViewApplication extends Application {
	private static BlurViewApplication sInstance;
	public static BlurViewApplication getInstance() {
		return sInstance;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
	}

}
