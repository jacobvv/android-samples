package org.jacobvv.permission.compiler;

import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jacob
 * @date 19-4-25
 */
class MethodInfo {
    private final String name;
    private final List<ParameterInfo> parameters;
    private final TypeName returnValue;

    MethodInfo(String name) {
        this(name, TypeName.VOID);
    }

    MethodInfo(String name, TypeName returnValue) {
        this(name, returnValue, null);
    }

    MethodInfo(String name, TypeName returnValue, List<ParameterInfo> parameters) {
        if (parameters != null) {
            ArrayList<ParameterInfo> params = new ArrayList<>(parameters);
            params.sort((p0, p1) -> p0.getPosition() - p1.getPosition());
            this.parameters = Collections.unmodifiableList(params);
        } else {
            this.parameters = Collections.emptyList();
        }
        this.name = name;
        this.returnValue = returnValue;
    }

    String getName() {
        return name;
    }

    List<ParameterInfo> getParameters() {
        return parameters;
    }

    TypeName getReturnValue() {
        return returnValue;
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "name='" + name + '\'' +
                ", parameters=" + parameters +
                ", returnValue=" + returnValue +
                '}';
    }
}
