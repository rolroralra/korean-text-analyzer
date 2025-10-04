package com.kakaobank.tools.comment.analyzer.client;

import com.kakaobank.tools.comment.analyzer.client.SchoolInfoResponse.Row;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NeisApiClientTest {

    private static final String SCHOOL_LIST_FILENAME = "school_name_list.txt";

    private NeisApiClient neisApiClient = new NeisApiClient("280aa8a1663740e49e464b09ec7c3f85");

    @BeforeAll
    static void setup() throws IOException {
        // 파일이 존재하면 삭제
        Files.deleteIfExists(Path.of(SCHOOL_LIST_FILENAME));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13})
    void test(int page) throws IOException {
        SchoolInfoResponse schoolInfo = neisApiClient.getSchoolInfo(page, 1000);

        List<String> schoolNames = schoolInfo.schoolInfo().get(1).row()
            .stream()
            .map(Row::schulNm)
            .toList();

        Files.write(
            Path.of(SCHOOL_LIST_FILENAME),
            schoolNames,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        );
    }
}