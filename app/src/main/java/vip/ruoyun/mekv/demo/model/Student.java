package vip.ruoyun.mekv.demo.model;

import android.os.Parcel;
import android.os.Parcelable;
import vip.ruoyun.mekv.annotations.MeKV;

@MeKV(key = "你好")
public class Student implements Parcelable {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public Student() {
    }

    protected Student(Parcel in) {
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}
