package com.kakaobank.tools.comment.analyzer;

import com.kakaobank.tools.comment.analyzer.csv.CsvReader;
import com.kakaobank.tools.comment.analyzer.csv.DefaultCsvReader;
import com.kakaobank.tools.comment.analyzer.csv.DefaultResultWriter;
import com.kakaobank.tools.comment.analyzer.csv.ResultWriter;
import com.kakaobank.tools.comment.analyzer.extract.KomoranSchoolNameExtractor;
import com.kakaobank.tools.comment.analyzer.extract.SchoolNameExtractor;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SchoolAnalyzer {
    private static final String OUTPUT_FILE = "result.txt";

    private static final String INPUT_FILE = "comments.csv";

    private final CsvReader csvReader = new DefaultCsvReader();

    private final ResultWriter resultWriter = new DefaultResultWriter();

    private final SchoolNameExtractor schoolNameExtractor = new KomoranSchoolNameExtractor();

    public static void main(String[] args) {
        log.info("학교명 분석 시작");

        SchoolAnalyzer analyzer = new SchoolAnalyzer();
        try {
            Map<String, Integer> schoolCounts = analyzer.analyzeSchools(INPUT_FILE);
            analyzer.writeResults(schoolCounts, OUTPUT_FILE);
            log.info("분석 완료. 총 {}개 학교 발견", schoolCounts.size());
        } catch (Exception e) {
            log.error("분석 중 오류 발생", e);
            System.exit(1);
        }
    }

    public Map<String, Integer> analyzeSchools(String inputFile) throws IOException, CsvException {
        // 1단계: 전체 형태의 학교명 수집
        List<String> comments = csvReader.readAllComments(inputFile);

        Set<String> fullSchoolNames = comments.stream()
            .flatMap(comment -> this.extractSchoolNames(comment).stream())
            .collect(Collectors.toSet());

        log.debug("학교 전체 목록: {}", fullSchoolNames);

        return analyzeSchoolCounts(fullSchoolNames, comments);
    }

    private Map<String, Integer> analyzeSchoolCounts(Set<String> fullSchoolNames,
        List<String> allMessages) {
        log.info("전체 형태 학교명 {}개 발견", fullSchoolNames.size());

        // 짧은 형태에서 전체 형태로 변환 맵 생성
        Map<String, String> shortToFull = buildShortToFullMap(fullSchoolNames);
        log.info("짧은 형태 매핑 {}개 생성", shortToFull.size());

        // 2단계: 모든 메시지 재처리하여 카운팅
        Map<String, Integer> schoolCounts = new HashMap<>();
        int totalRows = allMessages.size();
        int rowsWithSchools = 0;

        for (String message : allMessages) {
            Set<String> schools = this.extractSchoolNames(message);

            if (!schools.isEmpty()) {
                rowsWithSchools++;
                for (String school : schools) {
                    schoolCounts.merge(shortToFull.getOrDefault(school, school), 1, Integer::sum);
                }
            }
        }

        log.info("CSV 파일 읽기 완료");
        log.info("전체 댓글 수: {}", totalRows);
        log.info("학교명이 포함된 댓글 수: {}", rowsWithSchools);
        log.info("발견된 고유 학교 수: {}", schoolCounts.size());
        return schoolCounts;
    }

    /**
     * 전체 형태의 학교명만 추출
     */
    public Set<String> extractSchoolNames(String text) {
        return schoolNameExtractor.extractSchoolNames(text);
    }

    /**
     * 짧은 형태 -> 전체 형태 매핑 생성
     * 예: "율현중" -> "율현중학교"
     */
    private Map<String, String> buildShortToFullMap(Set<String> fullSchoolNames) {
        Map<String, String> shortToFull = new HashMap<>();

        for (String fullName : fullSchoolNames) {
            if (fullName.endsWith("초등학교")) {
                String shortName = fullName.replace("초등학교", "초");
                shortToFull.put(shortName, fullName);
            } else if (fullName.endsWith("중학교")) {
                String shortName = fullName.replace("중학교", "중");
                shortToFull.put(shortName, fullName);
            } else if (fullName.endsWith("고등학교")) {
                String shortName = fullName.replace("고등학교", "고");
                shortToFull.put(shortName, fullName);
            }
        }

        return shortToFull;
    }

    public void writeResults(Map<String, Integer> schoolCounts, String outputFile) throws IOException {
        resultWriter.writeResults(schoolCounts, outputFile);
    }
}