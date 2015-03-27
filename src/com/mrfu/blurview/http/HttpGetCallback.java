package com.mrfu.blurview.http;

public interface HttpGetCallback {
	public void requestFinished(String url,String pathName);
	public void requestFailed(String url,String errorStr);
}
