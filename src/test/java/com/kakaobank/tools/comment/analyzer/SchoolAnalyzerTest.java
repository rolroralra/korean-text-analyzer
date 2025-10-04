package com.kakaobank.tools.comment.analyzer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SchoolAnalyzerTest {

    private SchoolAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new SchoolAnalyzer();
    }

    @Test
    void testExtractSchools_단일학교() {
        String text = "수원, 창현고 짜장면 먹고싶어요";
        Set<String> schools = analyzer.extractFullSchools(text);

        assertTrue(schools.isEmpty(), "창현고는 패턴에 맞지 않아 추출되지 않아야 함");
    }

    @Test
    void testExtractSchools_정확한패턴() {
        String text = "하양여자중학교 학생입니다";
        Set<String> schools = analyzer.extractFullSchools(text);

        assertEquals(1, schools.size());
        assertTrue(schools.contains("하양여자중학교"));
    }

    @Test
    void testExtractSchools_여러학교() {
        String text = "율현중학교와 영덕중학교 그리고 동대문중학교 학생들";
        Set<String> schools = analyzer.extractFullSchools(text);

        assertEquals(3, schools.size());
        assertTrue(schools.contains("율현중학교"));
        assertTrue(schools.contains("영덕중학교"));
        assertTrue(schools.contains("동대문중학교"));
    }

    @Test
    void testExtractSchools_초등학교() {
        String text = "서울초등학교 다니는 학생입니다";
        Set<String> schools = analyzer.extractFullSchools(text);

        assertEquals(1, schools.size());
        assertTrue(schools.contains("서울초등학교"));
    }

    @Test
    void testExtractSchools_고등학교() {
        String text = "문수고등학교 졸업생입니다";
        Set<String> schools = analyzer.extractFullSchools(text);

        assertEquals(1, schools.size());
        assertTrue(schools.contains("문수고등학교"));
    }

    @Test
    void testExtractSchools_빈문자열() {
        Set<String> schools = analyzer.extractFullSchools("");
        assertTrue(schools.isEmpty());
    }

    @Test
    void testExtractSchools_null() {
        Set<String> schools = analyzer.extractFullSchools(null);
        assertTrue(schools.isEmpty());
    }

    @Test
    void testExtractSchools_학교없음() {
        String text = "짜장면 먹고 싶어요";
        Set<String> schools = analyzer.extractFullSchools(text);
        assertTrue(schools.isEmpty());
    }

    @Test
    void testExtractSchools_중복학교() {
        String text = "율현중학교 학생입니다. 율현중학교는 좋은 학교입니다.";
        Set<String> schools = analyzer.extractFullSchools(text);

        assertEquals(1, schools.size(), "중복은 Set으로 제거됨");
        assertTrue(schools.contains("율현중학교"));
    }

    @Test
    void testWriteResults(@TempDir Path tempDir) throws IOException {
        Path outputFile = tempDir.resolve("test_result.txt");

        Map<String, Integer> schoolCounts = Map.of(
            "율현중학교", 183,
            "영덕중학교", 159,
            "동대문중학교", 80
        );

        analyzer.writeResults(schoolCounts, outputFile.toString());

        assertTrue(Files.exists(outputFile));

        List<String> lines = Files.readAllLines(outputFile);
        assertEquals(3, lines.size());

        assertEquals("율현중학교\t183", lines.get(0));
        assertEquals("영덕중학교\t159", lines.get(1));
        assertEquals("동대문중학교\t80", lines.get(2));
    }

    @Test
    void testWriteResults_정렬확인(@TempDir Path tempDir) throws IOException {
        Path outputFile = tempDir.resolve("test_result.txt");

        Map<String, Integer> schoolCounts = Map.of(
            "가중학교", 10,
            "나중학교", 50,
            "다중학교", 30
        );

        analyzer.writeResults(schoolCounts, outputFile.toString());

        List<String> lines = Files.readAllLines(outputFile);

        assertEquals("나중학교\t50", lines.get(0), "가장 높은 카운트가 첫번째");
        assertEquals("다중학교\t30", lines.get(1), "두번째 높은 카운트가 두번째");
        assertEquals("가중학교\t10", lines.get(2), "가장 낮은 카운트가 마지막");
    }

    @Test
    void testExtractSchools_복잡한문장() {
        String text = """
            경북 경산, 하양여자중학교
            
            안녕하세요 배달의 민족님
            저희 학교 전체가 짜장면을 갈구 합니다.
            """;

        Set<String> schools = analyzer.extractFullSchools(text);

        assertEquals(1, schools.size());
        assertTrue(schools.contains("하양여자중학교"));
    }
}