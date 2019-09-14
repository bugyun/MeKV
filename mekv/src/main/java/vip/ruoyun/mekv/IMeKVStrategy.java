package vip.ruoyun.mekv;

import java.util.Set;

/**
 * Created by ruoyun on 2019-09-12.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction: 策略
 */
public interface IMeKVStrategy<E> {

    /**
     * 编码
     *
     * @param key key
     * @param src 要保存的 bean
     * @param <T> 传入字符的泛型
     */
    <T extends E> boolean encode(String key, T src);

    /**
     * 解码
     *
     * @param key      key
     * @param classOfT 类型
     * @param <T>      泛型
     * @return bean
     */
    <T extends E> T decode(String key, Class<T> classOfT);

    /**
     * 移除指定对象
     */
    void remove(String key);

    /**
     * 删除全局的缓存数据
     */
    void clear();


    boolean encode(String key, boolean value);

    boolean decodeBoolean(String key);


    boolean encode(String key, int value);

    int decodeInt(String key);


    boolean encode(String key, long value);

    long decodeLong(String key);


    boolean encode(String key, float value);

    float decodeFloat(String key);


    boolean encode(String key, double value);

    double decodeDouble(String key);


    boolean encode(String key, String value);

    String decodeString(String key);


    boolean encode(String key, Set<String> value);

    Set<String> decodeStringSet(String key);

    boolean encode(String key, byte[] value);

    byte[] decodeBytes(String key);
}
