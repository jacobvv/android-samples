package org.jacobvv.permission.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * @author jacob
 * @date 19-4-24
 */
class PermissionRequestSet {

    private final TypeName targetTypeName;
    private final int requestCode;
    private final String[] permissions;
    private final MethodInfo method;
    private final MethodInfo rationale;
    private final MethodInfo permissionDenied;

    private final String fieldRequestCode;
    private final String fieldPermissions;
    private final String classNameOfRequest;

    private final CodeBlock requestPermission;
    private final CodeBlock callTargetCode;
    private final CodeBlock callRationaleCode;
    private final CodeBlock callDeniedCode;

    private final ClassName nonNull;
    private final ClassName permissionUtils;

    private PermissionRequestSet(TypeName targetTypeName, int requestCode, String[] permissions, MethodInfo method,
                                 MethodInfo rationale, MethodInfo permissionDenied) {
        this.targetTypeName = targetTypeName;
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.method = method;
        this.rationale = rationale;
        this.permissionDenied = permissionDenied;

        String name = method.getName();
        this.fieldRequestCode = Constants.VAR_REQUEST_CODE_PREFIX + name.toUpperCase();
        this.fieldPermissions = Constants.VAR_PERMISSIONS_PREFIX + name.toUpperCase();
        this.classNameOfRequest = Character.toUpperCase(name.charAt(0)) + name.substring(1) +
                Constants.TYPE_REQUEST_SUFFIX;

        this.nonNull = ClassName.get(Constants.ANNOTATION_PACKAGE, Constants.TYPE_NONNULL);
        this.permissionUtils = ClassName.get(Constants.PERMISSION_PACKAGE, Constants.TYPE_UTILS);

        this.requestPermission = CodeBlock.builder()
                .add("$T.$N($N, $N, $N)",
                        ClassName.get(Constants.ANDROID_V4_APP, Constants.TYPE_ACTIVITY_COMPAT),
                        Constants.METHOD_REQUEST, Constants.VAR_TARGET, fieldPermissions, fieldRequestCode)
                .build();

        this.callTargetCode = CodeBlock.builder()
                .add("$L.$N()", Constants.VAR_TARGET, name)
                .build();

        if (rationale != null) {
            this.callRationaleCode = CodeBlock.builder()
                    .add("$L.$N()", Constants.VAR_TARGET, rationale.getName())
                    .build();
        } else {
            this.callRationaleCode = null;
        }

        if (permissionDenied != null) {
            this.callDeniedCode = CodeBlock.builder()
                    .add("$L.$N()", Constants.VAR_TARGET, permissionDenied.getName())
                    .build();
        } else {
            this.callDeniedCode = null;
        }

    }

    FieldSpec createFieldRequestCode() {
        return FieldSpec.builder(int.class, fieldRequestCode, PRIVATE, STATIC, FINAL)
                .initializer("$L", requestCode)
                .build();
    }

    FieldSpec createFieldPermissions() {
        CodeBlock.Builder permissionsBuilder = CodeBlock.builder().add("new $T[] {", String.class);
        for (int i = 0; i < permissions.length; i++) {
            if (i == 0) {
                permissionsBuilder.add("$S", permissions[i]);
            } else {
                permissionsBuilder.add(",$S", permissions[i]);
            }
        }
        CodeBlock code = permissionsBuilder.add("}").build();
        return FieldSpec.builder(String[].class, fieldPermissions, PRIVATE, STATIC, FINAL)
                .initializer(code)
                .build();
    }

    MethodSpec createMethodWithCheck() {
        CodeBlock.Builder statementBuilder = CodeBlock.builder()
                .addStatement("$T<$T> $L = $T.$N($N, $N)",
                        List.class, String.class, Constants.VAR_DENIED_FOREVER, permissionUtils,
                        Constants.METHOD_SHOULD_REQUEST, Constants.VAR_TARGET, fieldPermissions)
                .beginControlFlow("if ($L.$N())",
                        Constants.VAR_DENIED_FOREVER, Constants.METHOD_ISEMPTY)
                .addStatement(callTargetCode)
                .nextControlFlow("else");

        if (rationale != null) {
            statementBuilder.addStatement("$T<$T> $L = $T.$N($N, $N)", List.class, String.class,
                    Constants.VAR_DENIED, permissionUtils, Constants.METHOD_SHOULD_RATIONALE,
                    Constants.VAR_TARGET, fieldPermissions)
                    .beginControlFlow("if (!$L.$N())",
                            Constants.VAR_DENIED, Constants.METHOD_ISEMPTY)
                    .addStatement("$L.$N($N)", Constants.VAR_DENIED_FOREVER,
                            Constants.METHOD_REMOVEALL, Constants.VAR_DENIED)
                    .addStatement("$T $L = new $T($N, $N)",
                            classNameOfRequest, Constants.VAR_REQUEST, classNameOfRequest,
                            Constants.VAR_DENIED, Constants.VAR_DENIED_FOREVER)
                    .addStatement(callRationaleCode)
                    .nextControlFlow("else")
                    .addStatement(requestPermission)
                    .endControlFlow()
                    .endControlFlow();
        } else {
            statementBuilder.addStatement(requestPermission)
                    .endControlFlow();
        }
        return MethodSpec.methodBuilder(method.getName() + Constants.METHOD_REQUEST_SUFFIX)
                .addModifiers(STATIC)
                .addParameter(ParameterSpec.builder(targetTypeName, Constants.VAR_TARGET)
                        .addAnnotation(nonNull).build())
                .addCode(statementBuilder.build())
                .build();
    }

