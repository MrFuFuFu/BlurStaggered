package com.mrfu.blurview.http;

public interface HttpGetProgressCallback extends HttpGetCallback{
	public void progressPublish(String url,int progress);
}
