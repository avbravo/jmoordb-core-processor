/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor;

import com.jmoordb.core.processor.internal.JSonObjectProcessorAux;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.sun.source.tree.Scope;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 *
 * @author avbravo
 */
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("com.avbravo.jmoordb.core.annotation.JsonObject")
public class JsonObjectProcessor  extends AbstractProcessor{
    
     private static final String TEMPLATE = "com/avbravo/jmoordb/template/jsonwriter.mustache";

    private final Mustache template;

    public JsonObjectProcessor() {
        this.template = createTemplate();
    }

    private Mustache createTemplate() {
        MustacheFactory factory = new DefaultMustacheFactory();
        return factory.compile(TEMPLATE);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for ( TypeElement annotation : annotations ) {
            for ( Element element : roundEnv.getElementsAnnotatedWith(annotation) ) {
                try {
                    processJsonObject(element);
                } catch (IOException e) {
                    error(e);
                }
            }
        }
        return true;
    }

    private void processJsonObject(Element element) throws IOException {
        if (isTypeElement(element)) {
            TypeElement typeElement = (TypeElement) element;
            JSonObjectProcessorAux jSonObjectProcessorAux = createModel(typeElement);
            writeJsonWriterClass(element, jSonObjectProcessorAux);
        }
    }

    private void writeJsonWriterClass(Element element, JSonObjectProcessorAux jSonObjectProcessorAux) throws IOException {
        Filer filer = processingEnv.getFiler();
        JavaFileObject fileObject = filer.createSourceFile(jSonObjectProcessorAux.getTargetClassNameWithPackage(), element);
        try (Writer writer = fileObject.openWriter()) {
            template.execute(writer, jSonObjectProcessorAux);
        }
    }

    private JSonObjectProcessorAux createModel(TypeElement element) {
        String packageName = getPackageName(element);
        String sourceClassName = getSimpleNameAsString(element);

       JSonObjectProcessorAux jSonObjectProcessorAux = new JSonObjectProcessorAux(packageName, sourceClassName);
        appendFields(element, jSonObjectProcessorAux); 
        return jSonObjectProcessorAux;
    }

    private String getSimpleNameAsString(Element element) {
        return element.getSimpleName().toString();
    }

    private String getPackageName(TypeElement classElement) {
        return ((PackageElement) classElement.getEnclosingElement()).getQualifiedName().toString();
    }

    private void appendFields(TypeElement element,JSonObjectProcessorAux jSonObjectProcessorAux) {
        if (isTypeElement(element)) {
            for (String getterMethod : getAllGetterMethodNames(element)) {
                jSonObjectProcessorAux.addGetter(getterMethod);
            }
        }
    }

    private boolean isTypeElement(Element element) {
        return element instanceof TypeElement;
    }

    private List<String> getAllGetterMethodNames(TypeElement typeElement) {
        return processingEnv.getElementUtils().getAllMembers(typeElement)
                .stream()
                .filter(el -> el.getKind() == ElementKind.METHOD)
                .map(el -> getSimpleNameAsString(el))
                .filter(name -> name.startsWith("get"))
                .collect(Collectors.toList());
    }

    private void error(IOException e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "failed to write extension file: " + e.getMessage());
    }
}
