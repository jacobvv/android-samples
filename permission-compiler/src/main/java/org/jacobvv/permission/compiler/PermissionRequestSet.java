package org.jacobvv.permission.compiler;

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
