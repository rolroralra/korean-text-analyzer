package com.kakaobank.tools.comment.analyzer;

import com.kakaobank.tools.comment.analyzer.csv.CsvReader;
import com.kakaobank.tools.comment.analyzer.csv.DefaultCsvReader;
import com.kakaobank.tools.comment.analyzer.csv.DefaultResultWriter;
import com.kakaobank.tools.comment.analyzer.csv.ResultWriter;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SchoolAnalyzer {
    // 전체 형태 패턴
    private static final Pattern FULL_PATTERN = Pattern.compile("([가-힣]+?(?:초등학교|중학교|고등학교))");
//    private static final Pattern FULL_PATTERN = Pattern.compile("([가-힣]+(?:초등학교|중학교|고등학교))");

    // 짧은 형태 패턴
    private static final Pattern SHORT_PATTERN = Pattern.compile("([가-힣]+?(?:초|중|고))");

    private static final String OUTPUT_FILE = "result.txt";

    private static final String INPUT_FILE = "comments.csv";

    private final Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

    private final CsvReader csvReader = new DefaultCsvReader();

    private final ResultWriter resultWriter = new DefaultResultWriter();

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

    private Set<String> extractSchoolsWithMorphology(String text) {
        if (text.isBlank()) {
            return Collections.emptySet();
        }

        Set<String> schools = new HashSet<>();

        KomoranResult result = komoran.analyze(text);

        log.info("원본        : {}", text);
        log.info("분석 결과 원본: {}", result.getPlainText());

        List<Token> tokens = result.getTokenList();

        for (Token token : tokens) {
            String word = token.getMorph();  // 형태소
            String pos = token.getPos();     // 품사

            // 고유명사(NNP) 또는 일반명사(NNG)이면서 학교 키워드 포함
            if ((pos.equals("NNP") || pos.equals("NNG")) &&
                (word.endsWith("초등학교") || word.endsWith("중학교") ||
                    word.endsWith("고등학교"))) {
                schools.add(word);
            }
        }

        return schools;
    }

    public Map<String, Integer> analyzeSchools(String inputFile) throws IOException, CsvException {
        // 1단계: 전체 형태의 학교명 수집
        Set<String> fullSchoolNames = new HashSet<>();
        List<String> allMessages = new ArrayList<>();

        List<String> messages = csvReader.readAllComments(inputFile);

        for (String message : messages) {
            Set<String> fullSchools = extractFullSchools(message);
            allMessages.add(message);
            fullSchoolNames.addAll(fullSchools);
        }

        log.info("전체 형태 학교명 {}개 발견", fullSchoolNames.size());

        // 정규화 맵 생성
        Map<String, String> normalizationMap = buildNormalizationMap(fullSchoolNames);
        log.info("학교명 정규화 매핑 {}개 생성", normalizationMap.size());

        // 짧은 형태에서 전체 형태로 변환 맵 생성
        Map<String, String> shortToFull = buildShortToFullMap(fullSchoolNames);
        log.info("짧은 형태 매핑 {}개 생성", shortToFull.size());

        // 2단계: 모든 메시지 재처리하여 카운팅
        Map<String, Integer> schoolCounts = new HashMap<>();
        int totalRows = allMessages.size();
        int rowsWithSchools = 0;

        for (String message : allMessages) {
            Set<String> schools = extractAndNormalizeSchools(message, shortToFull);
//            Set<String> schools = extractSchoolsWithMorphology(message);

            if (!schools.isEmpty()) {
                rowsWithSchools++;
                for (String school : schools) {
                    String cleanSchool = removeConsecutiveDuplicates(school);
                    // 정규화 적용
                    String normalizedSchool = normalizeSchoolName(cleanSchool, normalizationMap);
                    schoolCounts.merge(normalizedSchool, 1, Integer::sum);
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
    public Set<String> extractFullSchools(String text) {
        Set<String> schools = new HashSet<>();
        if (text == null || text.isEmpty()) {
            return schools;
        }

        // 연속된 같은 학교명을 하나로 처리
        // 예: "서울장평중학교서울장평중학교..." -> "서울장평중학교"
        Matcher matcher = FULL_PATTERN.matcher(text);

        while (matcher.find()) {
            String school = matcher.group(1);

            // 학교명 정리 (첫 번째 패턴만 사용)
            String cleanSchool = removeConsecutiveDuplicates(school);
            schools.add(cleanSchool);
        }

        return schools;
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

    /**
     * 학교명 정규화 맵 생성
     * 짧은 이름 -> 긴 이름(지역 포함)으로 매핑
     */
    private Map<String, String> buildNormalizationMap(Set<String> schoolNames) {
        Map<String, String> normalizationMap = new HashMap<>();

        // 학교명을 길이순으로 정렬 (긴 것부터)
        List<String> sortedNames = new ArrayList<>(schoolNames);
        sortedNames.sort(Comparator.comparingInt(String::length).reversed());

        for (int i = 0; i < sortedNames.size(); i++) {
            String longer = sortedNames.get(i);

            for (int j = i + 1; j < sortedNames.size(); j++) {
                String shorter = sortedNames.get(j);

                // 긴 이름이 짧은 이름을 포함하고 있으면
                // 짧은 이름을 긴 이름으로 매핑
                if (longer.contains(shorter)) {
                    // 이미 매핑되어 있지 않은 경우만 (가장 긴 것으로)
                    log.debug("학교명 매핑: {} -> {}", shorter, longer);
                    normalizationMap.putIfAbsent(shorter, longer);
                }
            }
        }

        return normalizationMap;
    }

    /**
     * 학교명 정규화 적용
     */
    private String normalizeSchoolName(String schoolName, Map<String, String> normalizationMap) {
        return normalizationMap.getOrDefault(schoolName, schoolName);
    }

    /**
     * 학교명 추출 및 정규화
     */
    private Set<String> extractAndNormalizeSchools(String text, Map<String, String> shortToFull) {
        Set<String> schools = new HashSet<>();
        if (text == null || text.isEmpty()) {
            return schools;
        }

        // 1. 전체 형태 추출 (중복 제거)
        Matcher fullMatcher = FULL_PATTERN.matcher(text);
        String lastFullSchool = null;
        int lastFullEnd = -1;

        while (fullMatcher.find()) {
            String school = fullMatcher.group(1);
            int currentStart = fullMatcher.start();

            // 이전 학교명과 같고 바로 붙어있으면 스킵
            if (school.equals(lastFullSchool) && currentStart == lastFullEnd) {
                lastFullEnd = fullMatcher.end();
                continue;
            }

            schools.add(school);
            lastFullSchool = school;
            lastFullEnd = fullMatcher.end();
        }

        // 2. 짧은 형태 추출 및 변환
        Matcher shortMatcher = SHORT_PATTERN.matcher(text);
        String lastShortSchool = null;
        int lastShortEnd = -1;

        while (shortMatcher.find()) {
            String shortName = shortMatcher.group(1);
            int currentStart = shortMatcher.start();

            // 이전 학교명과 같고 바로 붙어있으면 스킵
            if (shortName.equals(lastShortSchool) && currentStart == lastShortEnd) {
                lastShortEnd = shortMatcher.end();
                continue;
            }

            // 전체 형태가 아니고, 매핑이 존재하는 경우만 추가
            if (!schools.contains(shortName) && shortToFull.containsKey(shortName)) {
                String fullName = shortToFull.get(shortName);
                schools.add(fullName);
            }

            lastShortSchool = shortName;
            lastShortEnd = shortMatcher.end();
        }

        return schools;
    }

    public void writeResults(Map<String, Integer> schoolCounts, String outputFile) throws IOException {
        resultWriter.writeResults(schoolCounts, outputFile);
    }

    /**
     * 연속된 동일 학교명 제거
     * 예: "서울장평중학교서울장평중학교서울장평중학교" -> "서울장평중학교"
     */
    private String removeConsecutiveDuplicates(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // 원본 길이 체크
        if (text.length() > 100) {
            log.debug("긴 텍스트 발견: {}자", text.length());
        }

        // 초등학교, 중학교, 고등학교로 끝나는 패턴 찾기
        Matcher matcher = FULL_PATTERN.matcher(text);

        if (matcher.find()) {
            String result = matcher.group(1);

            // 정리 전후 로그
            if (!text.equals(result)) {
                log.debug("학교명 정리: {}자 -> {}자 ({})",
                    text.length(), result.length(), result);
            }
            return result;
        }

        log.debug("패턴 매칭 실패: {}", text.substring(0, Math.min(50, text.length())));

        return text;
    }
}