package com.kakaobank.tools.comment.analyzer;

import static org.assertj.core.api.Assertions.assertThat;
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
    void testExtractSchools_정확한패턴() {
        String text = "하양여자중학교 학생입니다";
        Set<String> schools = analyzer.extractSchoolNames(text);

        assertThat(schools)
            .hasSize(1)
            .containsExactly("하양여자중학교");
    }

    @Test
    void testExtractSchools_여러학교() {
        String text = "율현중학교와 영덕중학교 그리고 동대문중학교 학생들";
        Set<String> schools = analyzer.extractSchoolNames(text);

        assertThat(schools)
            .hasSize(3)
            .containsExactly("율현중학교", "영덕중학교", "동대문중학교");
    }

    @Test
    void testExtractSchools_여러학교_띄어쓰지않고_중첩() {
        String text = "율현중학교율현중학교율현중학교율현중학교율현중학교율현중학교율현중학교영덕중학교영덕중학교영덕중학교동대문중학교동대문중학교";
        Set<String> schools = analyzer.extractSchoolNames(text);

        assertThat(schools)
            .hasSize(3)
            .containsExactly("율현중학교", "영덕중학교", "동대문중학교");
    }

    @Test
    void testExtractSchools_초등학교() {
        String text = "서울양정초등학교 다니는 학생입니다";
        Set<String> schools = analyzer.extractSchoolNames(text);

        assertThat(schools)
            .hasSize(1)
            .containsExactly("양정초등학교");
    }

    @Test
    void testExtractSchools_고등학교() {
        String text = "문수고등학교 졸업생입니다";
        Set<String> schools = analyzer.extractSchoolNames(text);

        assertThat(schools)
            .hasSize(1)
            .containsExactly("문수고등학교");
    }

    @Test
    void testExtractSchools_빈문자열() {
        Set<String> schools = analyzer.extractSchoolNames("");
        assertThat(schools).isEmpty();
    }

    @Test
    void testExtractSchools_null() {
        Set<String> schools = analyzer.extractSchoolNames(null);
        assertThat(schools).isEmpty();
    }

    @Test
    void testExtractSchools_학교없음() {
        String text = "짜장면 먹고 싶어요";
        Set<String> schools = analyzer.extractSchoolNames(text);
        assertThat(schools).isEmpty();
    }

    @Test
    void testExtractSchools_유효하지_않은_학교_이름() {
        String text = "뉴진스고등학교 학생인데, BTS팬입니다.";
        Set<String> schools = analyzer.extractSchoolNames(text);
        assertThat(schools).isEmpty();
    }

    @Test
    void testExtractSchools_중복학교() {
        String text = "율현중학교 학생입니다. 율현중학교는 좋은 학교입니다.";
        Set<String> schools = analyzer.extractSchoolNames(text);

        assertThat(schools)
            .hasSize(1)
            .containsExactly("율현중학교");
    }

    @Test
    void testWriteResults(@TempDir Path tempDir) throws IOException {
        Path outputFile = tempDir.resolve("test_result.txt");

        Map<String, Long> schoolCounts = Map.of(
            "율현중학교", 183L,
            "영덕중학교", 159L,
            "동대문중학교", 80L
        );

        analyzer.writeResults(schoolCounts, outputFile.toString());

        assertTrue(Files.exists(outputFile));

        List<String> lines = Files.readAllLines(outputFile);
        assertThat(lines)
            .hasSize(3)
            .containsExactly("율현중학교\t183", "영덕중학교\t159", "동대문중학교\t80");
    }

    @Test
    void testWriteResults_정렬확인(@TempDir Path tempDir) throws IOException {
        Path outputFile = tempDir.resolve("test_result.txt");

        Map<String, Long> schoolCounts = Map.of(
            "가중학교", 10L,
            "나중학교", 50L,
            "다중학교", 30L
        );

        analyzer.writeResults(schoolCounts, outputFile.toString());

        List<String> lines = Files.readAllLines(outputFile);

        assertThat(lines)
            .hasSize(3)
            .containsExactly("나중학교\t50", "다중학교\t30", "가중학교\t10");
    }

    @Test
    void testExtractSchools_복잡한문장() {
        String text = """
            경북 경산, 하양여자중학교
            
            안녕하세요 배달의 민족님
            저희 학교 전체가 짜장면을 갈구 합니다.
            """;

        Set<String> schools = analyzer.extractSchoolNames(text);
        assertThat(schools)
            .hasSize(1)
            .containsExactly("하양여자중학교");
    }
}