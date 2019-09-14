package vip.ruoyun.mekv.demo;

import android.app.Application;
import vip.ruoyun.mekv.MMKVStrategy;
import vip.ruoyun.mekv.MeKV;

/**
 * Created by ruoyun on 2019-09-12.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MeKV.init(new MMKVStrategy(this));
    }
}
