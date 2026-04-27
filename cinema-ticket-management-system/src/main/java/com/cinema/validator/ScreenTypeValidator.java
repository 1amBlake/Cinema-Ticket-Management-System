package com.cinema.validator;

import com.cinema.entity.ScreenType;

/**
 * Validator cho thực thể ScreenType.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng ScreenType
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Trọng (chính)
 * @author Minh Huy (sửa)
 */
public final class ScreenTypeValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp ScreenTypeValidator.
     */
    private ScreenTypeValidator() {
    }

    /**
     * Kiểm tra dữ liệu của ScreenType trong trường hợp thêm mới.
     *
     * @param screenType - Đối tượng ScreenType cần kiểm tra
     */
    public static void validateForCreate(ScreenType screenType) {
        validateCommon(screenType);
    }

    /**
     * Kiểm tra dữ liệu của ScreenType trong trường hợp cập nhật.
     *
     * @param screenType - Đối tượng ScreenType cần kiểm tra
     */
    public static void validateForUpdate(ScreenType screenType) {
        validateCommon(screenType);

        if (screenType.getScreenTypeId() <= 0) {
            throw new IllegalArgumentException("screenTypeId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể ScreenType.
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param screenType - Đối tượng ScreenType cần kiểm tra
     */
    private static void validateCommon(ScreenType screenType) {
        if (screenType == null) {
            throw new IllegalArgumentException("screenType không được null!");
        }

        if (screenType.getScreenTypeName() == null
                || screenType.getScreenTypeName().trim().isEmpty()) {
            throw new IllegalArgumentException("screenTypeName không được để trống!");
        }

        if (screenType.getScreenTypeName().trim().length() > 100) {
            throw new IllegalArgumentException("screenTypeName không được vượt quá 100 ký tự!");
        }

        validateBusinessRule(screenType);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của ScreenType.
     *
     * @param screenType - Đối tượng ScreenType cần kiểm tra
     */
    private static void validateBusinessRule(ScreenType screenType) {
    }
}