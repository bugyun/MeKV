package vip.ruoyun.mekv.demo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.tencent.mmkv.MMKV;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        MeKV.init(new GsonStrategy());
        //
        //
//        UserMeKV.changeStrategy(new GsonStrategy());
        //单次操作
        //多次操作，先获取，在保存
        User user = UserMeKV.getUser();
        if (user != null) {

        }
        user.setName("");
        user.getName();
        UserMeKV.saveUser(user);
        User user1 = MMKV.defaultMMKV().decodeParcelable("", User.class);

//        PeopleMeKV.getPeople();
//        PeopleMeKV.saveUser(new User());

//        StudentMeKV.getStudent();

    }
}
