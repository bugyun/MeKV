package vip.ruoyun.mekv.processor;

import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by ruoyun on 2019-09-14.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:对应 model 的相关信息
 */
public class ModelClass {

    Element element;

    String packageName;//包名

    String className;//类名

    Set<VariableElement> fields;//属性集合

    Set<ExecutableElement> methods;//方法集合
}
