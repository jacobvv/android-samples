package org.jacobvv.permission.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * @author jacob
 * @date 19-4-24
 */
class PermissionRequestSet {

    private final int requestCode;
    private final String[] permissions;
    private final MethodInfo method;
    private final MethodInfo rationale;
    private final MethodInfo permissionDenied;

    private PermissionRequestSet(int requestCode, String[] permissions, MethodInfo method,
                                 MethodInfo rationale, MethodInfo permissionDenied) {
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.method = method;
        this.rationale = rationale;
        this.permissionDenied = permissionDenied;
    }

    FieldSpec createFieldRequestCode() {
        return null;
    }

    FieldSpec createFieldPermissions() {
        return null;
    }

    MethodSpec createMethodWithPermission() {
        return null;
    }

    TypeSpec createInterfaceRequest() {
        return null;
    }

    CodeBlock createSwitchCase() {
        return null;
    }

    static final class Builder {
        private final int requestCode;
        private String[] permissions;
        private MethodInfo method;
        private MethodInfo rationale;
        private MethodInfo permissionDenied;

        Builder(int requestCode) {
            this.requestCode = requestCode;
        }

        PermissionRequestSet build() {
            return new PermissionRequestSet(requestCode, permissions, method,
                    rationale, permissionDenied);
        }

        public boolean hasMethodBinding() {
            return method != null;
        }

        public void addMethodBinding(String[] permissions, MethodInfo method) {
            this.permissions = permissions;
            this.method = method;
        }
    }
}
