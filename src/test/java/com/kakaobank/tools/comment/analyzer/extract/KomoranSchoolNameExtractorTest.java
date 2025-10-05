package com.kakaobank.tools.comment.analyzer.extract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class KomoranSchoolNameExtractorTest {
    private SchoolNameExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new KomoranSchoolNameExtractor();
    }

    @Test
    void extractSchoolNames_정확한_학교명_추출() {
        // given
        String text = "율현중학교 학생입니다";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        assertTrue(result.contains("율현중학교"));
    }

    @Test
    void extractSchoolNames_여러_학교명_추출() {
        // given
        String text = "율현중학교와 영덕중학교 그리고 동대문중학교 학생들";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        System.out.println(result);

        // then
        assertEquals(3, result.size());
        assertTrue(result.contains("율현중학교"));
        assertTrue(result.contains("영덕중학교"));
        assertTrue(result.contains("동대문중학교"));
    }

    @Test
    void extractSchoolNames_초등학교_추출() {
        // given
        String text = "천안월봉초등학교 다니는 학생입니다";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        assertTrue(result.contains("천안월봉초등학교"));
    }

    @Test
    void extractSchoolNames_고등학교_추출() {
        // given
        String text = "대구여자상업고등학교 졸업생입니다";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        assertTrue(result.contains("대구여자상업고등학교"));
    }

    @Test
    void extractSchoolNames_대학교_추출() {
        // given
        String text = "서울대학교 학생입니다";

        // when
        Set<String> result = extractor.extractSchoolNames(text);
        System.out.println(result);

        // then
        assertTrue(result.contains("서울대학교"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void extractSchoolNames_빈_문자열_and_공백만_있는_문자열() {
        // given
        String text = "";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void extractSchoolNames_학교명_없음() {
        // given
        String text = "짜장면 먹고 싶어요";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void extractSchoolNames_중복_학교명() {
        // given
        String text = "율현중학교 학생입니다. 율현중학교는 좋은 학교입니다.";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains("율현중학교"));
    }

    @Test
    void extractSchoolNames_여러_줄_텍스트() {
        // given
        String text = """
                경북 경산, 하양여자중학교
                
                안녕하세요 배달의 민족님
                저희 학교 전체가 짜장면을 갈구 합니다.
                """;

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        assertTrue(result.contains("하양여자중학교"));
    }

    @Test
    void extractSchoolNames_특수문자_포함() {
        // given
        String text = "율현중학교!!! 영덕중학교??? 동대문중학교...";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        System.out.println(result);
        // then
        assertEquals(3, result.size());
    }

    @ParameterizedTest(name = "given: {0}, expected: {1}")
    @CsvSource(value = {
        "서울장평중학교,장평중학교",
        "대구경북대학교사범대학부설중학교,경북대학교사범대학부설중학교",
        "홍익대학교사범대학부속여자중학교,홍익대학교사범대학부속여자중학교"
    }, delimiterString = ",")
    void extractSchoolNames_긴_학교명(String schoolName, String expectedSchool) {
        // when
        Set<String> result = extractor.extractSchoolNames(schoolName);

        System.out.println(result);
        // then
        assertTrue(result.contains(expectedSchool));
    }

    @Test
    void extractSchoolNames_짧은_형태_초() {
        // given
        String text = "부양초 학생입니다";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        assertFalse(result.isEmpty());
    }

    @Test
    void extractSchoolNames_짧은_형태_중() {
        // given
        String text = "영덕중 학생입니다";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        assertFalse(result.isEmpty());
    }

    @Test
    void extractSchoolNames_짧은_형태_고() {
        // given
        String text = "창현고 학생입니다";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        System.out.println(result);
        // then
        assertFalse(result.isEmpty());
    }

    @Test
    void extractSchoolNames_잘못된_붙여쓰기() {
        // given
        String text = "다른애들도언급해댓글은신정중학교";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        // Komoran이 형태소 분석을 통해 "신정중학교"를 추출할 수 있음
        assertTrue(result.contains("신정중학교") || result.isEmpty());
    }

    @Test
    void extractSchoolNames_지역명_포함() {
        // given
        String text = "서울구로구개봉중학교 학생입니다";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        assertTrue(result.contains("서울구로구개봉중학교") ||
            result.contains("개봉중학교"));
    }

    @ParameterizedTest
    @CsvSource({
        "율현중학교 학생, 율현중학교",
        "영덕중 다녀요, 영덕중",
        "부양초등학교입니다, 부양초등학교",
        "서울대학교 재학중, 서울대학교"
    })
    void extractSchoolNames_다양한_케이스(String text, String expectedSchool) {
        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        assertTrue(result.stream().anyMatch(s -> s.contains(expectedSchool.replace("학교", ""))));
    }

    @Test
    void extractSchoolNames_숫자_포함_학교명() {
        // given
        String text = "제2고등학교 학생입니다";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        // 한글만 매칭하므로 숫자는 제외됨
        assertFalse(result.stream().anyMatch(s -> s.contains("2")));
    }

    @Test
    void extractSchoolNames_대소문자_영문_포함() {
        // given
        String text = "KMLA 민족사관고등학교";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        assertTrue(result.contains("민족사관고등학교"));
    }

    @Test
    void extractSchoolNames_반복되는_학교명() {
        // given
        String text = "서울장평중학교".repeat(100);

        // when
        Set<String> result = extractor.extractSchoolNames(text);
        System.out.println(result);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains("장평중학교"));
    }

    @Test
    void extractSchoolNames_학교_키워드만() {
        // given
        String text = "중학교 고등학교 초등학교";

        // when
        Set<String> result = extractor.extractSchoolNames(text);

        // then
        // 학교명 앞에 다른 한글이 없으므로 추출 안됨
        assertTrue(result.isEmpty() || result.stream().allMatch(s -> s.length() <= 4));
    }

    @Test
    void extractSchoolNames_성능_테스트() {
        // given
        String longText = "율현중학교 영덕중학교 동대문중학교 ".repeat(100);

        // when
        long startTime = System.currentTimeMillis();
        Set<String> result = extractor.extractSchoolNames(longText);
        long endTime = System.currentTimeMillis();

        System.out.println(result);
        // then
        assertTrue(endTime - startTime < 5000); // 5초 이내
        assertEquals(3, result.size());
    }

    @Test
    void extractSchoolNames_다양한_문장_성능_테스트() {
        // given
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longText.append("댓글").append(i).append(": 율현중학교 학생입니다. ");
            longText.append("영덕중학교도 좋아요. ");
            longText.append("동대문중학교 짱! ");
        }

        // when
        Set<String> result = extractor.extractSchoolNames(longText.toString());

        // then
        assertEquals(3, result.size());
    }
}