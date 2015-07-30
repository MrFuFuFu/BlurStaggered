package mrfu.blurstaggered;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mrfu.blurstaggered.lib.view.PullRefreshStaggeredListView;
import mrfu.blurstaggered.lib.view.PullRefreshStaggeredListener;
import mrfu.blurstaggered.lib.view.StaggeredGridView;


public class MainActivity extends Activity implements PullRefreshStaggeredListener, ReleasedWaterFallAdapter.GetNewRingListener {

    private ReleasedWaterFallAdapter adapter;
    private PullRefreshStaggeredListView mListView;
    private StaggeredGridView mGridView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTitleBar();
        mListView = (PullRefreshStaggeredListView)findViewById(R.id.list);
        mListView.setPullRefreshListener(this);
        mGridView = mListView.getMutiColumnListView();
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ReleasedWaterFallAdapter(this, new ArrayList<DataModel>());
        mGridView.setAdapter(adapter);
        mListView.setRefreshing();
        adapter.setGetNewRingListener(this);
    }

    private void initTitleBar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Blur Staggered GridView");
        toolbar.setLogo(R.mipmap.logo);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
               @Override
               public boolean onMenuItemClick(MenuItem item) {
                   Uri uri = Uri.parse("https://github.com/MrFuFuFu/BlurStaggered");
                   Intent i = new Intent(Intent.ACTION_VIEW, uri);
                   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(i);
                   return false;
               }
           }
        );
    }

    @Override
    public void onPullDownRefresh(PullRefreshStaggeredListView view) {
        //TODO http post request page number 1
        isFirst = true;
        isLast = false;
        new SampleTask(0).execute();
    }
    boolean isFirst = true;
    boolean isLast = false;
    @Override
    public void onPullUpRefresh(PullRefreshStaggeredListView view) {
        //TODO http post request page number start 2
        if (isFirst) {
            isFirst = false;
            new SampleTask(1).execute();
        }else if(!isLast){
            new SampleTask(2).execute();
        }else {
            mListView.reset();
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

    private class SampleTask extends AsyncTask<Void, Void, Void> {
        int state;//0:pulldown; 1:pullup; 2:no more
        public SampleTask(int b) {
            state = b;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (isFromGanHuo){
                dataModelList = TestData.getTestGanhuoData();
            }
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
                    if (isFromGanHuo){
                        adapter.setList(dataModelList);
                    }else {
                        adapter.setList(TestData.getPullDownDataModels());
                    }
                    break;
                case 1://pull up
                    adapter.append(TestData.getPullUpDataModels());
                    break;
                case 2://no more
                    isLast = true;
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.nomore), Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            super.onPostExecute(result);
        }
    }

    boolean isFromGanHuo = false;

    private List<DataModel> dataModelList;
}
