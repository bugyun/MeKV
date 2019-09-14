package vip.ruoyun.mekv.demo.model;

import vip.ruoyun.mekv.annotations.MeKV;
import vip.ruoyun.mekv.demo.User;

@MeKV(isModel = true)
public class People {

//    @Ignore
    private String name;

    private boolean isOld;

    private int age;

    private double doubleMoney;

    private long longMoney;

    private Byte[] mBytes;

    private User user;



}
