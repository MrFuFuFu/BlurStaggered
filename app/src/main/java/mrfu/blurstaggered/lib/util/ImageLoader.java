package mrfu.blurstaggered.lib.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import mrfu.blurstaggered.lib.http.HttpGetProgressCallback;
import mrfu.blurstaggered.lib.http.HttpRequestGet;

public class ImageLoader {
	private static final int MAX_BLOCK_QUEUE_SIZE = 30;
	
	private ThreadPoolExecutor mExecutorPool;
	private Hashtable<String, Set<ItemTask>> mTaskQueue;
	private LruMemoryCache<String, Bitmap> mBitmapCache;
	
	private static ImageLoader sImageLoader;
	
	/**
	 * get instance
	 * @return
	 */
	public static ImageLoader get() {
		if(sImageLoader == null) {
			sImageLoader = new ImageLoader();
		}
		
		return sImageLoader;
	}
	
	private ImageLoader() {
		mTaskQueue = new Hashtable<String, Set<ItemTask>>();
		mExecutorPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
		
		long maxMemory = Runtime.getRuntime().maxMemory();
		int cacheSize = (int) (maxMemory / 16); 
		mBitmapCache = new LruMemoryCache<String, Bitmap>(cacheSize) {
			/**
			 * Measure item size in bytes rather than units which is more
			 * practical for a bitmap cache
			 */
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return getBitmapSize(bitmap);
			}
		};
	}
	
	private int getBitmapSize(Bitmap bitmap) {
		if(bitmap != null){
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
		return 0;
	}
	
	public Bitmap queryBitmapInMemory(String url) {
		if(TextUtils.isEmpty(url)) { 
			return null;
		}
		
		Bitmap bmp = mBitmapCache.get(url);
		if(bmp == null) {
			return null;
		} else if(bmp.isRecycled()) {
			mBitmapCache.remove(url);
			return null;
		} else {
			return bmp;
		}
	}
	
	public void saveBitmapInMemory(String url, Bitmap bitmap){
		mBitmapCache.put(url, bitmap);
	}

	/**
	 * load image async
	 * @param request
	 * @param callback
	 */
	public void loadImageAsync(ImageRequest request, AbsHttpCallback callback) {
		loadImage(request, callback);
	}
	
	public void loadImagePreAsync(ImageRequest request) {
		loadImage(request, new AbsHttpCallback() {
			@Override
			public void onSuccess(ImageRequest request, Bitmap bitmap) {
				
			}
			
			@Override
			public void onFailure(ImageRequest request, HttpErrorResult error) {
				
			}
		});
	}
	
	/**
	 * load image
	 * @param request
	 * @param callback
	 */
	private void loadImage(ImageRequest request, AbsHttpCallback callback) {
		if(callback == null) {
			return ;
		}
		if(TextUtils.isEmpty(request.url)) {
			callback.onFailure(request, HttpErrorResult.URL_INVALID);
			return ;
		}
		
		if (request.url.startsWith("http")) { 
			Bitmap bitmap = mBitmapCache.get(request.url);
			if (bitmap != null) {
				request.inMemory = true;
				callback.onSuccess(request, bitmap);
				return ;
			}
		}
		
		request.inMemory = false;
		if(mTaskQueue.containsKey(request.url)){
			Set<ItemTask> set = mTaskQueue.get(request.url);
			set.add(new ItemTask(request, callback));
		}else{
			Set<ItemTask> set = new HashSet<ItemTask>();
			set.add(new ItemTask(request, callback));
			mTaskQueue.put(request.url, set);
			mExecutorPool.execute(new LocalTask(request, callback));
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
					mTaskQueue.remove(task.httpRequest.url);
				}
			}
			queue.clear();
			queue.addAll(list);
		}
	}
	
	private class ItemTask {
		ImageRequest httpRequest;
		AbsHttpCallback callback;
		
		ItemTask(ImageRequest request, AbsHttpCallback callback) {
			this.httpRequest = request;
			this.callback = callback;
		}
	}
	
	public static class ImageRequest {
		public String url;
		public Object cookie;
		public Object message;
		public long time;
		public boolean inMemory;
		
		public ImageRequest(String url) {
			this.url = url;
		}
		
		public ImageRequest(String url, Object cookie) {
			this.url = url;
			this.cookie = cookie;
		}
	};
	
	public static abstract class AbsHttpCallback {
		
		public void onStart(ImageRequest request) {} 
		
		public abstract void onSuccess(ImageRequest request, Bitmap bitmap);
		
		public abstract void onFailure(ImageRequest request, HttpErrorResult error);
		
		public void onProgress(ImageRequest request, int progress) {}
	
	};
	
	public static enum HttpErrorResult {
		URL_INVALID, HTTP_GET_FAIL
	};
	
	/**
	 * local task
	 *
	 */
	private class LocalTask implements Runnable, HttpGetProgressCallback {
		private ImageRequest httpRequest;
		private AbsHttpCallback callback;
		
		public LocalTask(ImageRequest request, AbsHttpCallback callback) {
			this.httpRequest = request;
			this.callback = callback;
		}
		
		@Override
		public void run() {
			String pathName = FileStore.cachePathForKey(httpRequest.url);
			if (!TextUtils.isEmpty(pathName)) {
				Bitmap  bitmap = decodeBitmap(pathName);
				if(bitmap != null) {
					notifySuccess(httpRequest, callback, bitmap);
					return ;
				} else {
					new File(pathName).delete();
				}
			}
			
			callback.onStart(httpRequest);
			HttpRequestGet httpGet = new HttpRequestGet(httpRequest.url, this);
			httpGet.run();
		}

		@Override
		public void requestFinished(String url, String pathName) {
			Bitmap bitmap = decodeBitmap(pathName);
			if(bitmap != null) {
				notifySuccess(httpRequest, callback, bitmap);
			} 
		}

		@Override
		public void requestFailed(String url, String errorStr) {
			notifyFailure(httpRequest, callback, HttpErrorResult.HTTP_GET_FAIL);
		}

		@Override
		public void progressPublish(String url, int progress) {
			callback.onProgress(httpRequest, progress);
		}

	};
	
	/**
	 * notify success tasks
	 * @param httpRequest
	 * @param callback
	 */
	private void notifySuccess(ImageRequest httpRequest, AbsHttpCallback callback, Bitmap bitmap) {
		mBitmapCache.put(httpRequest.url, bitmap);
		Set<ItemTask> set = mTaskQueue.get(httpRequest.url);
		if(set != null){
			for(ItemTask task : set){
				task.callback.onSuccess(task.httpRequest, bitmap);
			}
			set.clear();
			mTaskQueue.remove(httpRequest.url);
		}
	}
	
	private void notifyFailure(ImageRequest httpRequest, AbsHttpCallback callback, HttpErrorResult error) {
		Set<ItemTask> set = mTaskQueue.get(httpRequest.url);
		if(set != null){
			for(ItemTask task : set){
				task.callback.onFailure(task.httpRequest, HttpErrorResult.HTTP_GET_FAIL);
			}
			set.clear();
			mTaskQueue.remove(httpRequest.url);
		}
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
			oe.printStackTrace();
			return null;
		}
	}
	
}
