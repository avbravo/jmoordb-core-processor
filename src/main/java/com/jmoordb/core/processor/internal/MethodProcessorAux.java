package com.jmoordb.core.processor.internal;

import java.util.SortedMap;

public class MethodProcessorAux {
    private StringBuilder builder = new StringBuilder();
    private boolean firstParam = true;
    private boolean forInterface;

    public MethodProcessorAux forInterface() {
        forInterface = true;
        return this;
    }

    public MethodProcessorAux defineSignature(String accessModifier, boolean asStatic, String returnType) {
        builder.append(forInterface ? "" : accessModifier)
                .append(asStatic? " static ": " ")
                .append(returnType)
                .append(" ");
        return this;
    }

    public MethodProcessorAux name(String name) {
        builder.append(name)
                .append("(");
        return this;
    }

    public MethodProcessorAux addParam(String type, String identifier) {
        if (!firstParam) {
            builder.append(", ");
        } else {
            firstParam = false;
        }
        builder.append(type)
                .append(" ")
                .append(identifier);

        return this;
    }

    public MethodProcessorAux defineBody(String body) {
        if (forInterface) {
            throw new IllegalArgumentException("Interface cannot define a body");
        }
        builder.append(") {")
                .append(ClassProccessorAux.LINE_BREAK)
                .append(body)
                .append(ClassProccessorAux.LINE_BREAK)
                .append("}")
                .append(ClassProccessorAux.LINE_BREAK);
        return this;
    }

    public String end() {
        return forInterface ? ");" : builder.toString();
    }
}