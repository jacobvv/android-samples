package org.jacobvv.permission.compiler;

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
    private final ParameterInfo returnValue;

    MethodInfo(String name, List<ParameterInfo> parameters) {
        this(name, parameters, null);
    }

    MethodInfo(String name, List<ParameterInfo> parameters, ParameterInfo returnValue) {
        ArrayList<ParameterInfo> params = new ArrayList<>(parameters);
        params.sort((p0, p1) -> p0.getPosition() - p1.getPosition());
        this.name = name;
        this.parameters = Collections.unmodifiableList(params);
        this.returnValue = returnValue;
    }

    String getName() {
        return name;
    }

    List<ParameterInfo> getParameters() {
        return parameters;
    }

    ParameterInfo getReturnValue() {
        return returnValue;
    }
}
