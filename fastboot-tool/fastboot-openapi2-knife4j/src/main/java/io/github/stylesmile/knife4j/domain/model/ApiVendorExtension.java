package io.github.stylesmile.knife4j.domain.model;

import java.io.Serializable;

/**
 * 接口供应商扩展
 */
public interface ApiVendorExtension<T> extends Serializable {
    String getName();

    T getValue();
}
