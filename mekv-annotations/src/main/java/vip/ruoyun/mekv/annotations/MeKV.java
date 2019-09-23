package vip.ruoyun.mekv.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ruoyun on 2019-09-12.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface MeKV {

    /**
     * 自定义后缀
     */
    String suffix() default "";

    String key() default "";

    boolean model() default true;
}
