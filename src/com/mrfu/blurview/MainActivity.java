package com.mrfu.blurview;


import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.mrfu.blurview.ReleasedWaterFallAdapter.GetNewRingListener;
import com.mrfu.blurview.pulltorefresh.PullRefreshStaggeredListView;
import com.mrfu.blurview.pulltorefresh.PullRefreshStaggeredListener;
import com.mrfu.blurview.staggered.StaggeredGridView;

/***
 * @author MrFu
 */
public class MainActivity extends Activity implements PullRefreshStaggeredListener, GetNewRingListener {
	private ReleasedWaterFallAdapter adapter;
	private PullRefreshStaggeredListView mListView;
	private StaggeredGridView mGridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mListView = (PullRefreshStaggeredListView)findViewById(R.id.list);
		mListView.setPullRefreshListener(this);
		mGridView = mListView.getMutiColumnListView();
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ReleasedWaterFallAdapter(this, new ArrayList<DataModel>());
		mGridView.setAdapter(adapter);
		mListView.setRefreshing();
		adapter.setGetNewRingListener(this);
	}
	
	@Override
	public void onPullDownRefresh(PullRefreshStaggeredListView view) {
		//TODO http post request page number 1
		new SampleTask(0).execute();
	}
	boolean isFirst = true;
	@Override
	public void onPullUpRefresh(PullRefreshStaggeredListView view) {
		//TODO http post request page number start 2
		if (isFirst) {
			isFirst = false;
			new SampleTask(1).execute();
		}else {
			new SampleTask(2).execute();
		}
	}
	@Override
	public void onScrollY(PullRefreshStaggeredListView view, int scrollY) {
	}

	@Override
	public void onGetNewRing(DataModel dataModel) {
		mGridView.isUnLock = true;
		adapter.setUnLock(dataModel);
		Toast.makeText(this, getResources().getString(R.string.unlock_success), Toast.LENGTH_SHORT).show();
	}
	
	private class SampleTask extends AsyncTask<Void, Void, Void>{
		int state;//0:pulldown; 1:pullup; 2:no more
		public SampleTask(int b) {
			state = b;
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (state == 2) {
				return null;
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mListView.reset();
			//TODO data from server. this just for test
			switch (state) {
			case 0://pull down
				adapter.setList(TestData.getPullDownDataModels());
				break;
			case 1://pull up
				adapter.append(TestData.getPullUpDataModels());
				break;
			case 2://no more
				Toast.makeText(MainActivity.this, getResources().getString(R.string.nomore), Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
			super.onPostExecute(result);
		}
	}
}
