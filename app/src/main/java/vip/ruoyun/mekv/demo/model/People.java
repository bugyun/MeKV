package vip.ruoyun.mekv.demo.model;

import java.util.Set;
import vip.ruoyun.mekv.annotations.MeKV;
import vip.ruoyun.mekv.demo.User;

@MeKV(isModel = false)
public class People {

    //    @Ignore
    private String name;

    private boolean isOld;

    private int age;

    private double doubleMoney;

    private long longMoney;

    private byte[] mBytes;

    private User user;

    private Set<String> setString;
}
