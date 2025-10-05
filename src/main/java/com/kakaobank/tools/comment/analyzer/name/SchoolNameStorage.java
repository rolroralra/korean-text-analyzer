package com.kakaobank.tools.comment.analyzer.name;

import com.kakaobank.tools.comment.analyzer.exception.CommentAnalyzeGlobalException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

public class SchoolNameStorage {

    public static final String SCHOOL_NAME_LIST_FILENAME = "school_name_list.txt";
    private static Set<String> schoolNames;

    private SchoolNameStorage() {
        throw new UnsupportedOperationException("SchoolNameStorage constructor is not supported");
    }

    /**
     * resources/school_name_list.txt 파일을 읽어서 Set으로 반환
     */
    private static Set<String> loadSchoolNames() {
        try (InputStream is = SchoolNameStorage.class.getClassLoader().getResourceAsStream(SCHOOL_NAME_LIST_FILENAME)) {
            if (is == null) {
                throw new CommentAnalyzeGlobalException(SCHOOL_NAME_LIST_FILENAME + " not found in resources");
            }

            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .collect(Collectors.toSet());
            }
        } catch (IOException e) {
            throw new CommentAnalyzeGlobalException("Failed to load school names", e);
        }
    }

    public static Set<String> getSchoolNames() {
        if  (schoolNames == null) {
            schoolNames = loadSchoolNames();
        }

        return schoolNames;
    }
}
