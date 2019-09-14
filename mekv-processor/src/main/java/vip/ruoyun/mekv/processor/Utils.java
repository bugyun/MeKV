package vip.ruoyun.mekv.processor;

public class Utils {


    public static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
