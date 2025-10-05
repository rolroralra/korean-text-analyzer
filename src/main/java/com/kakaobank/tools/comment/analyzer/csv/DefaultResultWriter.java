package com.kakaobank.tools.comment.analyzer.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultResultWriter implements ResultWriter{

    public static final String TAB = "\t";

    @Override
    public void writeResults(Map<String, Long> schoolCounts, String outputFile)
        throws IOException {
        log.info("결과 파일 작성 시작: {}", outputFile);

        List<Entry<String, Long>> sortedEntries = schoolCounts.entrySet()
            .stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .toList();

        try (BufferedWriter writer = Files.newBufferedWriter(
            Paths.get(outputFile), StandardCharsets.UTF_8)) {
            for (Map.Entry<String, Long> entry : sortedEntries) {
                writer.write(entry.getKey() + TAB + entry.getValue());
                writer.newLine();
            }
        }

        loggingResultSummary(sortedEntries);

        log.info("결과 파일 작성 완료: {}", outputFile);
    }

    private static void loggingResultSummary(List<Entry<String, Long>> sortedEntries) {
        List<Entry<String, Long>> topN = sortedEntries.stream()
            .limit(10)
            .toList();

        IntStream.range(0, topN.size())
            .forEach(index ->
                log.info("  {}. {} - {}건", index + 1, topN.get(index).getKey(),
                    topN.get(index).getValue()));
    }
}
