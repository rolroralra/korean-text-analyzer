package com.kakaobank.tools.comment.analyzer;

import com.kakaobank.tools.comment.analyzer.csv.CsvReader;
import com.kakaobank.tools.comment.analyzer.csv.ResultWriter;
import com.kakaobank.tools.comment.analyzer.extract.SchoolNameExtractor;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SchoolAnalyzer {

    private final CsvReader csvReader;

    private final ResultWriter resultWriter;

    private final SchoolNameExtractor schoolNameExtractor;

    public Map<String, Long> analyzeSchools(String inputFile) throws IOException, CsvException {
        // 1. Load comments from input file
        List<String> comments = csvReader.readAllComments(inputFile);

        AtomicInteger rowsWithSchools = new AtomicInteger();

        Map<Integer, Integer> commentStatistics = new HashMap<>();

        Map<String, Long> schoolCounts = comments.stream()
            .flatMap(comment -> {
                // 2. Extract school names from every comments
                Set<String> extractedSchoolNames = this.extractSchoolNames(comment);

                commentStatistics.put(extractedSchoolNames.size(), commentStatistics.getOrDefault(extractedSchoolNames.size(), 0) + 1);

                if (!extractedSchoolNames.isEmpty()) {
                    rowsWithSchools.getAndIncrement();
                }
                return extractedSchoolNames.stream();
            })
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Set<String> schoolNames = schoolCounts.keySet();

        // 3. Logging stats
        log.debug("학교 전체 목록: {}", schoolNames);
        log.info("발견된 고유 학교 이름 수: {}", schoolNames.size());
        log.info("전체 댓글 수: {}", comments.size());
        log.info("학교 이름이 포함된 댓글 수: {}", rowsWithSchools.get());

        commentStatistics.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry ->
                log.info("{}개 학교가 포함된 댓글: {}", entry.getKey(), entry.getValue()));

        return schoolCounts;
    }

    public Set<String> extractSchoolNames(String text) {
        return schoolNameExtractor.extractSchoolNames(text);
    }

    public void writeResults(Map<String, Long> schoolCounts, String outputFile) throws IOException {
        resultWriter.writeResults(schoolCounts, outputFile);
    }
}