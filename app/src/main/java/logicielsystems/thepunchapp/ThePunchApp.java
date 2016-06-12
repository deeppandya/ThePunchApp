package logicielsystems.thepunchapp;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by Deep on 12/06/2016.
 */
public class ThePunchApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
