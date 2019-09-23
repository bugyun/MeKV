package vip.ruoyun.mekv.processor;

public class Utils {

    /**
     * 大写第一位字母
     */
    public static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
