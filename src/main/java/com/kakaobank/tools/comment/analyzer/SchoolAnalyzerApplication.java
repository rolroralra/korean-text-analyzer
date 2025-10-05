package com.kakaobank.tools.comment.analyzer;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SchoolAnalyzerApplication {

    private static final String OUTPUT_FILE = "result.txt";

    private static final String INPUT_FILE = "comments.csv";

    public static void main(String[] args) {
        SchoolAnalyzerApplication.run(args);
    }

    public static void run(String[] args) {
        try {
            log.info("학교명 분석 시작");

            SchoolAnalyzer analyzer = new SchoolAnalyzer();

            Map<String, Long> schoolCounts = analyzer.analyzeSchools(INPUT_FILE);
            analyzer.writeResults(schoolCounts, OUTPUT_FILE);
        } catch (Exception e) {
            log.error("분석 중 오류 발생", e);
            System.exit(1);
        } finally {
            log.info("학교명 분석 완료");
        }
    }
}
