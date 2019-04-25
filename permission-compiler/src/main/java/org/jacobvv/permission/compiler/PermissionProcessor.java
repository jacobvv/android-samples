package org.jacobvv.permission.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;

import org.jacobvv.permission.annotaion.OnPermissionDenied;
import org.jacobvv.permission.annotaion.OnShowRationale;
import org.jacobvv.permission.annotaion.RequiresPermission;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.Diagnostic;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class PermissionProcessor extends AbstractProcessor {

    private static final List<Class<? extends Annotation>> ANNOTATIONS = Arrays.asList(
            RequiresPermission.class,
            OnPermissionDenied.class,
            OnShowRationale.class
    );

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        filer = env.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        Map<TypeElement, PermissionSet> permissionSet = findAndParseTargets(env);

        for (Map.Entry<TypeElement, PermissionSet> entry : permissionSet.entrySet()) {
            TypeElement typeElement = entry.getKey();
            PermissionSet permission = entry.getValue();

            JavaFile javaFile = permission.brewJava();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                error(typeElement, "Unable to write binding for type %s: %s", typeElement, e.getMessage());
            }
        }

        return false;
    }

    private Map<TypeElement, PermissionSet> findAndParseTargets(RoundEnvironment env) {
        Map<TypeElement, PermissionSet.Builder> builderMap = new LinkedHashMap<>();

        // Process @RequiresPermission element.
        for (Element element : env.getElementsAnnotatedWith(RequiresPermission.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parsePermission(element, builderMap);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));
                error(element, "Unable to generate permission helper for @%s.\n\n%s",
                        RequiresPermission.class.getSimpleName(), stackTrace.toString());
            }
        }

        // Process @OnShowRationale element.
        for (Element element : env.getElementsAnnotatedWith(OnShowRationale.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseRationaleCallback(element, builderMap);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));
                error(element, "Unable to generate permission helper for @%s.\n\n%s",
                        OnShowRationale.class.getSimpleName(), stackTrace.toString());
            }
        }

        // Process @OnPermissionDenied element.
        for (Element element : env.getElementsAnnotatedWith(OnPermissionDenied.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parsePermissionDeniedCallback(element, builderMap);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));
                error(element, "Unable to generate permission helper for @%s.\n\n%s",
                        OnPermissionDenied.class.getSimpleName(), stackTrace.toString());
            }
        }

        Map<TypeElement, PermissionSet> permissionMap = new LinkedHashMap<>();
        for (Map.Entry<TypeElement, PermissionSet.Builder> entry : builderMap.entrySet()) {
            TypeElement type = entry.getKey();
            PermissionSet.Builder builder = entry.getValue();
            permissionMap.put(type, builder.build());
        }

        return permissionMap;
    }

    private void parsePermission(
            Element element, Map<TypeElement, PermissionSet.Builder> builderMap) throws Exception {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        // Verify that the method and its containing class are accessible via generated code.
        if (isMethodAnnotationInvalid(element, enclosingElement, RequiresPermission.class)
                || isAccessibleInvalid(element, enclosingElement, RequiresPermission.class)
                || isPackageInvalid(element, enclosingElement, RequiresPermission.class)) {
            return;
        }
        ExecutableElement executableElement = (ExecutableElement) element;

        // Assemble information on the method.
        Annotation annotation = element.getAnnotation(RequiresPermission.class);
        Method annotationValue = RequiresPermission.class.getDeclaredMethod("value");
        if (annotationValue.getReturnType() != String[].class) {
            throw new IllegalStateException(
                    "@RequiresPermission annotation value() type not String[].");
        }
        Method annotationRequestCode = RequiresPermission.class.getDeclaredMethod("requestCode");
        if (annotationRequestCode.getReturnType() != int.class) {
            throw new IllegalStateException(
                    "@RequiresPermission annotation requestCode() type not int.");
        }

        String[] permissions = (String[]) annotationValue.invoke(annotation);
        int requestCode = (int) annotationRequestCode.invoke(annotation);
        String name = executableElement.getSimpleName().toString();

        List<? extends VariableElement> methodParameters = executableElement.getParameters();
        List<ParameterInfo> parameters = new ArrayList<>(methodParameters.size());
        if (!methodParameters.isEmpty()) {
            for (int i = 0; i < methodParameters.size(); i++) {
                VariableElement methodParameter = methodParameters.get(i);
                TypeMirror methodParameterType = methodParameter.asType();
                if (methodParameterType instanceof TypeVariable) {
                    TypeVariable typeVariable = (TypeVariable) methodParameterType;
                    methodParameterType = typeVariable.getUpperBound();
                }
                parameters.add(new ParameterInfo(i, TypeName.get(methodParameterType)));
            }
        }

        MethodInfo method = new MethodInfo(name, parameters); // Return value is ignored.
        PermissionSet.Builder builder = getOrCreateBindingBuilder(builderMap, enclosingElement);
        if (!builder.addPermissionRequestMethod(requestCode, permissions, method)) {
            error(element, "Duplicate permission request code in same Class. (%s.%s)",
                    enclosingElement.getQualifiedName(), element.getSimpleName());
        }
    }

    private void parseRationaleCallback(
            Element element, Map<TypeElement, PermissionSet.Builder> builderMap) throws Exception {
    }

    private void parsePermissionDeniedCallback(
            Element element, Map<TypeElement, PermissionSet.Builder> builderMap) throws Exception {

    }

    private PermissionSet.Builder getOrCreateBindingBuilder(
            Map<TypeElement, PermissionSet.Builder> builderMap, TypeElement enclosingElement) {
        PermissionSet.Builder builder = builderMap.get(enclosingElement);
        if (builder == null) {
            builder = PermissionSet.newBuilder(enclosingElement);
            builderMap.put(enclosingElement, builder);
        }
        return builder;
    }

    private boolean isMethodAnnotationInvalid(Element element, TypeElement enclosingElement,
                                              Class<? extends Annotation> annotation) {
        // This should be guarded by the annotation's @Target but it's worth a check for safe casting.
        if (!(element instanceof ExecutableElement) || element.getKind() != METHOD) {
            error(element, "@%s annotation must be on a method. (%s.%s)",
                    annotation.getSimpleName(), enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            return true;
        }
        return false;
    }

    private boolean isAccessibleInvalid(Element element, TypeElement enclosingElement,
                                        Class<? extends Annotation> annotation) {
        boolean isInvalid = false;

        // Verify field or method modifiers.
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
            error(element, "@%s method must not be private or static. (%s.%s)",
                    annotation.getSimpleName(), enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            isInvalid = true;
        }

        // Verify containing type.
        if (enclosingElement.getKind() != CLASS) {
            error(enclosingElement, "@%s method may only be contained in classes. (%s.%s)",
                    annotation.getSimpleName(), enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            isInvalid = true;
        }

        // Verify containing class visibility is not private.
        if (enclosingElement.getModifiers().contains(PRIVATE)) {
            error(enclosingElement, "@%s method may not be contained in private classes. (%s.%s)",
                    annotation.getSimpleName(), enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            isInvalid = true;
        }

        return isInvalid;
    }

    private boolean isPackageInvalid(Element element, TypeElement enclosingElement,
                                     Class<? extends Annotation> annotation) {
        String qualifiedName = enclosingElement.getQualifiedName().toString();
        if (qualifiedName.startsWith("android.")) {
            error(element, "@%s-annotated class incorrectly in Android framework package. (%s)",
                    annotation.getSimpleName(), qualifiedName);
            return true;
        }
        if (qualifiedName.startsWith("androidx.")) {
            error(element, "@%s-annotated class incorrectly in Androidx jetpack package. (%s)",
                    annotation.getSimpleName(), qualifiedName);
            return true;
        }
        if (qualifiedName.startsWith("java.")) {
            error(element, "@%s-annotated class incorrectly in Java framework package. (%s)",
                    annotation.getSimpleName(), qualifiedName);
            return true;
        }
        return false;
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
        Set<String> types = new HashSet<>();
        for (Class<? extends Annotation> annotation : ANNOTATIONS) {
            types.add(annotation.getCanonicalName());
        }
        types.add(RequiresPermission.class.getCanonicalName());
        return types;
    }
}
