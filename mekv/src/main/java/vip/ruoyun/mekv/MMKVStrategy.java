package vip.ruoyun.mekv;

import android.content.Context;
import android.os.Parcelable;
import com.tencent.mmkv.MMKV;

/**
 * Created by ruoyun on 2019-09-12.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction: MMKV 策略
 */
public class MMKVStrategy implements IMeKVStrategy<Parcelable> {

    private MMKV mmkv;

    public MMKVStrategy(Context context) {
        MMKV.initialize(context);
        mmkv = MMKV.defaultMMKV();
    }

    public MMKVStrategy(MMKV mmkv) {
        this.mmkv = mmkv;
    }

    @Override
    public <T extends Parcelable> void encode(final String key, final T src) {
        mmkv.encode(key, src);
    }

    @Override
    public <T extends Parcelable> T decode(final String key, final Class<T> classOfT) {
        return mmkv.decodeParcelable(key, classOfT);
    }

    @Override
    public void remove(final String key) {
        mmkv.remove(key);
    }

    @Override
    public void clear() {
        mmkv.clear();
    }
}
