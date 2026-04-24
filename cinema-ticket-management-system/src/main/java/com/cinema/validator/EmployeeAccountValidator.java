package com.cinema.validator;

import com.cinema.entity.EmployeeAccount;

/**
 * Validator cho thực thể EmployeeAccount.
 * 
 * @author Hải Anh (chính)
 */
public final class EmployeeAccountValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp EmployeeAccountValidator.
     */
    private EmployeeAccountValidator() {
    }

    /**
     * Kiểm tra dữ liệu của EmployeeAccount trong trường hợp thêm mới.
     *
     * @param employeeAccount - Đối tượng EmployeeAccount cần kiểm tra
     */
    public static void validateForCreate(EmployeeAccount employeeAccount) {
        validateCommon(employeeAccount);
    }

    /**
     * Kiểm tra dữ liệu của EmployeeAccount trong trường hợp cập nhật.
     *
     * @param employeeAccount - Đối tượng EmployeeAccount cần kiểm tra
     */
    public static void validateForUpdate(EmployeeAccount employeeAccount) {
        validateCommon(employeeAccount);

        if (employeeAccount.getAccountId() <= 0) {
            throw new IllegalArgumentException("Mã tài khoản phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể EmployeeAccount.
     *
     * @param employeeAccount - Đối tượng EmployeeAccount cần kiểm tra
     */
    private static void validateCommon(EmployeeAccount employeeAccount) {
        if (employeeAccount == null) {
            throw new IllegalArgumentException("Tài khoản không được null!");
        }

        if (employeeAccount.getEmployee() == null) {
            throw new IllegalArgumentException("Nhân viên không được null!");
        }

        if (employeeAccount.getAccountName() == null || employeeAccount.getAccountName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên tài khoản không được để trống!");
        }

        if (employeeAccount.getAccountName().trim().length() > 255) {
            throw new IllegalArgumentException("tên tài khoản không được vượt quá 255 ký tự!");
        }

        if (employeeAccount.getAccountPassword() == null || employeeAccount.getAccountPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống!");
        }

        if (employeeAccount.getAccountPassword().trim().length() > 255) {
            throw new IllegalArgumentException("Mật khẩu không được vượt quá 255 ký tự!");
        }

        if (employeeAccount.getAccountStatus() == null) {
            throw new IllegalArgumentException("Trạng thái tài khoản không được null!");
        }

        validateBusinessRule(employeeAccount);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của EmployeeAccount.
     *
     * @param employeeAccount - Đối tượng EmployeeAccount cần kiểm tra
     */
    private static void validateBusinessRule(EmployeeAccount employeeAccount) {
    }
}