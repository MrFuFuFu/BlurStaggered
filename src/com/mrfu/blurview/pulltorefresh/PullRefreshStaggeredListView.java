package com.mrfu.blurview.pulltorefresh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrfu.blurview.R;
import com.mrfu.blurview.staggered.StaggeredGridView;
import com.mrfu.blurview.staggered.StaggeredGridView.OnScrollListener;

public class PullRefreshStaggeredListView extends FrameLayout implements OnScrollListener{
	
	protected static final int PULL_TO_REFRESH = 0x0;
	protected static final int RELEASE_TO_REFRESH = 0x1;
	protected static final int REFRESHING = 0x2;
	public static int HEADER_VIEW_HEIGHT;
	
	protected int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	protected StaggeredGridView absListView;
	protected View header;
	protected View footer;
	protected ImageView arrow;
	protected TextView textView;
	protected ProgressBarCircularIndeterminate progressBar;
	public int state;
	protected boolean enablePullUpRefresh;//是否支持滚动到最下面条刷新
	protected boolean enablePullDownRefresh;

	private PullRefreshStaggeredListener pullRefreshListener;
	private OnScrollListener listener;
	private RelativeLayout content;
	
	public PullRefreshStaggeredListView(Context context) {
        super(context);
    }
	
	public void setOnScrollListener(OnScrollListener listener){
		this.listener = listener;
	}

