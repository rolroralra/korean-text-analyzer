package com.kakaobank.tools.comment.analyzer.name;

import com.kakaobank.tools.comment.analyzer.exception.CommentAnalyzeGlobalException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class SchoolNameStorage {

    private static Set<String> schoolNames;

    private SchoolNameStorage() {
        throw new UnsupportedOperationException("SchoolNameStorage constructor is not supported");
    }

    /**
     * resources/school_name_list.txt 파일을 읽어서 Set으로 반환
     */
    private static Set<String> loadSchoolNames() {
        var resource = SchoolNameStorage.class.getResource("/school_name_list.txt");
        if (resource == null) {
            throw new CommentAnalyzeGlobalException("school_name_list.txt not found in resources");
        }

        try(var lines = Files.lines(Path.of(resource.toURI()))) {
            return lines.collect(Collectors.toSet());
        } catch (IOException | URISyntaxException e) {
            throw new CommentAnalyzeGlobalException(e);
        }
    }

    public static Set<String> getSchoolNames() {
        if  (schoolNames == null) {
            schoolNames = loadSchoolNames();
        }

        return schoolNames;
    }
}
