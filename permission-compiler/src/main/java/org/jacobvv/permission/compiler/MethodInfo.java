package org.jacobvv.permission.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jacob
 * @date 19-4-25
 */
public class MethodInfo {
    private final String name;
    private final List<ParameterInfo> parameters;
    private final ParameterInfo returnValue;

    MethodInfo(String name, List<ParameterInfo> parameters) {
        this(name, parameters, null);
    }

    MethodInfo(String name, List<ParameterInfo> parameters, ParameterInfo returnValue) {
        this.name = name;
        this.parameters = Collections.unmodifiableList(new ArrayList<>(parameters));
        this.returnValue = returnValue;
    }

    public String getName() {
        return name;
    }

    public List<ParameterInfo> getParameters() {
        return parameters;
    }

    public ParameterInfo getReturnValue() {
        return returnValue;
    }
}
