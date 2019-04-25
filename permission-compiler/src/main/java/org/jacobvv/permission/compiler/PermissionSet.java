package org.jacobvv.permission.compiler;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.google.auto.common.MoreElements.getPackage;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * @author jacob
 * @date 19-4-24
 */
class PermissionSet {
    private static int requestCodeIndex = 1;

    private final TypeName targetTypeName;
    private final ClassName permissionClassName;
    private final ImmutableList<PermissionRequestSet> requests;

    private PermissionSet(TypeName targetTypeName, ClassName permissionClassName,
                          ImmutableList<PermissionRequestSet> requests) {
        this.targetTypeName = targetTypeName;
        this.permissionClassName = permissionClassName;
        this.requests = requests;
    }

    static Builder newBuilder(TypeElement enclosingElement) {
        TypeMirror typeMirror = enclosingElement.asType();

        TypeName targetType = TypeName.get(typeMirror);
        if (targetType instanceof ParameterizedTypeName) {
            targetType = ((ParameterizedTypeName) targetType).rawType;
        }

        String packageName = getPackage(enclosingElement).getQualifiedName().toString();
        String className = enclosingElement.getQualifiedName().toString().substring(
                packageName.length() + 1).replace('.', '$');
        ClassName helperClassName = ClassName.get(packageName,
                className + "_PermissionHelper");

        return new Builder(targetType, helperClassName);
    }

    JavaFile brewJava() {
        TypeSpec permissionHelper = createType();
        return JavaFile.builder(permissionClassName.packageName(), permissionHelper).build();
    }

    private TypeSpec createType() {
        MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(PRIVATE).build();
        TypeSpec.Builder result = TypeSpec.classBuilder(permissionClassName.simpleName())
                .addJavadoc(Constants.WARNING_TIPS)
                .addModifiers(FINAL)
                .addMethod(constructor);

        ClassName nonNull = ClassName.get(Constants.ANNOTATION_PACKAGE, Constants.TYPE_NONNULL);
        ClassName permissionUtils = ClassName.get(Constants.PERMISSION_PACKAGE, Constants.TYPE_UTILS);
        CodeBlock.Builder switchBuilder = CodeBlock.builder()
                .beginControlFlow("switch ($N)", Constants.VAR_REQUEST_CODE);
        MethodSpec.Builder resultBuilder = MethodSpec.methodBuilder(Constants.METHOD_RESULT)
                .addModifiers(STATIC)
                .addParameter(ParameterSpec.builder(targetTypeName, Constants.VAR_TARGET)
                        .addAnnotation(nonNull).build())
                .addParameter(int.class, Constants.VAR_REQUEST_CODE)
                .addParameter(ParameterSpec.builder(String[].class, Constants.VAR_PERMISSIONS)
                        .addAnnotation(nonNull).build())
                .addParameter(ParameterSpec.builder(int[].class, Constants.VAR_GRANT_RESULTS)
                        .addAnnotation(nonNull).build())
                .addStatement("$T<$T> $N = $T.$N($N, $N)", List.class, String.class,
                        Constants.VAR_DENIED_FOREVER, permissionUtils, Constants.METHOD_CHECK,
                        Constants.VAR_PERMISSIONS, Constants.VAR_GRANT_RESULTS);

        for (PermissionRequestSet request : requests) {
            result.addField(request.createFieldRequestCode())
                    .addField(request.createFieldPermissions())
                    .addMethod(request.createMethodWithCheck())
                    .addType(request.createInterfaceRequest());
//            switchBuilder.add(request.createSwitchCase());
        }

        CodeBlock switchCode = switchBuilder.add("default:\n").endControlFlow().build();
        result.addMethod(resultBuilder.addCode(switchCode).build());

        return result.build();
    }

    static final class Builder {
        private final TypeName targetTypeName;
        private final ClassName permissionClassName;

        private final Map<Integer, PermissionRequestSet.Builder> requestsMap = new HashMap<>();

        private Builder(TypeName targetTypeName, ClassName permissionClassName) {
            this.targetTypeName = targetTypeName;
            this.permissionClassName = permissionClassName;
        }

        private PermissionRequestSet.Builder getOrCreateRequest(int requestCode) {
            PermissionRequestSet.Builder request = requestsMap.get(requestCode);
            if (request == null) {
                request = new PermissionRequestSet.Builder(targetTypeName, requestCode);
                requestsMap.put(requestCode, request);
            }
            return request;
        }

        PermissionSet build() {
            ImmutableList.Builder<PermissionRequestSet> requests = ImmutableList.builder();
            for (PermissionRequestSet.Builder builder : requestsMap.values()) {
                requests.add(builder.build());
            }
            return new PermissionSet(targetTypeName, permissionClassName, requests.build());
        }

        boolean addPermissionRequestMethod(int requestCode, String[] permissions,
                                           MethodInfo method) {
            if (requestCode == 0) {
                requestCode = requestCodeIndex++;
            }
            PermissionRequestSet.Builder request = getOrCreateRequest(requestCode);
            if (request.hasMethodBinding()) {
                return false;
            }
            request.addMethodBinding(permissions, method);
            return true;
        }

    }
}
