package com.davemorrissey.labs.subscaleview.test;

import android.app.Application;
import android.os.StrictMode;

public class SsivSampleApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		StrictMode.setThreadPolicy(
			new StrictMode.ThreadPolicy.Builder()
				.detectAll()
				.penaltyLog()
				.build()
		);
		StrictMode.setVmPolicy(
			new StrictMode.VmPolicy.Builder()
				.detectAll()
				.penaltyLog()
				.build()
		);
	}
}
