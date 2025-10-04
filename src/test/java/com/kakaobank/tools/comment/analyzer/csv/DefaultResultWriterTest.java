package com.kakaobank.tools.comment.analyzer.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DefaultResultWriterTest {
    private DefaultResultWriter resultWriter;

    @BeforeEach
    void setUp() {
        resultWriter = new DefaultResultWriter();
    }

    @Test
    void writeResults_정상_작성(@TempDir Path tempDir) throws IOException {
        // given
        Map<String, Integer> schoolCounts = Map.of(
            "율현중학교", 183,
            "영덕중학교", 159,
            "동대문중학교", 80
        );
        Path outputFile = tempDir.resolve("result.txt");

        // when
        resultWriter.writeResults(schoolCounts, outputFile.toString());

        // then
        assertTrue(Files.exists(outputFile));
        List<String> lines = Files.readAllLines(outputFile);
        assertEquals(3, lines.size());
    }

    @Test
    void writeResults_카운트_내림차순_정렬(@TempDir Path tempDir) throws IOException {
        // given
        Map<String, Integer> schoolCounts = Map.of(
            "가중학교", 10,
            "나중학교", 50,
            "다중학교", 30
        );
        Path outputFile = tempDir.resolve("sorted.txt");

        // when
        resultWriter.writeResults(schoolCounts, outputFile.toString());

        // then
        List<String> lines = Files.readAllLines(outputFile);
        assertEquals("나중학교\t50", lines.get(0));
        assertEquals("다중학교\t30", lines.get(1));
        assertEquals("가중학교\t10", lines.get(2));
    }

    @Test
    void writeResults_탭으로_구분(@TempDir Path tempDir) throws IOException {
        // given
        Map<String, Integer> schoolCounts = Map.of("율현중학교", 183);
        Path outputFile = tempDir.resolve("tab.txt");

        // when
        resultWriter.writeResults(schoolCounts, outputFile.toString());

        // then
        String content = Files.readString(outputFile);
        assertTrue(content.contains("\t"));
        String[] parts = content.trim().split("\t");
        assertEquals(2, parts.length);
        assertEquals("율현중학교", parts[0]);
        assertEquals("183", parts[1]);
    }

    @Test
    void writeResults_UTF8_인코딩(@TempDir Path tempDir) throws IOException {
        // given
        Map<String, Integer> schoolCounts = Map.of(
            "서울장평중학교", 100,
            "대구경북대학교사범대학부설중학교", 50
        );
        Path outputFile = tempDir.resolve("utf8.txt");

        // when
        resultWriter.writeResults(schoolCounts, outputFile.toString());

        // then
        List<String> lines = Files.readAllLines(outputFile);
        assertTrue(lines.stream().anyMatch(line -> line.contains("서울장평중학교")));
        assertTrue(lines.stream().anyMatch(line -> line.contains("대구경북대학교사범대학부설중학교")));
    }

    @Test
    void writeResults_빈_맵(@TempDir Path tempDir) throws IOException {
        // given
        Map<String, Integer> schoolCounts = new HashMap<>();
        Path outputFile = tempDir.resolve("empty.txt");

        // when
        resultWriter.writeResults(schoolCounts, outputFile.toString());

        // then
        assertTrue(Files.exists(outputFile));
        List<String> lines = Files.readAllLines(outputFile);
        assertTrue(lines.isEmpty());
    }

    @Test
    void writeResults_대용량_데이터(@TempDir Path tempDir) throws IOException {
        // given
        Map<String, Integer> schoolCounts = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            schoolCounts.put("학교" + i, 1000 - i);
        }
        Path outputFile = tempDir.resolve("large.txt");

        // when
        resultWriter.writeResults(schoolCounts, outputFile.toString());

        // then
        List<String> lines = Files.readAllLines(outputFile);
        assertEquals(1000, lines.size());

        // 첫 줄이 가장 높은 카운트
        assertTrue(lines.get(0).contains("학교0"));
        assertTrue(lines.get(0).contains("1000"));
    }

    @Test
    void writeResults_동일_카운트_처리(@TempDir Path tempDir) throws IOException {
        // given
        Map<String, Integer> schoolCounts = Map.of(
            "가중학교", 100,
            "나중학교", 100,
            "다중학교", 100
        );
        Path outputFile = tempDir.resolve("same_count.txt");

        // when
        resultWriter.writeResults(schoolCounts, outputFile.toString());

        // then
        List<String> lines = Files.readAllLines(outputFile);
        assertEquals(3, lines.size());
        assertTrue(lines.stream().allMatch(line -> line.endsWith("\t100")));
    }

    @Test
    void writeResults_파일_덮어쓰기(@TempDir Path tempDir) throws IOException {
        // given
        Path outputFile = tempDir.resolve("overwrite.txt");
        Files.writeString(outputFile, "기존 내용");

        Map<String, Integer> schoolCounts = Map.of("율현중학교", 50);

        // when
        resultWriter.writeResults(schoolCounts, outputFile.toString());

        // then
        String content = Files.readString(outputFile);
        assertFalse(content.contains("기존 내용"));
        assertTrue(content.contains("율현중학교"));
    }

    @Test
    void writeResults_특수문자_학교명(@TempDir Path tempDir) throws IOException {
        // given
        Map<String, Integer> schoolCounts = Map.of(
            "서울(강남)중학교", 10,
            "부산-해운대중학교", 20
        );
        Path outputFile = tempDir.resolve("special.txt");

        // when
        resultWriter.writeResults(schoolCounts, outputFile.toString());

        // then
        List<String> lines = Files.readAllLines(outputFile);
        assertTrue(lines.stream().anyMatch(line -> line.contains("서울(강남)중학교")));
        assertTrue(lines.stream().anyMatch(line -> line.contains("부산-해운대중학교")));
    }

    @Test
    void writeResults_각_줄_개행문자_확인(@TempDir Path tempDir) throws IOException {
        // given
        Map<String, Integer> schoolCounts = Map.of(
            "율현중학교", 100,
            "영덕중학교", 50
        );
        Path outputFile = tempDir.resolve("newline.txt");

        // when
        resultWriter.writeResults(schoolCounts, outputFile.toString());

        // then
        List<String> lines = Files.readAllLines(outputFile);
        assertEquals(2, lines.size());
        assertFalse(lines.get(0).contains("\n"));
        assertFalse(lines.get(1).contains("\n"));
    }

    @Test
    void writeResults_파일_생성_디렉토리_없음(@TempDir Path tempDir) {
        // given
        Map<String, Integer> schoolCounts = Map.of("율현중학교", 100);
        Path outputFile = tempDir.resolve("subdir/result.txt");

        // when & then
        // 디렉토리가 없으면 예외 발생
        assertThrows(IOException.class, () ->
            resultWriter.writeResults(schoolCounts, outputFile.toString()));
    }

    @Test
    void writeResults_상위10개_로깅_확인(@TempDir Path tempDir) throws IOException {
        // given
        Map<String, Integer> schoolCounts = new HashMap<>();
        for (int i = 0; i < 20; i++) {
            schoolCounts.put("학교" + i, 100 - i);
        }
        Path outputFile = tempDir.resolve("top10.txt");

        // when
        resultWriter.writeResults(schoolCounts, outputFile.toString());

        // then
        // 로그 출력은 상위 10개만 (실제 로그 확인은 수동)
        List<String> lines = Files.readAllLines(outputFile);
        assertEquals(20, lines.size());
    }
}