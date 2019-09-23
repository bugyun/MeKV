package vip.ruoyun.mekv.demo;

import android.os.Parcel;
import vip.ruoyun.mekv.annotations.MeKV;

/**
 * Created by ruoyun on 2019-09-23.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
@MeKV(suffix = "Manger")
public class Order implements android.os.Parcelable {

    private String name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public Order() {
    }

    protected Order(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
