package com.avbravo.jmoordb.core.processor.internal;

import java.util.SortedMap;

public class MethodProcessor {
    private StringBuilder builder = new StringBuilder();
    private boolean firstParam = true;
    private boolean forInterface;

    public MethodProcessor forInterface() {
        forInterface = true;
        return this;
    }

    public MethodProcessor defineSignature(String accessModifier, boolean asStatic, String returnType) {
        builder.append(forInterface ? "" : accessModifier)
                .append(asStatic? " static ": " ")
                .append(returnType)
                .append(" ");
        return this;
    }

    public MethodProcessor name(String name) {
        builder.append(name)
                .append("(");
        return this;
    }

    public MethodProcessor addParam(String type, String identifier) {
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

    public MethodProcessor defineBody(String body) {
        if (forInterface) {
            throw new IllegalArgumentException("Interface cannot define a body");
        }
        builder.append(") {")
                .append(ClassProccessor.LINE_BREAK)
                .append(body)
                .append(ClassProccessor.LINE_BREAK)
                .append("}")
                .append(ClassProccessor.LINE_BREAK);
        return this;
    }

    public String end() {
        return forInterface ? ");" : builder.toString();
    }
}