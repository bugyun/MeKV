package vip.ruoyun.mekv;

import android.content.Context;
import android.os.Parcelable;
import com.tencent.mmkv.MMKV;
import java.util.Set;

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
    public <T extends Parcelable> boolean encode(final String key, final T src) {
        return mmkv.encode(key, src);
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

    @Override
    public boolean encode(final String key, final boolean value) {
        return mmkv.encode(key, value);
    }

    @Override
    public boolean decodeBoolean(final String key) {
        return mmkv.decodeBool(key);
    }

    @Override
    public boolean encode(final String key, final int value) {
        return mmkv.encode(key, value);
    }

    @Override
    public int decodeInt(final String key) {
        return mmkv.decodeInt(key);
    }

    @Override
    public boolean encode(final String key, final long value) {
        return mmkv.encode(key, value);
    }

    @Override
    public long decodeLong(final String key) {
        return mmkv.decodeLong(key);
    }

    @Override
    public boolean encode(final String key, final float value) {
        return mmkv.encode(key, value);
    }

    @Override
    public float decodeFloat(final String key) {
        return mmkv.decodeFloat(key);
    }

    @Override
    public boolean encode(final String key, final double value) {
        return mmkv.encode(key, value);
    }

    @Override
    public double decodeDouble(final String key) {
        return mmkv.decodeDouble(key);
    }

    @Override
    public boolean encode(final String key, final String value) {
        return mmkv.encode(key, value);
    }

    @Override
    public String decodeString(final String key) {
        return mmkv.decodeString(key);
    }

    @Override
    public boolean encode(final String key, final Set<String> value) {
        return mmkv.encode(key, value);
    }

    @Override
    public Set<String> decodeStringSet(final String key) {
        return mmkv.decodeStringSet(key);
    }

    @Override
    public boolean encode(final String key, final byte[] value) {
        return mmkv.encode(key, value);
    }

    @Override
    public byte[] decodeBytes(final String key) {
        return mmkv.decodeBytes(key);
    }
}
