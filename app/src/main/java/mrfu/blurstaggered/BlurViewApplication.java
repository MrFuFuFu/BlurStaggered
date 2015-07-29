package mrfu.blurstaggered;

import android.app.Application;

/**
 * Created by MrFu on 15/7/29.
 */
public class BlurViewApplication extends Application {
    private static BlurViewApplication sInstance;
    public static BlurViewApplication getInstance() {
        return sInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

}