    TypeSpec createInterfaceRequest() {
        ClassName typeName = ClassName.get(
                Constants.PERMISSION_ANNOTATION_PACKAGE, Constants.TYPE_REQUEST);

        ParameterizedTypeName name = ParameterizedTypeName.get(typeName, targetTypeName);
        ParameterizedTypeName list = ParameterizedTypeName.get(List.class, String.class);

        FieldSpec denied = FieldSpec.builder(list, Constants.VAR_DENIED, PRIVATE, FINAL).build();
        FieldSpec deniedForever = FieldSpec.builder(list, Constants.VAR_DENIED_FOREVER,
                PRIVATE, FINAL).build();

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(PRIVATE)
                .addParameter(ParameterSpec.builder(list, Constants.VAR_DENIED)
                        .addAnnotation(nonNull).build())
                .addParameter(ParameterSpec.builder(list, Constants.VAR_DENIED_FOREVER)
                        .addAnnotation(nonNull).build())
                .addStatement("this.$N = $N", Constants.VAR_DENIED, Constants.VAR_DENIED)
                .addStatement("this.$N = $N", Constants.VAR_DENIED_FOREVER, Constants.VAR_DENIED_FOREVER)
                .build();

        MethodSpec proceed = MethodSpec.methodBuilder(Constants.METHOD_PROCEED)
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(ParameterSpec.builder(targetTypeName, Constants.VAR_TARGET)
                        .addAnnotation(nonNull).build())
                .addStatement(requestPermission)
                .build();
        MethodSpec.Builder cancelBuilder = MethodSpec.methodBuilder(Constants.METHOD_CANCEL)
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class);
        if (permissionDenied != null) {
            cancelBuilder.addStatement(callDeniedCode);
        }
        MethodSpec cancel = cancelBuilder.build();

        return TypeSpec.classBuilder(classNameOfRequest)
                .addModifiers(PRIVATE, STATIC, FINAL)
                .addSuperinterface(name)
                .addField(denied)
                .addField(deniedForever)
                .addMethod(constructor)
                .addMethod(proceed)
                .addMethod(cancel)
                .build();
    }

    CodeBlock createSwitchCase() {
        CodeBlock.Builder caseBuilder = CodeBlock.builder()
                .add("case $L:\n", requestCode)
                .indent()
                .beginControlFlow("if ($L.$N())",
                        Constants.VAR_DENIED_FOREVER, Constants.METHOD_ISEMPTY)
                .addStatement(callTargetCode);
        if (permissionDenied != null) {
            caseBuilder.nextControlFlow("else")
                    .addStatement("$T<$T> $L = $T.$N($N, $N)", List.class, String.class,
                            Constants.VAR_DENIED, permissionUtils, Constants.METHOD_SHOULD_RATIONALE,
                            Constants.VAR_TARGET, Constants.VAR_DENIED_FOREVER)
                    .addStatement("$L.$N($N)", Constants.VAR_DENIED_FOREVER,
                            Constants.METHOD_REMOVEALL, Constants.VAR_DENIED)
                    .addStatement(callDeniedCode);
        }
        return caseBuilder.endControlFlow()
                .addStatement("break")
                .unindent()
                .build();
    }

    static final class Builder {
        private final TypeName targetTypeName;
        private final int requestCode;
        private String[] permissions;
        private MethodInfo method;
        private MethodInfo rationale;
        private MethodInfo permissionDenied;

        Builder(TypeName targetTypeName, int requestCode) {
            this.requestCode = requestCode;
            this.targetTypeName = targetTypeName;
        }

        PermissionRequestSet build() {
            return new PermissionRequestSet(targetTypeName, requestCode, permissions, method,
                    rationale, permissionDenied);
        }

        boolean hasMethodBinding() {
            return method != null;
        }

        void addMethodBinding(String[] permissions, MethodInfo method) {
            this.permissions = permissions;
            this.method = method;
        }
    }
}
