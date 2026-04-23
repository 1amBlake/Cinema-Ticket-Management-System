package com.cinema.validator;

import main.java.com.cinema.entity.ProductType;


public final class ProductTypeValidator {


    private ProductTypeValidator() {
    }


    public static void validateForCreate(ProductType productType) {
        validateCommon(productType);
    }


    public static void validateForUpdate(ProductType productType) {
        validateCommon(productType);

        if (productType.getProductTypeId() <= 0) {
            throw new IllegalArgumentException("productTypeId phải lớn hơn 0!");
        }
    }


    private static void validateCommon(ProductType productType) {
        if (productType == null) {
            throw new IllegalArgumentException("productType không được null!");
        }

        if (productType.getProductTypeName() == null 
                || productType.getProductTypeName().trim().isEmpty()) {
            throw new IllegalArgumentException("productTypeName không được để trống!");
        }

        if (productType.getProductTypeName().trim().length() > 255) {
            throw new IllegalArgumentException("productTypeName không được vượt quá 255 ký tự!");
        }

        validateBusinessRule(productType);
    }


    private static void validateBusinessRule(ProductType productType) {
    }
}