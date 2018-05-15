package com.blanink.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/26.
 * 产品属性
 */

public class OrderProductSpecification implements Serializable {
    private Attribute attribute;		// 产品属性编号
    private String attributeValue;		// 产品属性值
    public String attributeSearchType;//文本框type

    public String getAttributeSearchType() {
        return attributeSearchType;
    }

    public void setAttributeSearchType(String attributeSearchType) {
        this.attributeSearchType = attributeSearchType;
    }

    @Override
    public String toString() {
        return "OrderProductSpecification{" +
                "attribute=" + attribute +
                ", attributeValue='" + attributeValue + '\'' +
                ", attributeSearchType='" + attributeSearchType + '\'' +
                '}';
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
