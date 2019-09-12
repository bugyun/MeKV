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
        //        MeKV.init(new GsonStrategy());
        //
        //
        //        UserMeKV.changeStrategy(new GsonStrategy());
        //        //单次操作
        //        UserMeKV.saveName("");
        //
        //        //多次操作，先获取，在保存
        //        User user = UserMeKV.getUser();
        //        user.setAge(1);
        //        user.setName("");
        //        UserMeKV.setUser(user);

    }
}
