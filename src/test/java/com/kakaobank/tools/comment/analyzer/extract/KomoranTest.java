package com.kakaobank.tools.comment.analyzer.extract;

import java.util.Arrays;
import java.util.List;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import org.junit.jupiter.api.Test;

class KomoranTest {
    private final Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

    @Test
    void test() {
        List<KomoranResult> results = komoran.analyze(Arrays.stream("대구경북대학교사범대학부설중학교 대구경북대학교사범대학부설중학교".split(" ")).toList(), 2);

        for (KomoranResult result : results) {
            printKomoranResult(result);
            System.out.println();
        }

        printKomoranResult(komoran.analyze("대구경북대학교사범대학부설중학교 대구경북대학교사범대학부설중학교"));
    }

    private void printKomoranResult(KomoranResult result) {
        List<String> nouns = result.getNouns();
        System.out.println(nouns);
        List<Token> tokenList = result.getTokenList();
        tokenList.forEach(System.out::println);
    }
}