    public PullRefreshStaggeredListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }
    
    public PullRefreshStaggeredListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(attrs);
	}

	public void setPullRefreshListener(PullRefreshStaggeredListener pullRefreshListener){
    	this.pullRefreshListener = pullRefreshListener;
    }
    
    public void setEnablePullUpRefresh(boolean enablePullUpRefresh) {
		this.enablePullUpRefresh = enablePullUpRefresh;
	}
    
    public void setEnablePullDownRefresh(boolean enablePullDownRefresh){
    	this.enablePullDownRefresh = enablePullDownRefresh;
    }
    
    public void setRefreshing(){
    	state = RELEASE_TO_REFRESH;
    	refreshing();
    }
    
    public void setFooterBackgroundColor(int color){
    	if(footer != null){
    		footer.setBackgroundColor(color);
    	}
    }
    
    public boolean isDraging(){
    	return state != PULL_TO_REFRESH;
    }
    
    public boolean isRefreshing(){
    	return state == REFRESHING ? true : false;
    }
    
	private int px2dip(float pxValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public int dip2px(float dpValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
    
    protected void initViews(AttributeSet attrs){
    	enablePullUpRefresh = true;
    	enablePullDownRefresh = true;
    	
    	absListView = createMutiColumnListView(attrs);
//    	if(absListView != null){
//    		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
//    		addView(absListView, params);
//    	}
    	absListView.setOnScrollListener(this);
    	
    	footer = View.inflate(getContext(), R.layout.footer, null);
//    	if(footer != null){
//    		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, 100);
//    		addView(footer, params);
//    		footer.setVisibility(View.GONE);
//    	}
    	footer.setVisibility(View.GONE);
    	footer.setId(1);
    	FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    	content = new RelativeLayout(getContext());
    	content.setLayoutParams(fl);
    	RelativeLayout.LayoutParams footerParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, dip2px(40));
    	footerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
    	content.addView(footer,footerParams);
    	RelativeLayout.LayoutParams listparams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    	listparams.addRule(RelativeLayout.ABOVE, 1);
    	content.addView(absListView, listparams);
    	addView(content, new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

    	
    	header = View.inflate(getContext(), R.layout.header, null);
    	if(header != null){
    		addView(header, new FrameLayout.LayoutParams(
    				LayoutParams.FILL_PARENT, 100));
        	if(header != null){
        		arrow = (ImageView)header.findViewById(R.id.pull_to_refresh_arrow);
        	}
        	measureView(header);
        	HEADER_VIEW_HEIGHT = header.getMeasuredHeight();
        	textView = (TextView)header.findViewById(R.id.text);
        	progressBar = (ProgressBarCircularIndeterminate)header.findViewById(R.id.progress);
    	}
    	
    	ViewConfiguration config = ViewConfiguration.get(getContext());
		mTouchSlop = config.getScaledTouchSlop();
		state = PULL_TO_REFRESH;
    }
    
    public StaggeredGridView getMutiColumnListView(){
    	return this.absListView;
    }
    
    protected void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}
    
    private void rotateArrow() {
    	if(arrow != null){
    		Drawable drawable = arrow.getDrawable();
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.save();
            canvas.rotate(180.0f, canvas.getWidth() / 2.0f, canvas.getHeight() / 2.0f);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            canvas.restore();
            arrow.setImageBitmap(bitmap);
    	}
    }
    
    @Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
    	if((isReadyForPullDown() || isReadyForPullUp()) && state != REFRESHING){
    		int action = event.getAction();
    		float x = event.getX();
    		float y = event.getY();
        	switch (action) {
    			case MotionEvent.ACTION_MOVE: {
    				final float yDiff = Math.abs(y - mLastMotionY);
    				final float xDiff = Math.abs(x - mLastMotionX);
    				if (yDiff > mTouchSlop && (yDiff > xDiff) && enablePullDownRefresh) {
    					if (y > mLastMotionY  && isReadyForPullDown()) {
    						mLastMotionY = y;
    						return true;
    					}
//    					else if (y < mLastMotionY && isReadyForPullUp()) {
//    						if(pullRefreshListener != null && state != REFRESHING && enablePullUpRefresh){
//    			    			state = REFRESHING;
//    			    			footer.setVisibility(View.VISIBLE);
//    			    			pullRefreshListener.onPullUpRefresh(this);
//    			    		}
//    					}
    				}
    				if (y < mLastMotionY && isReadyForPullUp()) { 
						if(pullRefreshListener != null && state != REFRESHING && enablePullUpRefresh){
			    			state = REFRESHING;
			    			footer.setVisibility(View.VISIBLE);
			    			pullRefreshListener.onPullUpRefresh(this);
			    		}
					}
    				break;
    			}
    			case MotionEvent.ACTION_DOWN: {
    				mLastMotionY = event.getY();
    				mLastMotionX = event.getX();
    				break;
    			}
        	}
    	}
    	return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE: {
				int yDiff = (int) (mLastMotionY - event.getY());
				scrollBy(0, yDiff*2/3);
				mLastMotionY = event.getY();
				int scrollY = getScrollY();
				if(pullRefreshListener != null){
					pullRefreshListener.onScrollY(this,scrollY);
				}
				if(isReadyForPullDown()){
					if(state == PULL_TO_REFRESH && scrollY < -HEADER_VIEW_HEIGHT){
						state = RELEASE_TO_REFRESH;
						arrow.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate));
						rotateArrow();
						textView.setText(R.string.release_to_refresh);
					}else if(state == RELEASE_TO_REFRESH && scrollY > -HEADER_VIEW_HEIGHT){
						state = PULL_TO_REFRESH;
						arrow.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate));
						rotateArrow();
						textView.setText(R.string.pull_to_refresh);
					}
				}
				return true;
			}
			case MotionEvent.ACTION_DOWN: {
				break;
			}
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP: {
				processState();
				break;
			}
		}
		return false;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
