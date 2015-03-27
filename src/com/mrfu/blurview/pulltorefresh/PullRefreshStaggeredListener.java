package com.mrfu.blurview.pulltorefresh;


public interface PullRefreshStaggeredListener {
	
	public void onPullDownRefresh(PullRefreshStaggeredListView view);
	
	public void onPullUpRefresh(PullRefreshStaggeredListView view);
	
	public void onScrollY(PullRefreshStaggeredListView view,int scrollY);

}
