package mrfu.blurstaggered;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mrfu.blurstaggered.lib.util.ImageBlurLoader;
import mrfu.blurstaggered.lib.util.ImageBlurLoader.ThreadRequest;
import mrfu.blurstaggered.lib.util.ImageLoader;
import mrfu.blurstaggered.lib.util.ImageLoader.AbsHttpCallback;
import mrfu.blurstaggered.lib.util.ImageLoader.HttpErrorResult;
import mrfu.blurstaggered.lib.util.ImageLoader.ImageRequest;
import mrfu.blurstaggered.lib.util.ImageViewEx;

;

public class ReleasedWaterFallAdapter extends BaseAdapter implements OnClickListener {

	protected List<DataModel> mData;
	protected LayoutInflater mInflater;
	protected Context mContext;
	public int currPageNo = 1;
	private float width;
	private DisplayMetrics dm;
	private GetNewRingListener getNewRingListener;

	public interface GetNewRingListener{
		void onGetNewRing(DataModel dataModel);
	}
	
	public ReleasedWaterFallAdapter(Context c, List<DataModel> dataList) {
		mContext = c;
		mInflater = LayoutInflater.from(c);
		mData = dataList;
		
		dm = new DisplayMetrics();
		((WindowManager) c.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(dm);
		
		width = (dm.widthPixels - 24 * dm.density) / 2;
	}
	
	public void setGetNewRingListener(GetNewRingListener getNewRingListener){
		this.getNewRingListener = getNewRingListener;
	}
	
	public void setList(List<DataModel> mData){
		this.mData = mData;
		this.notifyDataSetChanged();
	}
	
	public List<DataModel> getList(){
		return mData;
	}
	
	public void append(List<DataModel> list){
		this.mData.addAll(list);
		this.notifyDataSetChanged();
	}

	public void setUnLock(DataModel model){
		for (int i = 0; i < mData.size(); i++) {
			if (mData.get(i).getId().equals(model.getId())) {
				mData.get(i).setIsLocked("NO");
				this.notifyDataSetChanged();
				break;
			}
		}
	}

	@Override
	public int getCount() {
		if (mData !=null) {
		}
		return mData == null ? 0 : mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView != null) {
			holder = (ViewHolder)convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.square_list_item, null);
			holder.img = (ImageViewEx)convertView.findViewById(R.id.item_1);
			holder.resume = (TextView)convertView.findViewById(R.id.resume);
			holder.iv_lock =(ImageView)convertView.findViewById(R.id.iv_lock);
			holder.relate_last = convertView.findViewById(R.id.relate_last);
			holder.releat = convertView.findViewById(R.id.releat);
			convertView.setTag(holder);
		}

		LayoutParams params = (LayoutParams) holder.img.getLayoutParams();
		params.height = (int) (width * 250 / 250);
		holder.img.setVisibility(View.VISIBLE);
		holder.img.setLayoutParams(params);
		holder.relate_last.setLayoutParams(params);

		LayoutParams params_lock = (LayoutParams) holder.iv_lock.getLayoutParams();
		params_lock.width = (int) (width / 3);
		params_lock.height = (int) (width / 3);
		holder.iv_lock.setLayoutParams(params_lock);
		
		final DataModel item = mData.get(position);
		holder.releat.setOnClickListener(this);
		if ("NO".equalsIgnoreCase(item.getIsLocked())) {//unlock
			holder.relate_last.setVisibility(View.GONE);
			holder.img.setTag(R.id.item_1, item);
			holder.img.loadImage(item.getImageUrl());
//			holder.img.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					OpenDetail(item.getId());
//				}
//			});
//			holder.resume.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					OpenDetail(item.getId());
//				}
//			});
		}else {//lock
			holder.relate_last.setVisibility(View.VISIBLE);
			holder.relate_last.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					new UnLockDialog(mContext, 1, item, getNewRingListener, mUserProfileItem).show();
					new AlertDialog.Builder(mContext).setTitle(mContext.getResources().getString(R.string.tips)).setMessage(mContext.getResources().getString(R.string.need_unlock))
					.setCancelable(true)
					.setNegativeButton(mContext.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							getNewRingListener.onGetNewRing(item);
						}
					}).create().show();
				}
			});
			holder.resume.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					new UnLockDialog(mContext, 1, item, getNewRingListener, mUserProfileItem).show();
					new AlertDialog.Builder(mContext).setTitle(mContext.getResources().getString(R.string.tips)).setMessage(mContext.getResources().getString(R.string.need_unlock))
					.setCancelable(true)
					.setNegativeButton(mContext.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							getNewRingListener.onGetNewRing(item);
						}
					}).create().show();
				}
			});
			ImageRequest imageRequest = new ImageRequest(item.getImageUrl(), holder.img);
			ImageLoader.get().loadImageAsync(imageRequest, mAbsHttpCallback);
		}
		holder.resume.setVisibility(View.GONE);
		if (TextUtils.isEmpty(item.getComment())) {
			holder.resume.setVisibility(View.GONE);
		}else {
			holder.resume.setVisibility(View.VISIBLE);
			holder.resume.setText(item.getComment());
		}
		return convertView;
	}
	
	private class ViewHolder {
		ImageViewEx img;
		ImageView iv_lock;
		TextView resume;
		View relate_last;
		View releat;
	}
	
//	private void OpenDetail(String ringId){
//	}
	AbsHttpCallback mAbsHttpCallback = new AbsHttpCallback() {

		@Override
		public void onSuccess(final ImageRequest request, final Bitmap bitmap) {
			((Activity)mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					final ImageView iv = (ImageView) request.cookie;
					ThreadRequest threadRequest = new ThreadRequest("blur_"+request.url, bitmap);
					ImageBlurLoader.get().loadBlur(mContext, threadRequest, new ImageBlurLoader.DealBlurListener() {
						@Override
						public void onGetBlur(String blur_Url, final Bitmap bitmap) {
							((Activity)mContext).runOnUiThread(new Runnable() {
								@Override
								public void run() {
									iv.setImageBitmap(bitmap);
								}
							});
						}
					});
				}
			});
		}

		@Override
		public void onFailure(ImageRequest request, HttpErrorResult error) {
		}
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.releat:
			//Nothing to do here , but be sure to write the code in here , otherwise there will be displacement cascade problem
			//这里什么也不要做，但是代码一定要写在这里，不然瀑布流会有很蛋碎的位移问题
			break;
		default:
			break;
		}
	}
}
