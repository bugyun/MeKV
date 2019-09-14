package vip.ruoyun.mekv;

/**
 * Created by ruoyun on 2019-09-12.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
public class MeKV {

    private MeKV() {
    }

    private static class SingletonHolder {

        private static final MeKV INSTANCE = new MeKV();
    }

    private static MeKV getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private IMeKVStrategy mMeKVStrategy;

    public static void init(IMeKVStrategy strategy) {
        getInstance().mMeKVStrategy = strategy;
    }

    public static void clear() {
        if (getInstance().mMeKVStrategy != null) {
            getInstance().mMeKVStrategy.clear();
        }
    }

    public static IMeKVStrategy getStrategy() {
        return getInstance().mMeKVStrategy;
    }
}
