package com.mrfu.blurview.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.mrfu.blurview.blur.Blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

/**
 * @author Mr.傅
 * 2014-12-22 下午3:41:52
 */
public class ImageBlurLoader {
	private static final int MAX_BLOCK_QUEUE_SIZE = 5;
	private ThreadPoolExecutor mExecutorPool;
	private Hashtable<String, Set<ItemTask>> mTaskQueue;
	private static ImageBlurLoader sImageBlurLoader;
//	private DealBlurListener listener;
//	private ThreadRequest request;
//	private Context mContext;
	/**
	 * get instance
	 * @return
	 */
	public static ImageBlurLoader get() {
		if(sImageBlurLoader == null) {
			sImageBlurLoader = new ImageBlurLoader();
		}
		
		return sImageBlurLoader;
	}
	
	private ImageBlurLoader(){
		mTaskQueue = new Hashtable<String, Set<ItemTask>>();
		mExecutorPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
	}
	
	public void loadBlur(Context context, ThreadRequest request, DealBlurListener listener){
//		this.listener = listener;
//		this.request = request;
//		this.mContext = context;
		if (listener == null) {
			return;
		}
		if (TextUtils.isEmpty(request.blur_url)) {
			return;
		}
		if (mTaskQueue.containsKey(request.blur_url)) {
			Set<ItemTask> set = mTaskQueue.get(request.blur_url);
			set.add(new ItemTask(context, request, listener));
		}else {
			Set<ItemTask> set = new HashSet<ItemTask>();
			set.add(new ItemTask(context, request, listener));
			mTaskQueue.put(request.blur_url, set);
			mExecutorPool.execute(new LocalTask(context, request));
		}
		balanceTasks();
		
	}
	
	/**
	 * balance tasks
	 */
	private void balanceTasks() {
		BlockingQueue<Runnable> queue = mExecutorPool.getQueue();
		if (queue == null) { 
			return ;
		}
			
		int poolSize = queue.size();
		if (poolSize > MAX_BLOCK_QUEUE_SIZE) { //保留队尾MAX_BLOCK_QUEUE_SIZE个队列其他清理掉
			int keep = poolSize - MAX_BLOCK_QUEUE_SIZE;
			List<Runnable> list = new ArrayList<Runnable>();
			int i = 0;
			while (queue.size() > 0) {
				i++;
				Runnable r = queue.remove();
				if (i >= keep) {
					list.add(r);
				} else {
					LocalTask task = (LocalTask) r;
					mTaskQueue.remove(task.request.blur_url);
				}
			}
			queue.clear();
			queue.addAll(list);
		}
	}
	
	private class LocalTask implements Runnable{
		private ThreadRequest request;
//		private DealBlurListener listener;
		private Context context;

		public LocalTask(Context context, ThreadRequest request) {
			this.context = context;
			this.request = request;
//			this.listener = listener;
		}



		@Override
		public void run() {
			Bitmap bitmap = getPrivateBitmap(context, request.bitmap, request.blur_url);
//			listener.onGetBlur(request.blur_url, bitmap);
			Set<ItemTask> set = mTaskQueue.get(request.blur_url);
			if (set != null) {
				for(ItemTask task : set){
					task.listener.onGetBlur(request.blur_url, bitmap);
				}
				set.clear();
				mTaskQueue.remove(request.blur_url);
			}
		}
		
	}
	
	/**
	 * Get Gaussian blur pictures, start taking the cache , no longer removed from the file, then just do not have a Gaussian blur , coexistence and file cache
	 * 获取高斯模糊图片，先从缓存中取，没有再从文件中取，再没有才做高斯模糊，并存到缓存和文件中
	 * 
	 * **/
	private Bitmap getPrivateBitmap(Context context, Bitmap bitmap, String blur_url){
		Bitmap memeryBitmap = ImageLoader.get().queryBitmapInMemory(blur_url);
		if (memeryBitmap != null) {
			return memeryBitmap;
		}
		
		//文件路径 file path
		String filePathName = FileStore.cachePathForKeyBlur(blur_url);
		//文件图片 file picture
		Bitmap fileBitmap = null;
		if (!TextUtils.isEmpty(filePathName)) {
			fileBitmap = decodeBitmap(filePathName);
			if(fileBitmap == null) {
				new File(filePathName).delete();
			}else {
				return fileBitmap;
			}
		}

		//模糊处理 Gaussian blur operation
		Bitmap blurBitmap = Blur.fastblur(bitmap, 15);
		
		//保存到缓存  save to cache
		ImageLoader.get().saveBitmapInMemory(blur_url, blurBitmap);
		String filePath = FileStore.createNewCacheFile(blur_url, true);
		//写入到文件  write to file
		FileStore.writemBitmapForBlur(filePath, blurBitmap);
		return blurBitmap;
	}

	private Bitmap decodeBitmap(String pathName) {
		Bitmap bitmap = null;
		try{
			bitmap = BitmapFactory.decodeFile(pathName);
		}catch(OutOfMemoryError oe){
			System.gc();
			bitmap = scaleBitmap(pathName);
		}
		return bitmap;
	}
	private Bitmap scaleBitmap(String pathName) {
		try{
			BitmapFactory.Options opts = new BitmapFactory.Options();  
			opts.inSampleSize = 4;  
			return BitmapFactory.decodeFile(pathName, opts);  
		}catch(OutOfMemoryError oe){
			BitmapFactory.Options opts = new BitmapFactory.Options();  
			opts.inSampleSize = 16;  
			return BitmapFactory.decodeFile(pathName, opts);  
		}
	}
	
	public static class ThreadRequest{
		public String blur_url;
		public Bitmap bitmap;
		public ThreadRequest(String blur_url, Bitmap bitmap) {
			this.blur_url = blur_url;
			this.bitmap = bitmap;
		}
		
	}
	
	@SuppressWarnings("unused")
	private class ItemTask {
		Context context;
		ThreadRequest request;
		DealBlurListener listener;
		
		ItemTask(Context context, ThreadRequest request, DealBlurListener listener) {
			this.request = request;
			this.listener = listener;
			this.context = context;
		}
	}
	
	public interface DealBlurListener{
		void onGetBlur(String blur_Url, Bitmap bitmap);
	}
}