//		final int childCount = getChildCount();
//		int childTop = 0;
//		for (int i = 0; i < childCount; i++) {
//			final View childView = getChildAt(i);
//			if (childView.getVisibility() != View.GONE) {
//				int childHeight = childView.getMeasuredHeight();
//				if(childView == footer){
//					childTop = childTop - childHeight;
//				}
//				if(childView == header){
//					childTop = - HEADER_VIEW_HEIGHT;
//				}
//				childView.layout(0, childTop, childView.getMeasuredWidth(),
//						childTop+childHeight);
//				childTop += childHeight;
//			}
//		}
		
		final int childCount = getChildCount();
		int childTop = 0;
		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {				
				if(childView == header){
					childView.layout(0, - HEADER_VIEW_HEIGHT, childView.getMeasuredWidth(),childTop+childView.getMeasuredHeight());
				}else{
					childView.layout(0, 0, childView.getMeasuredWidth(),childView.getMeasuredHeight());
				}
			}
		}
	}
	
	private void processState(){
		switch(state){
			case PULL_TO_REFRESH:{
				scrollTo(0, 0);
				if(pullRefreshListener != null){
					pullRefreshListener.onScrollY(this,0);
				}
				break;
			}
			case RELEASE_TO_REFRESH:
			case REFRESHING:{
				refreshing();
				break;
			}
			default:
				break;
		}
	}
	
	private void refreshing(){
		scrollTo(0, - HEADER_VIEW_HEIGHT);
		if(pullRefreshListener != null){
			pullRefreshListener.onScrollY(this,- HEADER_VIEW_HEIGHT);
		}
		if(state == RELEASE_TO_REFRESH){
			state = REFRESHING;
			textView.setText(R.string.refreshing);
			progressBar.setVisibility(View.VISIBLE);
			arrow.setVisibility(View.GONE);
			invalidate();
			if(pullRefreshListener != null){
				pullRefreshListener.onPullDownRefresh(this);
			}
		}
	}
	
	public void reset(){
		resetHeader();
		resetFooter();
	}
	
	private void resetHeader(){
		if(state == REFRESHING){
			state = PULL_TO_REFRESH;
			arrow.setVisibility(View.VISIBLE);
			textView.setText(R.string.pull_to_refresh);
			progressBar.setVisibility(View.GONE);
			scrollTo(0, 0);
			if(pullRefreshListener != null){
				pullRefreshListener.onScrollY(this,0);
			}
		}
	}
	
	private void resetFooter(){
		state = PULL_TO_REFRESH;
		if(footer.getVisibility() == View.VISIBLE){
			footer.setVisibility(View.GONE);
			postInvalidate();
		}
	}
	
	protected boolean isReadyForPullDown(){
		if (absListView != null && absListView.getFirstVisiblePosition() == 0) {
			View firstVisibleChild = absListView.getChildAt(0);
			if (firstVisibleChild != null) {
				return firstVisibleChild.getTop() >= 0;
			}else{
				return true;
			}
		}
		return false;
	}

	protected boolean isReadyForPullUp(){
		if(absListView != null){
			int count = absListView.getCount();
//			Debug.i_MrFu("count = "+count+" && absListView.getLastVisiblePosition() = "+absListView.getLastVisiblePosition());
			if(absListView.getLastVisiblePosition() == count - 1){
				return true;
//				final int childIndex = absListView.getLastVisiblePosition() - absListView.getFirstVisiblePosition();
//				Debug.i_MrFu("childIndex="+childIndex);
//				View lastVisibleChild = absListView.getLastChild();
//				if (lastVisibleChild != null) {
//					Debug.i_MrFu("isReadyForPullUp = "+(lastVisibleChild.getBottom() <= absListView.getBottom()-absListView.getTop())
//							+ "  bottom="+lastVisibleChild.getBottom()
//							+ "  Bottom-top="+(absListView.getBottom()-absListView.getTop())
//							);
//					return lastVisibleChild.getBottom() <= absListView.getBottom()-absListView.getTop();
//				}
			}
		}
		return false;
	}
    
    public StaggeredGridView createMutiColumnListView(AttributeSet attrs){
    	return new StaggeredGridView(getContext(),attrs);
    }

	@Override
	public void onScrollStateChanged(ViewGroup view, int scrollState) {
		boolean up = isReadyForPullUp();
		if(up){
    		if(pullRefreshListener != null && state != REFRESHING && enablePullUpRefresh){
    			state = REFRESHING;
    			footer.setVisibility(View.VISIBLE);
    			pullRefreshListener.onPullUpRefresh(this);
    		}
    	}
    	if(listener != null){
    		listener.onScrollStateChanged(view, scrollState);
    	}
	}

	@Override
	public void onScroll(ViewGroup view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if(listener != null){
    		listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    	}
	}
}
