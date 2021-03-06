package vip.ruoyun.mekv.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import vip.ruoyun.mekv.annotations.MeKV;

/**
 * Created by ruoyun on 2019-09-12.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction: 代码执行器
 */
public class MeKVProcessor extends AbstractProcessor {

    private Messager messager; // 日志相关的辅助类,Log 日志

    private List<ModelClass> providers = new ArrayList<>();//model 集合

    private AtomicBoolean once;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        once = new AtomicBoolean();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        if (!roundEnvironment.processingOver()) { //处理注解
            if (once.compareAndSet(false, true)) {
                Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(MeKV.class);
                for (Element element : elementSet) {//找到类
                    log(":::::" + element
                            .getEnclosedElements());//这个类有哪些方法，User(),name,getName(),setName(java.lang.String)
                    ModelClass modelClass = new ModelClass();
                    modelClass.packageName = element.getEnclosingElement().toString();
                    modelClass.className = element.getSimpleName().toString();
                    modelClass.element = element;
                    Set<VariableElement> fieldSet = new HashSet<>();
                    Set<ExecutableElement> methodSet = new HashSet<>();
                    for (Element enclosedElement : element.getEnclosedElements()) {//找到属性
                        if (enclosedElement instanceof VariableElement) {
                            if (enclosedElement.getKind() == ElementKind.FIELD) {//属性
                                fieldSet.add((VariableElement) enclosedElement);
                            }
                        }
                        if (enclosedElement instanceof ExecutableElement) {
                            if (enclosedElement.getKind() == ElementKind.METHOD) {//属性
                                methodSet.add((ExecutableElement) enclosedElement);
//                            log(enclosedElement.getSimpleName().toString());
                            }
                        }
                    }
                    modelClass.fields = fieldSet;
                    modelClass.methods = methodSet;
                    providers.add(modelClass);

                }
                for (ModelClass modelClass : providers) {
                    JavaWriter.write(modelClass, processingEnv.getFiler(), messager);
                }
            }
        }
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(MeKV.class.getCanonicalName());
        return types;
    }

    private void log(String message) {
        if (false) {
            messager.printMessage(Diagnostic.Kind.NOTE, message);
        }
    }
}