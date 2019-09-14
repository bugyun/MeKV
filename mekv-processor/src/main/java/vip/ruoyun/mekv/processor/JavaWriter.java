package vip.ruoyun.mekv.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import vip.ruoyun.mekv.annotations.MeKV;

/**
 * Created by ruoyun on 2019-09-14.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:java 代码生成
 */
public class JavaWriter {

    private static final String SUFFIX = "MeKV";//后缀

    private static final String KEY_NAME = "KEY";//key 值名称

    private static final String MeKV_PACKAGE_NAME = "vip.ruoyun.mekv";//MeKV的包名

    private static final String MeKV_CLASS_NAME = "MeKV";//MeKV的class name


    private JavaWriter() {
    }

    public static void write(ModelClass modelClass, Filer filer) {
        MeKV meKV = modelClass.element.getAnnotation(MeKV.class);
        String value = meKV.key();
        if (value.length() == 0) {
            value = modelClass.element.toString() + SUFFIX;
        }
        //key变量
        FieldSpec key = FieldSpec.builder(String.class, KEY_NAME)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$S", value)
                .build();

        //私有构造方法
        MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE)
                .build();

        //model 的类信息
        ClassName modelName = ClassName.get(modelClass.packageName, modelClass.className);
        //MeKV 相关的信息
        ClassName meKVName = ClassName.get(MeKV_PACKAGE_NAME, MeKV_CLASS_NAME);
        //get 方法
        MethodSpec getModel = MethodSpec.methodBuilder("get" + modelClass.className)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("return ($L)$T.getStrategy().decode($L,$L)", modelClass.className, meKVName, key.name,
                        modelClass.className + ".class")
                .returns(modelName)
                .build();

        //save 方法
        MethodSpec saveModel = MethodSpec.methodBuilder("save" + modelClass.className)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(modelName, "model")
                .addStatement("$T.getStrategy().encode($L,$L)", meKVName, key.name, "model")
                .returns(TypeName.VOID)
                .build();

        //remove 方法
        MethodSpec remove = MethodSpec.methodBuilder("remove")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("$T.getStrategy().remove($L)", meKVName, key.name)
                .returns(TypeName.VOID)
                .build();

        //java文件
        TypeSpec helloWorld = TypeSpec.classBuilder(modelClass.className + "MeKV")
                .addModifiers(Modifier.PUBLIC)
                .addField(key)
                .addMethod(constructor)
                .addMethod(getModel)
                .addMethod(saveModel)
                .addMethod(remove)
                .build();

        JavaFile javaFile = JavaFile.builder(modelClass.packageName, helloWorld)
                .build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
