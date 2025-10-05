package com.kakaobank.tools.comment.analyzer.client;

import com.kakaobank.tools.comment.analyzer.client.SchoolInfoResponse.Row;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Disabled("This test is only used for user_dic.txt, school_name_list.txt")
class NeisApiClientTest {

    private static final String SCHOOL_LIST_FILENAME = "school_name_list.txt";
    private static final String USER_DICT_FILENAME = "user_dic.txt";

    private final NeisApiClient neisApiClient = new NeisApiClient("280aa8a1663740e49e464b09ec7c3f85");

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

        List<String> shortSchoolNames = schoolInfo.schoolInfo().get(1).row()
            .stream()
            .map(Row::schulNm)
            .map(this::shortenSchoolName)
            .toList();

        Set<String> finalSchoolNames = new TreeSet<>(schoolNames);
        finalSchoolNames.addAll(shortSchoolNames);

        Files.write(
            Path.of(SCHOOL_LIST_FILENAME),
            finalSchoolNames,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        );

        Files.write(
            Path.of(USER_DICT_FILENAME),
            finalSchoolNames.stream().map(it -> it + "\tNNG").toList(),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        );
    }

    private String shortenSchoolName(String schoolName) {
        if (schoolName.endsWith("대학교")) {
            return schoolName.substring(0, schoolName.length() - 2);
        }
        if (schoolName.endsWith("초등학교")) {
            return schoolName.substring(0, schoolName.length() - 3);
        }
        if (schoolName.endsWith("중학교")) {
            return schoolName.substring(0, schoolName.length() - 2);
        }
        if (schoolName.endsWith("고등학교")) {
            return schoolName.substring(0, schoolName.length() - 3);
        }

        return schoolName;
    }
}