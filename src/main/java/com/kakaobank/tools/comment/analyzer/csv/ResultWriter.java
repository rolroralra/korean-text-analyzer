package com.kakaobank.tools.comment.analyzer.csv;

import java.io.IOException;
import java.util.Map;

public interface ResultWriter {
    void writeResults(Map<String, Long> schoolCounts, String outputFile) throws IOException;
}
