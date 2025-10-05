package com.kakaobank.tools.comment.analyzer.name;

public class SchoolNameShortenConverter {
    private SchoolNameShortenConverter() {
        throw new UnsupportedOperationException("SchoolNameShortenConverter should not be instantiated");
    }

    public static String convertShortenSchoolName(String schoolName) {
        if (schoolName.endsWith("대학")) {
            return schoolName.substring(0, schoolName.length() - 2) + "대학교";
        }
        if (schoolName.endsWith("초")) {
            return schoolName.substring(0, schoolName.length() - 1) + "초등학교";
        }
        if (schoolName.endsWith("중")) {
            return schoolName.substring(0, schoolName.length() - 1) + "중학교";
        }
        if (schoolName.endsWith("고")) {
            return schoolName.substring(0, schoolName.length() - 1) + "고등학교";
        }
        if (schoolName.endsWith("대")) {
            return schoolName.substring(0, schoolName.length() - 1) + "대학교";
        }
        return schoolName;
    }
}
