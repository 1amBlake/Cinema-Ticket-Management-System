package com.cinema.validator;

import com.cinema.entity.JobTitle;

public final class JobTitleValidator {

    private JobTitleValidator() {
    }

    public static void validateForCreate(JobTitle jobTitle) {
        validateCommon(jobTitle);
    }

    public static void validateForUpdate(JobTitle jobTitle) {
        validateCommon(jobTitle);

        if (jobTitle.getJobTitleId() <= 0) {
            throw new IllegalArgumentException("jobTitleId phải lớn hơn 0!");
        }
    }

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

    private static void validateBusinessRule(JobTitle jobTitle) {
    }
}