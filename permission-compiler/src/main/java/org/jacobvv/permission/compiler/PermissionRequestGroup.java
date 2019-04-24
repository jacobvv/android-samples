package org.jacobvv.permission.compiler;

import com.google.common.collect.ImmutableList;

/**
 * @author jacob
 * @date 19-4-24
 */
class PermissionRequestGroup {

    private PermissionRequestGroup() {

    }

    static final class Builder {
        private final int requestCode;

        private final ImmutableList.Builder<PermissionRequestGroup> requests =
                ImmutableList.builder();

        Builder(int requestCode) {
            this.requestCode = requestCode;
        }

        PermissionRequestGroup build() {
            return new PermissionRequestGroup();
        }
    }
}
