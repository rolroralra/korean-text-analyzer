package com.kakaobank.tools.comment.analyzer.name;

import java.util.Set;

public class SchoolNameValidator {

    private final Set<String> validSchoolNames = SchoolNameStorage.getSchoolNames();

    public boolean isValidSchoolName(String schoolName) {
        return validSchoolNames.contains(schoolName);
    }
}
