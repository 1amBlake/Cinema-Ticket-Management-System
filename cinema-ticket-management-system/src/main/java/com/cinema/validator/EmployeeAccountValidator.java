package com.cinema.validator;

import com.cinema.entity.EmployeeAccount;

/**
 * Validator cho thực thể EmployeeAccount.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng EmployeeAccount
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Hải Anh (chính)
 * @author Minh Huy (sửa)
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
            throw new IllegalArgumentException("accountId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể EmployeeAccount.
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param employeeAccount - Đối tượng EmployeeAccount cần kiểm tra
     */
    private static void validateCommon(EmployeeAccount employeeAccount) {
        if (employeeAccount == null) {
            throw new IllegalArgumentException("employeeAccount không được null!");
        }

        if (employeeAccount.getEmployee() == null) {
            throw new IllegalArgumentException("employee không được null!");
        }

        if (employeeAccount.getEmployee().getEmployeeId() <= 0) {
            throw new IllegalArgumentException("employeeId phải lớn hơn 0!");
        }

        if (employeeAccount.getAccountName() == null || employeeAccount.getAccountName().trim().isEmpty()) {
            throw new IllegalArgumentException("accountName không được để trống!");
        }

        if (employeeAccount.getAccountName().trim().length() > 255) {
            throw new IllegalArgumentException("accountName không được vượt quá 255 ký tự!");
        }

        if (employeeAccount.getAccountPassword() == null || employeeAccount.getAccountPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("accountPassword không được để trống!");
        }

        if (employeeAccount.getAccountPassword().trim().length() > 255) {
            throw new IllegalArgumentException("accountPassword không được vượt quá 255 ký tự!");
        }

        if (employeeAccount.getAccountStatus() == null) {
            throw new IllegalArgumentException("accountStatus không được null!");
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