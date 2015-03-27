package com.mrfu.blurview.http;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.mrfu.blurview.util.FileStore;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;


public class HttpRequestGet implements Runnable {
	private static final int CONNECT_TRY_TIME = 1;

	private String urlStr;
	private HttpGetCallback callback;
	private static Handler handler;
	private int totalLen;
	private String pathName = "";

	public HttpRequestGet(String url, HttpGetCallback callback) {
		this.urlStr = url;
		this.callback = callback;
		if(handler == null){
			handler = new Handler(Looper.getMainLooper());
		}
	}
	
	protected String getFilePath(){
		return FileStore.createNewCacheFile(urlStr, true);
	}

	public void run() {
		boolean isProgress = false;
		if (callback instanceof HttpGetProgressCallback) {
			isProgress = true;
		}
		HttpURLConnection conn = null;
		InputStream is = null;
		OutputStream os = null;
		boolean isSuccess = false;
		boolean isConnectTimeout = false;
		for (int i = 0; i < CONNECT_TRY_TIME; i++) {
			pathName = "";
			try {
				URL url = new URL(urlStr);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Connection", "Keep-Alive");
				if (Build.VERSION.SDK_INT > 13) {
//					conn.setRequestProperty("Connection", "close");
//					conn.setRequestProperty("Connection", "Keep-Alive");
				}
				conn.setAllowUserInteraction(false);
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.connect();
				int respCode = conn.getResponseCode();
				if (respCode == HttpURLConnection.HTTP_OK
						|| respCode == HttpURLConnection.HTTP_PARTIAL) {
					is = conn.getInputStream();
					final int fileSize = conn.getContentLength();
					pathName = getFilePath();
					os = new FileOutputStream(pathName);
					byte[] data = new byte[4096];
					int readLen = 0;
					totalLen = 0;
					int notifySize = 0;
					while ((readLen = is.read(data)) != -1) {
						os.write(data, 0, readLen);
						os.flush();
						totalLen += readLen;
						notifySize += readLen;
						if (isProgress && callback != null && notifySize >= fileSize/100) {
							notifySize = 0;
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									((HttpGetProgressCallback) callback)
									.progressPublish(urlStr, totalLen * 100
											/ fileSize);
								}
							});
						}
					}
					
					if(totalLen != fileSize) {
						return ;
					}
					
					String newPath = pathName.replace(".tmp", "");
					File newfile = new File(newPath);
					File oldfile = new File(pathName);
					oldfile.renameTo(newfile);
					pathName = newPath;
					
					if (isProgress && callback != null) {
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								((HttpGetProgressCallback) callback)
								.progressPublish(urlStr, 100);
							}
						});
					}
					if (callback != null) {
						isSuccess = true;
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								callback.requestFinished(urlStr, pathName);
							}
						});
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(e instanceof SocketTimeoutException){
					isConnectTimeout = true;
				}
			} finally {
				close(is, os, conn);
				if (isSuccess) {
					break;
				}else{
					if(!TextUtils.isEmpty(pathName)){
						File file = new File(pathName);
						if(file.exists()){
							file.delete();
						}
					}
				}
			}
		}
		if(!isSuccess && callback != null){
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					callback.requestFailed(urlStr, "request fail");
				}
			});
		}
		if(isConnectTimeout){
			//TODO
		}
	}
	
	private void close(InputStream in, OutputStream out, HttpURLConnection conn) {
		try {
			if (in != null) {
				in.close();
				in = null;
			}

		} catch (Exception e) {

		}
		try {
			if (out != null) {
				out.close();
				out = null;
			}
		} catch (Exception e) {

		}
		try {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		} catch (Exception e) {

		}
	}
}
