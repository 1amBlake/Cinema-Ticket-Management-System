package com.cinema.validator;

import com.cinema.entity.JobTitle;

/**
 * Validator cho thực thể JobTitle.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng JobTitle
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Quốc Anh (chính)
 * @author Minh Huy (sửa)
 */
public final class JobTitleValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp JobTitleValidator.
     */
    private JobTitleValidator() {
    }

    /**
     * Kiểm tra dữ liệu của JobTitle trong trường hợp thêm mới.
     *
     * @param jobTitle - Đối tượng JobTitle cần kiểm tra
     */
    public static void validateForCreate(JobTitle jobTitle) {
        validateCommon(jobTitle);
    }

    /**
     * Kiểm tra dữ liệu của JobTitle trong trường hợp cập nhật.
     *
     * @param jobTitle - Đối tượng JobTitle cần kiểm tra
     */
    public static void validateForUpdate(JobTitle jobTitle) {
        validateCommon(jobTitle);

        if (jobTitle.getJobTitleId() <= 0) {
            throw new IllegalArgumentException("jobTitleId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể JobTitle.
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param jobTitle - Đối tượng JobTitle cần kiểm tra
     */
    private static void validateCommon(JobTitle jobTitle) {
        if (jobTitle == null) {
            throw new IllegalArgumentException("jobTitle không được null!");
        }

        if (jobTitle.getJobTitleName() == null || jobTitle.getJobTitleName().trim().isEmpty()) {
            throw new IllegalArgumentException("jobTitleName không được để trống!");
        }

        if (jobTitle.getJobTitleName().trim().length() > 100) {
            throw new IllegalArgumentException("jobTitleName không được vượt quá 100 ký tự!");
        }

        validateBusinessRule(jobTitle);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của JobTitle.
     *
     * @param jobTitle - Đối tượng JobTitle cần kiểm tra
     */
    private static void validateBusinessRule(JobTitle jobTitle) {
    }
}