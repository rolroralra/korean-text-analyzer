package com.kakaobank.tools.comment.analyzer.csv;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public interface CsvReader {

    Stream<String> streamAllComments(String inputFile) throws IOException, CsvException ;

    List<String> readAllComments(String inputFile) throws IOException, CsvException;
}
