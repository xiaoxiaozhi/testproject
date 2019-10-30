package test.com.myprocess;


import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

// 一定要新建一个module 类型 是'java-library' ；奇怪的是再建一个类继承不了AbstractProcessor
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedAnnotationTypes({"",""})
//编译时注解处理器
@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor {
    private Types mTypeUtils;
    private Elements mElementUtils;
    private Filer mFiler;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mTypeUtils = processingEnv.getTypeUtils();
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }

    // 这个方法会执行3遍但是只有第一遍，能遍历到元素。
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //
//        mTypeUtils.
        mMessager.printMessage(Diagnostic.Kind.NOTE, "-------日志开始---------------");
//        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(MyButterknife.class);
//        for (Element element : elementsAnnotatedWith) {
//            if (element.getKind() == ElementKind.CLASS) {
//                TypeElement typeElement = (TypeElement) element;
//                PackageElement packageElement = mElementUtils.getPackageOf(element);
//                String packagePath = packageElement.getQualifiedName().toString();
//                String className = typeElement.getSimpleName().toString();
//                try {
//                    JavaFileObject sourceFile = mFiler.createSourceFile(packagePath + "." + className + "_ViewBinding", typeElement);
//                    Writer writer = sourceFile.openWriter();
//                    writer.write("package  " + packagePath + ";\n");
//                    writer.write("import  " + packagePath + "." + className + ";\n");
//                    writer.write("public class " + className + "_ViewBinding" + "  { \n");
//                    writer.write("\n");
//                    writer.append("       public " + className + "  targe;\n");
//                    writer.write("\n");
//                    writer.append("}");
//                    writer.flush();
//                    writer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

        for (Element element : roundEnvironment.getElementsAnnotatedWith(MyButterknife.class)) {
//            mMessager.printMessage(Diagnostic.Kind.NOTE, element.getSimpleName());
            if (element.getKind() == ElementKind.FIELD) {//被注解的元素类型
                VariableElement ve = (VariableElement) element;

                mMessager.printMessage(Diagnostic.Kind.NOTE, ve.getSimpleName());
            }
        }

        mMessager.printMessage(Diagnostic.Kind.NOTE, "-------日志结束---------------");
        return false;
    }

    // 输入java版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        System.out.println("注解需要的java版本" + SourceVersion.latestSupported().name());
        return SourceVersion.latestSupported();
    }

    //添加要  解释的注解。1.7以后已经能用注解代替，看类开头
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> sets = new LinkedHashSet<>();
        sets.add("test.com.myprocess.MyButterknife");
        return sets;
    }
}
