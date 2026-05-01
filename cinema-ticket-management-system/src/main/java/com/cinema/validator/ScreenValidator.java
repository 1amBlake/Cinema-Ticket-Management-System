package com.cinema.validator;

import com.cinema.entity.Screen;

/**
 * Validator cho thực thể Screen.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng Screen
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Quốc Anh (chính)
 * @author Minh Huy (sửa)
 */
public final class ScreenValidator {

    private static final int MAX_SCREEN_NAME_LENGTH = 255;

    /*
     * Cho phép:
     * - Chữ tiếng Việt / tiếng Anh
     * - Số
     * - Khoảng trắng
     * - Dấu gạch ngang, gạch dưới
     * 
     * Ví dụ hợp lệ:
     * - Phòng 1
     * - Phòng A
     * - IMAX-01
     * - Room_A
     */
    private static final String SCREEN_NAME_REGEX = "[\\p{L}\\p{N}\\s_-]+";

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp ScreenValidator.
     */
    private ScreenValidator() {
    }

    /**
     * Kiểm tra dữ liệu của Screen trong trường hợp thêm mới.
     *
     * @param screen - Đối tượng Screen cần kiểm tra
     */
    public static void validateForCreate(Screen screen) {
        validateCommon(screen);
    }

    /**
     * Kiểm tra dữ liệu của Screen trong trường hợp cập nhật.
     *
     * @param screen - Đối tượng Screen cần kiểm tra
     */
    public static void validateForUpdate(Screen screen) {
        validateCommon(screen);

        if (screen.getScreenId() <= 0) {
            throw new IllegalArgumentException("screenId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể Screen.
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param screen - Đối tượng Screen cần kiểm tra
     */
    private static void validateCommon(Screen screen) {
        if (screen == null) {
            throw new IllegalArgumentException("screen không được null!");
        }

        if (screen.getScreenName() == null || screen.getScreenName().trim().isEmpty()) {
            throw new IllegalArgumentException("screenName không được để trống!");
        }

        if (screen.getScreenName().trim().length() > MAX_SCREEN_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    "screenName không được vượt quá " + MAX_SCREEN_NAME_LENGTH + " ký tự!"
            );
        }

        if (screen.getTheater() == null) {
            throw new IllegalArgumentException("theater không được null!");
        }

        if (screen.getTheater().getTheaterId() <= 0) {
            throw new IllegalArgumentException("theaterId phải lớn hơn 0!");
        }

        if (screen.getScreenType() == null) {
            throw new IllegalArgumentException("screenType không được null!");
        }

        if (screen.getScreenType().getScreenTypeId() <= 0) {
            throw new IllegalArgumentException("screenTypeId phải lớn hơn 0!");
        }

        if (screen.getScreenStatus() == null) {
            throw new IllegalArgumentException("screenStatus không được null!");
        }

        validateBusinessRule(screen);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của Screen.
     *
     * Quy ước nghiệp vụ:
     * - Tên phòng chiếu chỉ nên chứa chữ cái, chữ số, khoảng trắng,
     *   dấu gạch ngang hoặc dấu gạch dưới.
     * - ScreenStatus chỉ biểu diễn trạng thái vận hành của phòng chiếu,
     *   không biểu diễn trạng thái của suất chiếu.
     *
     * @param screen - Đối tượng Screen cần kiểm tra
     */
    private static void validateBusinessRule(Screen screen) {
        String screenName = screen.getScreenName().trim();

        if (!screenName.matches(SCREEN_NAME_REGEX)) {
            throw new IllegalArgumentException(
                    "screenName chỉ được chứa chữ cái, chữ số, khoảng trắng, "
                    + "dấu gạch ngang hoặc dấu gạch dưới!"
            );
        }
    }
}
