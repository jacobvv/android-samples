package org.jacobvv.permission.compiler;

import com.squareup.javapoet.TypeName;

/**
 * Represents a parameter type and its position in the method.
 *
 * @author jacob
 * @date 19-4-25
 */
public class ParameterInfo {

    private final int position;
    private final TypeName type;

    ParameterInfo(int position, TypeName type) {
        this.position = position;
        this.type = type;
    }

    int getPosition() {
        return position;
    }

    TypeName getType() {
        return type;
    }

    public boolean requiresCast(String toType) {
        return !type.toString().equals(toType);
    }
}
