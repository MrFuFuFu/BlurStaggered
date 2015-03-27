package com.mrfu.blurview.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.mrfu.blurview.R;
import com.mrfu.blurview.util.ImageLoader.AbsHttpCallback;
import com.mrfu.blurview.util.ImageLoader.HttpErrorResult;
import com.mrfu.blurview.util.ImageLoader.ImageRequest;

public class ImageViewEx extends ImageView {
	@SuppressWarnings("unused")
	private OnLoadImageListener listener;
	private Bitmap defaultImg;
	private Drawable defaultDrawable;
	private static Drawable defaultImage;
	private static Handler handler;
	
	private String mUrl;

	public static interface OnLoadImageListener {
		public void onLoadImage(boolean isSuccess);
	}

	public ImageViewEx(Context context) {
		super(context);
		init();
	}

	public ImageViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ImageViewEx(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		handler = new Handler();
		if (defaultImage == null) {
			defaultImage = getResources().getDrawable(R.drawable.imageview_bg);
		}
	}
	
	@Override
	public void setImageResource(int resId) {
		mUrl = "";
		super.setImageResource(resId);
	}

	public void loadImage(String url, OnLoadImageListener listener) {
		this.listener = listener;
		loadImage(url);
	}
	
	public void loadImage(String url, Bitmap defaultImage) {
		this.defaultImg = defaultImage;
		loadImage(url);
	}

	public void loadImage(String url, Drawable defaultImage) {
		this.defaultDrawable = defaultImage;
		loadImage(url);
	}
	
	public static Bitmap getBitmap(String url) {
		return ImageLoader.get().queryBitmapInMemory(url);
	}
	
	public void loadImage(String url) {
		mUrl = url;
		setImageBitmap(null);
		
		if (defaultDrawable != null) {
			setImageDrawable(defaultDrawable);
		} else if (defaultImg != null) {
			setImageBitmap(defaultImg);
		} else {
			setImageDrawable(defaultImage);
		}
		
		ImageLoader.ImageRequest request = new ImageLoader.ImageRequest(url, this);
		ImageLoader.get().loadImageAsync(request, mHttpCallback);
	}
	
	AbsHttpCallback mHttpCallback = new AbsHttpCallback() {
		
		@Override
		public void onSuccess(final ImageRequest request, final Bitmap bitmap) {
			if(!request.url.equals(mUrl)) {
				return ;
			}
			
			final ImageView iv = (ImageView)request.cookie;
			if(request.inMemory) {
				iv.setImageBitmap(bitmap);
				return ;
			}
			
			handler.post(new Runnable() {
				@Override
				public void run() {
					if(request.url.equals(mUrl)) {
						setImageWithAnimation(iv, bitmap);
					}
				}
			});
		}
		
		@Override
		public void onFailure(ImageRequest request, HttpErrorResult error) {
			
		}

	};

	protected void setImageWithAnimation(ImageView iv, Bitmap bitmap) {
		Drawable[] layers = new Drawable[2];
		Drawable drawable0 = getDrawable();
		if (drawable0 == null) {
			drawable0 = getContext().getResources()
					.getDrawable(R.drawable.imageview_bg);
		}
		@SuppressWarnings("deprecation")
		Drawable drawable1 = new BitmapDrawable(bitmap);
		layers[0] = drawable0;
		layers[1] = drawable1;
		TransitionDrawable transitionDrawable = new TransitionDrawable(
				layers);
		transitionDrawable.setCrossFadeEnabled(true);
		iv.setImageDrawable(transitionDrawable);
		transitionDrawable.startTransition(300);
	}
}
