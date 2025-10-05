package com.kakaobank.tools.comment.analyzer.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultCsvReader implements CsvReader {
    @Override
    public Stream<String> streamAllComments(String inputFile) throws IOException, CsvException {
        log.info("CSV 파일 읽기 시작: {}", inputFile);

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(inputFile);

        if (inputStream == null) {
            File file = new File(inputFile);
            if (!file.exists()) {
                throw new FileNotFoundException("파일을 찾을 수 없습니다: " + inputFile);
            }
            inputStream = new FileInputStream(file);
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.readAll().stream()
                .filter(row -> row.length > 0 && row[0] != null && !row[0].isBlank())
                .map(row -> row[0]);
        } finally {
            log.info("CSV 파일 읽기 완료: {}", inputFile);
        }
    }

    @Override
    public List<String> readAllComments(String inputFile) throws IOException, CsvException {
        return streamAllComments(inputFile).toList();
    }
}
