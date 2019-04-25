package org.jacobvv.permission.compiler;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.google.auto.common.MoreElements.getPackage;

/**
 * @author jacob
 * @date 19-4-24
 */
public class PermissionSet {

    private PermissionSet(TypeName targetTypeName, ClassName bindingClassName,
                          ImmutableList<PermissionRequestGroup> requests) {
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

    static final class Builder {
        private final TypeName targetTypeName;
        private final ClassName permissionClassName;

        private final Map<Integer, PermissionRequestGroup.Builder> requestsMap = new HashMap<>();

        private Builder(TypeName targetTypeName, ClassName permissionClassName) {
            this.targetTypeName = targetTypeName;
            this.permissionClassName = permissionClassName;
        }

        boolean addRequest(int requestCode, String[] permissions) {
            return true;
        }

        private PermissionRequestGroup.Builder getOrCreateViewBindings(int requestCode) {
            PermissionRequestGroup.Builder request = requestsMap.get(requestCode);
            if (request == null) {
                request = new PermissionRequestGroup.Builder(requestCode);
                requestsMap.put(requestCode, request);
            }
            return request;
        }

        PermissionSet build() {
            ImmutableList.Builder<PermissionRequestGroup> requests = ImmutableList.builder();
            for (PermissionRequestGroup.Builder builder : requestsMap.values()) {
                requests.add(builder.build());
            }
            return new PermissionSet(targetTypeName, permissionClassName, requests.build());
        }

        public boolean addMethod(int requestCode, String[] permissions, MethodInfo method) {
            return false;
        }
    }
}
