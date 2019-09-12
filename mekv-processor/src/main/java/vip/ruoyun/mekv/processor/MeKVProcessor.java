package vip.ruoyun.mekv.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import vip.ruoyun.mekv.annotations.MeKV;

public class MeKVProcessor extends AbstractProcessor {


    private Filer filerUtils; // 文件相关的辅助类

    private Elements elementUtils; // 元素相关的辅助类,操作Element 的工具类

    private Messager messagerUtils; // 日志相关的辅助类,Log 日志

    private Map<String, String> options;//传进来的参数

    private List<ModelClass> providers = new ArrayList<>();

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filerUtils = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        messagerUtils = processingEnvironment.getMessager();
        options = processingEnvironment.getOptions();//获取从外传进来的参数
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        if (!roundEnvironment.processingOver()) { //处理注解
            Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(MeKV.class);
            for (Element element : elementSet) {//找到类
                log(":::::" + element
                        .getEnclosedElements());//这个类有哪些方法，User(),name,getName(),setName(java.lang.String)
                ModelClass modelClass = new ModelClass();
                modelClass.packageName = element.getEnclosingElement().toString();
                modelClass.className = element.getSimpleName().toString();
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
        } else {  //生成文件
            //多线程
            ExecutorService executorService = Executors.newCachedThreadPool();
            for (ModelClass modelClass : providers) {
                executorService.execute(() -> {
                    String packageName = modelClass.packageName;
//                    JavaFile javaFile = JavaFile.builder(packageName, generateCodeByPoet(key, variableElementList))
//                            .build();
//                    try {
//                        javaFile.writeTo(processingEnv.getFiler());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                });
            }
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private class ModelClass {

        String packageName;//包名

        String className;//类名

        Set<VariableElement> fields;//属性集合

        Set<ExecutableElement> methods;//方法集合
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(MeKV.class.getCanonicalName());
        return types;
    }

    private void log(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }
}