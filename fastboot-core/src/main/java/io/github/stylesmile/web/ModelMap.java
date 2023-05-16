//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.github.stylesmile.web;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModelMap extends LinkedHashMap<String, Object> {
    public ModelMap() {
    }

    public ModelMap(String attributeName, Object attributeValue) {
        this.addAttribute(attributeName, attributeValue);
    }

    public ModelMap(Object attributeValue) {
        this.addAttribute(attributeValue);
    }

    public ModelMap addAttribute(String attributeName, Object attributeValue) {
        //Assert.notNull(attributeName, "Model attribute name must not be null");
        this.put(attributeName, attributeValue);
        return this;
    }

    public ModelMap addAttribute(Object attributeValue) {
        // Assert.notNull(attributeValue, "Model object must not be null");
        return attributeValue instanceof Collection
                && ((Collection) attributeValue).isEmpty() ? this :
                this.addAttribute(Conventions.getVariableName(attributeValue), attributeValue);
    }

    public ModelMap addAllAttributes(Collection<?> attributeValues) {
        if (attributeValues != null) {
            Iterator var2 = attributeValues.iterator();

            while (var2.hasNext()) {
                Object attributeValue = var2.next();
                this.addAttribute(attributeValue);
            }
        }

        return this;
    }

    public ModelMap addAllAttributes(Map<String, ?> attributes) {
        if (attributes != null) {
            this.putAll(attributes);
        }

        return this;
    }

    public ModelMap mergeAttributes(Map<String, ?> attributes) {
        if (attributes != null) {
            attributes.forEach((key, value) -> {
                if (!this.containsKey(key)) {
                    this.put(key, value);
                }

            });
        }

        return this;
    }

    public boolean containsAttribute(String attributeName) {
        return this.containsKey(attributeName);
    }


    public Object getAttribute(String attributeName) {
        return this.get(attributeName);
    }
}
