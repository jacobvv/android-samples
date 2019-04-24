package org.jacobvv.permission.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;

import org.jacobvv.permission.annotaion.OnPermissionDenied;
import org.jacobvv.permission.annotaion.RequiresPermission;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes(Constants.ANNOTATION_TYPE)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class PermissionProcessor extends AbstractProcessor {

    private static final List<Class<? extends Annotation>> ANNOTATIONS = Arrays.asList(
            RequiresPermission.class,
            OnPermissionDenied.class
    );

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        Map<TypeElement, PermissionSet.Builder> builderMap = new LinkedHashMap<>();
        for (Class<? extends Annotation> annotation : ANNOTATIONS) {
            findAndParse(env, annotation, builderMap);
        }
        // TODO: Build the permission set & Generate java files.
        return false;
    }

    private void findAndParse(RoundEnvironment env, Class<? extends Annotation> annotation,
                              Map<TypeElement, PermissionSet.Builder> builderMap) {
        for (Element element : env.getElementsAnnotatedWith(annotation)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseAnnotation(annotation, element, builderMap);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));

                error(element, "Unable to generate view binder for @%s.\n\n%s",
                        RequiresPermission.class.getSimpleName(), stackTrace.toString());
            }
        }
    }

    private void parseAnnotation(Class<? extends Annotation> annotationClass, Element element,
                                 Map<TypeElement, PermissionSet.Builder> builderMap) {
        // TODO: Parse and put the result into map.
    }


    private void error(Element element, String message, Object... args) {
        printMessage(Diagnostic.Kind.ERROR, element, message, args);
    }

    private void note(Element element, String message, Object... args) {
        printMessage(Diagnostic.Kind.NOTE, element, message, args);
    }

    private void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }

        processingEnv.getMessager().printMessage(kind, message, element);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : ANNOTATIONS) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }
}
