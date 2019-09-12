package vip.ruoyun.mekv.demo;

import vip.ruoyun.mekv.annotations.MeKV;
import vip.ruoyun.mekv.annotations.Ignore;

/**
 * Created by ruoyun on 2019-09-12.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
@MeKV
public class User {

    @Ignore
    private String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
