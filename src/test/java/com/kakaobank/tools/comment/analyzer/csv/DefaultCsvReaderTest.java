package com.kakaobank.tools.comment.analyzer.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DefaultCsvReaderTest {
    private DefaultCsvReader csvReader;

    @BeforeEach
    void setUp() {
        csvReader = new DefaultCsvReader();
    }

    @Test
    void streamAllComments_정상적인_CSV_파일_읽기(@TempDir Path tempDir) throws IOException, CsvException {
        // given
        Path csvFile = tempDir.resolve("test.csv");
        String csvContent = """
                "첫번째 댓글"
                "두번째 댓글"
                "세번째 댓글"
                """;
        Files.writeString(csvFile, csvContent);

        // when
        List<String> comments = csvReader.streamAllComments(csvFile.toString()).toList();

        // then
        assertEquals(3, comments.size());
        assertEquals("첫번째 댓글", comments.get(0));
        assertEquals("두번째 댓글", comments.get(1));
        assertEquals("세번째 댓글", comments.get(2));
    }

    @Test
    void streamAllComments_여러줄_댓글_읽기(@TempDir Path tempDir) throws IOException, CsvException {
        // given
        Path csvFile = tempDir.resolve("multiline.csv");
        String csvContent = """
                "첫번째 댓글은
                여러 줄로
                되어있음"
                "두번째 댓글"
                """;
        Files.writeString(csvFile, csvContent);

        // when
        List<String> comments = csvReader.streamAllComments(csvFile.toString()).toList();

        // then
        assertEquals(2, comments.size());
        assertTrue(comments.get(0).contains("여러 줄로"));
    }

    @Test
    void streamAllComments_빈_행_제외(@TempDir Path tempDir) throws IOException, CsvException {
        // given
        Path csvFile = tempDir.resolve("empty.csv");
        String csvContent = """
                "정상 댓글"
                ""
                "또 다른 댓글"
                """;
        Files.writeString(csvFile, csvContent);

        // when
        List<String> comments = csvReader.streamAllComments(csvFile.toString()).toList();

        // then
        assertEquals(2, comments.size());
        assertEquals("정상 댓글", comments.get(0));
        assertEquals("또 다른 댓글", comments.get(1));
    }

    @Test
    void streamAllComments_null_값_제외(@TempDir Path tempDir) throws IOException, CsvException {
        // given
        Path csvFile = tempDir.resolve("null.csv");
        String csvContent = """
                "정상 댓글"
                
                "또 다른 댓글"
                """;
        Files.writeString(csvFile, csvContent);

        // when
        List<String> comments = csvReader.streamAllComments(csvFile.toString()).toList();

        // then
        assertTrue(comments.size() >= 2);
        assertTrue(comments.stream().noneMatch(c -> c == null || c.isEmpty()));
    }

    @Test
    void streamAllComments_파일_없음_예외_발생() {
        // when & then
        assertThrows(IOException.class,
            () -> csvReader.streamAllComments("nonexistent.csv").toList());
    }

    @Test
    void streamAllComments_학교명_포함_댓글(@TempDir Path tempDir) throws IOException, CsvException {
        // given
        Path csvFile = tempDir.resolve("school.csv");
        String csvContent = """
                "율현중학교 학생입니다"
                "영덕중학교와 동대문중학교 좋아요"
                "짜장면 먹고 싶어요"
                """;
        Files.writeString(csvFile, csvContent);

        // when
        List<String> comments = csvReader.streamAllComments(csvFile.toString()).toList();

        // then
        assertEquals(3, comments.size());
        assertTrue(comments.get(0).contains("율현중학교"));
        assertTrue(comments.get(1).contains("영덕중학교"));
        assertFalse(comments.get(2).contains("중학교"));
    }

    @Test
    void streamAllComments_특수문자_포함_댓글(@TempDir Path tempDir) throws IOException, CsvException {
        // given
        Path csvFile = tempDir.resolve("special.csv");
        String csvContent = """
                "댓글에 ""따옴표"" 포함"
                "콤마, 세미콜론; 포함"
                "이모지 😀 포함"
                """;
        Files.writeString(csvFile, csvContent);

        // when
        List<String> comments = csvReader.streamAllComments(csvFile.toString()).toList();

        // then
        assertEquals(3, comments.size());
        assertTrue(comments.get(0).contains("따옴표"));
        assertTrue(comments.get(1).contains("콤마"));
    }

    @Test
    void readAllComments_리스트로_반환(@TempDir Path tempDir) throws IOException, CsvException {
        // given
        Path csvFile = tempDir.resolve("list.csv");
        String csvContent = """
                "댓글1"
                "댓글2"
                "댓글3"
                """;
        Files.writeString(csvFile, csvContent);

        // when
        List<String> comments = csvReader.readAllComments(csvFile.toString());

        // then
        assertNotNull(comments);
        assertEquals(3, comments.size());
        assertInstanceOf(List.class, comments);
    }

    @Test
    void streamAllComments_대용량_파일_처리(@TempDir Path tempDir) throws IOException, CsvException {
        // given
        Path csvFile = tempDir.resolve("large.csv");
        StringBuilder csvContent = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            csvContent.append("\"댓글 ").append(i).append("\"\n");
        }
        Files.writeString(csvFile, csvContent.toString());

        // when
        long count = csvReader.streamAllComments(csvFile.toString()).count();

        // then
        assertEquals(1000, count);
    }

    @Test
    void streamAllComments_UTF8_인코딩_처리(@TempDir Path tempDir) throws IOException, CsvException {
        // given
        Path csvFile = tempDir.resolve("utf8.csv");
        String csvContent = """
                "한글 댓글"
                "日本語コメント"
                "中文评论"
                "English comment"
                """;
        Files.writeString(csvFile, csvContent);

        // when
        List<String> comments = csvReader.streamAllComments(csvFile.toString()).toList();

        // then
        assertEquals(4, comments.size());
        assertEquals("한글 댓글", comments.get(0));
        assertEquals("日本語コメント", comments.get(1));
        assertEquals("中文评论", comments.get(2));
        assertEquals("English comment", comments.get(3));
    }

    @Test
    void streamAllComments_헤더만_있는_파일(@TempDir Path tempDir) throws IOException, CsvException {
        // given
        Path csvFile = tempDir.resolve("header_only.csv");
        String csvContent = "";
        Files.writeString(csvFile, csvContent);

        // when
        List<String> comments = csvReader.streamAllComments(csvFile.toString()).toList();

        // then
        assertTrue(comments.isEmpty());
    }
}