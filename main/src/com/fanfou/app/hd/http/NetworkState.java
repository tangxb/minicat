package com.fanfou.app.hd.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.fanfou.app.hd.App;
import com.fanfou.app.hd.App.ApnType;

/**
 * 网络连接状态判断，是否连接，接入点，WIFI等，
 * 
 * @author mcxiaoke
 * @version 1.0 2011.01.29
 * @version 1.1 2011.05.02
 * @version 1.2 2011.05.02
 * @version 1.3 2011.11.24
 * 
 */
public class NetworkState {
	/**
	 * 字符串标志，是否为WAP接入点
	 */
	private static final String TAG = NetworkState.class.getSimpleName();
	private ConnectivityManager cm;
	private ApnType apnType = ApnType.NET;

	/**
	 * 记录调试信息
	 * 
	 * @param message
	 *            调试信息内容
	 */
	private void log(String message) {
		Log.d(TAG, message);
	}

	/**
	 * New网络连接状态对象
	 * 
	 * @param c
	 */
	public NetworkState(Context context) {
		this.cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		initState();
	}

	private void initState() {
		try {
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isConnectedOrConnecting()) {
				if (App.DEBUG) {
					log(info.toString());
				}
//				App.noConnection = false;
				if (info.getType() == ConnectivityManager.TYPE_WIFI) {
					apnType = ApnType.WIFI;
				} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
					String apnTypeName = info.getExtraInfo();
					if (!TextUtils.isEmpty(apnTypeName)) {
						if (apnTypeName.equals("3gnet")) {
							apnType = ApnType.HSDPA;
						} else if (apnTypeName.contains("wap")) {
							apnType = ApnType.WAP;
						}
					}
				}
			} else {
//				App.noConnection = true;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 判断接入点类型
	 * 
	 * @return 返回接入点类型
	 */
	public ApnType getApnType() {
		return apnType;
	}

	/**
	 * 判断是否WIFI接入
	 * 
	 * @return 是否WIFI
	 */
	public boolean isWIFI() {
		boolean result = false;
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI
				&& info.isConnected()) {
			result = true;
		}
		return result;
	}

	public boolean is3G() {
		boolean result = false;
		try {
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				log(info.toString());
				if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
					String apnTypeName = info.getExtraInfo();
					if (!TextUtils.isEmpty(apnTypeName)) {
						if (apnTypeName.equals("3gnet")) {
							result = true;
						}
					}
				}
			}
		} catch (Exception e) {
			if (App.DEBUG) {
				e.printStackTrace();
			}
		}
		return result;
	}

}