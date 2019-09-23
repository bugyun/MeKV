package vip.ruoyun.mekv.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.Set;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import vip.ruoyun.mekv.annotations.Ignore;
import vip.ruoyun.mekv.annotations.MeKV;

/**
 * Created by ruoyun on 2019-09-14.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:java 代码生成
 */
public class JavaWriter {

    private static String SUFFIX = "MeKV";//后缀

    private static final String KEY_NAME = "KEY";//key 值名称

    private static final String MeKV_PACKAGE_NAME = "vip.ruoyun.mekv";//MeKV的包名

    private static final String MeKV_CLASS_NAME = "MeKV";//MeKV的class name


    private JavaWriter() {
    }

    public static void write(ModelClass modelClass, Filer filer, final Messager messager) {
        MeKV meKV = modelClass.element.getAnnotation(MeKV.class);

        String suffixValue = meKV.suffix();
        if (suffixValue.length() > 0) {
            SUFFIX = suffixValue;
        } else {
            SUFFIX = "MeKV";
        }

        if (meKV.model()) {
            generateModel(modelClass, filer);
        } else {//生成 key-value 形式
            String value = meKV.key();
            if (value.length() == 0) {
                value = modelClass.element.toString() + SUFFIX;
            }
            //MeKV 相关的信息
            ClassName meKVName = ClassName.get(MeKV_PACKAGE_NAME, MeKV_CLASS_NAME);
            //java文件
            TypeSpec.Builder javaCode = TypeSpec.classBuilder(modelClass.className + SUFFIX)
                    .addModifiers(Modifier.PUBLIC);

            //key变量
            FieldSpec key = FieldSpec.builder(String.class, KEY_NAME)
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                    .initializer("$S", value)
                    .build();
            javaCode.addField(key);

            //私有构造方法
            MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE)
                    .build();
            javaCode.addMethod(constructor);

            //
            for (VariableElement variableElement : modelClass.fields) {
                Ignore ignore = variableElement.getAnnotation(Ignore.class);
                if (ignore == null) {
                    if (variableElement.asType().getKind().isPrimitive()) {
                        //基本数据类型
                        //get 方法
                        MethodSpec getModel = MethodSpec
                                .methodBuilder("get" + Utils.captureName(variableElement.getSimpleName().toString()))
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .addStatement("return $T.getStrategy().decode$L($L+$S)",
                                        meKVName,
                                        Utils.captureName(variableElement.asType().toString()),
                                        key.name,
                                        "." + variableElement.getSimpleName())
                                .returns(TypeName.get(variableElement.asType()))
                                .build();
                        javaCode.addMethod(getModel);

                        //save 方法
                        MethodSpec saveModel = MethodSpec
                                .methodBuilder("save" + Utils.captureName(variableElement.getSimpleName().toString()))
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .addParameter(TypeName.get(variableElement.asType()), "value")
                                .addStatement("$T.getStrategy().encode($L+$S,$L)", meKVName,
                                        key.name,
                                        "." + variableElement.getSimpleName(),
                                        "value")
                                .returns(TypeName.VOID)
                                .build();
                        javaCode.addMethod(saveModel);
                        //remove 方法
                        MethodSpec remove = MethodSpec.methodBuilder(
                                "remove" + Utils.captureName(variableElement.getSimpleName().toString()))
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .addStatement("$T.getStrategy().remove($L+$S)", meKVName,
                                        key.name,
                                        "." + variableElement.getSimpleName())
                                .returns(TypeName.VOID)
                                .build();
                        javaCode.addMethod(remove);
                    } else if (variableElement.asType().getKind() == TypeKind.ARRAY) {
                        String function = "";
                        if (variableElement.asType().toString().endsWith("byte[]")) {
                            function = "decodeBytes";
                        }
                        if (function.length() == 0) {
                            continue;
                        }
                        //get 方法
                        MethodSpec getModel = MethodSpec
                                .methodBuilder("get" + Utils.captureName(variableElement.getSimpleName().toString()))
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .addStatement("return $T.getStrategy().$L($L+$S)",
                                        meKVName,
                                        function,
                                        key.name,
                                        "." + variableElement.getSimpleName())
                                .returns(byte[].class)
                                .build();
                        javaCode.addMethod(getModel);
                        //save 方法
                        MethodSpec saveModel = MethodSpec
                                .methodBuilder("save" + Utils.captureName(variableElement.getSimpleName().toString()))
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .addParameter(TypeName.get(variableElement.asType()), "value")
                                .addStatement("$T.getStrategy().encode($L+$S,$L)",
                                        meKVName,
                                        key.name,
                                        "." + variableElement.getSimpleName(),
                                        "value")
                                .returns(TypeName.VOID)
                                .build();
                        javaCode.addMethod(saveModel);
                        //remove 方法
                        MethodSpec remove = MethodSpec.methodBuilder(
                                "remove" + Utils.captureName(variableElement.getSimpleName().toString()))
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .addStatement("$T.getStrategy().remove($L+$S)", meKVName,
                                        key.name,
                                        "." + variableElement.getSimpleName())
                                .returns(TypeName.VOID)
                                .build();
                        javaCode.addMethod(remove);

                    } else if (variableElement.asType().getKind() == TypeKind.DECLARED) {

                        if (variableElement.asType().toString().endsWith(">")) {
                            if (!variableElement.asType().toString().startsWith("java.util.Set")) {
                                continue;
                            }
                        }
//                        messager.printMessage(Diagnostic.Kind.NOTE,
//                                "名称,,,," + variableElement.getSimpleName().toString());
//
//                        messager.printMessage(Diagnostic.Kind.NOTE,
//                                "类型,,,," + variableElement.asType().getKind().toString());
//
//                        messager.printMessage(Diagnostic.Kind.NOTE,
//                                "类型,,,," + variableElement.asType().toString());

                        //java.lang.String
                        //java.util.Set<java.lang.String>
                        TypeName modelName;
                        if (variableElement.asType().toString().endsWith("java.util.Set<java.lang.String>")) {
                            modelName = ParameterizedTypeName.get(Set.class, String.class);
                            //get 方法
                            MethodSpec getModel = MethodSpec
                                    .methodBuilder(
                                            "get" + Utils.captureName(variableElement.getSimpleName().toString()))
                                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                    .addStatement("return $T.getStrategy().decodeStringSet($L+$S)",
                                            meKVName,
                                            key.name,
                                            "." + variableElement.getSimpleName())
                                    .returns(modelName)
                                    .build();
                            javaCode.addMethod(getModel);
                        } else {
                            int lastIndexOf = variableElement.asType().toString().lastIndexOf(".");

                            String packageName = variableElement.asType().toString().substring(0, lastIndexOf);
                            String className = variableElement.asType().toString().substring(lastIndexOf + 1);

//                            messager.printMessage(Diagnostic.Kind.NOTE, packageName);
//                            messager.printMessage(Diagnostic.Kind.NOTE, className);

                            //vip.ruoyun.mekv.demo.model.People
//                            messager.printMessage(Diagnostic.Kind.NOTE,
//                                    variableElement.asType().toString().lastIndexOf(".") + "");

                            //model 的类信息
                            modelName = ClassName.get(packageName, className);
                            //get 方法
                            MethodSpec getModel = MethodSpec
                                    .methodBuilder(
                                            "get" + Utils.captureName(variableElement.getSimpleName().toString()))
                                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                    .addStatement("return ($L)$T.getStrategy().decode($L+$S,$L)",
                                            className,
                                            meKVName,
                                            key.name,
                                            "." + variableElement.getSimpleName(),
                                            className + ".class")
                                    .returns(modelName)
                                    .build();
                            javaCode.addMethod(getModel);
                        }

                        //save 方法
                        MethodSpec saveModel = MethodSpec
                                .methodBuilder("save" + Utils.captureName(variableElement.getSimpleName().toString()))
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .addParameter(modelName, "model")
                                .addStatement("$T.getStrategy().encode($L+$S,$L)",
                                        meKVName,
                                        key.name,
                                        "." + variableElement.getSimpleName(),
                                        "model")
                                .returns(TypeName.VOID)
                                .build();

                        javaCode.addMethod(saveModel);

                        //remove 方法
                        MethodSpec remove = MethodSpec.methodBuilder(
                                "remove" + Utils.captureName(variableElement.getSimpleName().toString()))
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .addStatement("$T.getStrategy().remove($L+$S)",
                                        meKVName,
                                        key.name,
                                        "." + variableElement.getSimpleName())
                                .returns(TypeName.VOID)
                                .build();

                        javaCode.addMethod(remove);

                    }
                }
            }

//            for (ExecutableElement executableElement : modelClass.methods) {
//                messager.printMessage(Diagnostic.Kind.NOTE, executableElement.getSimpleName());
//            }

            JavaFile javaFile = JavaFile.builder(modelClass.packageName, javaCode.build())
                    .addFileComment("Generated code from MeKV . Do not modify!")
                    .build();
            try {
                javaFile.writeTo(filer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //生成 model 形式
    private static void generateModel(ModelClass modelClass, Filer filer) {
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
        TypeSpec javaCode = TypeSpec.classBuilder(modelClass.className + SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .addField(key)
                .addMethod(constructor)
                .addMethod(getModel)
                .addMethod(saveModel)
                .addMethod(remove)

                .build();

        JavaFile javaFile = JavaFile.builder(modelClass.packageName, javaCode)
                .addFileComment("Generated code from MeKV . Do not modify!")
                .build();
        try {
            javaFile.writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
