package com.kakaobank.tools.comment.analyzer.extract;

import java.util.Set;

public interface SchoolNameExtractor {
    Set<String> extractSchoolNames(String text);
}
