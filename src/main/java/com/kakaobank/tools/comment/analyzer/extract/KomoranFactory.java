package com.kakaobank.tools.comment.analyzer.extract;

import java.util.Objects;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;

public class KomoranFactory {

    private static final String USER_DIC_FILE_PATH = "/user_dic.txt";

    private KomoranFactory() {
        throw new UnsupportedOperationException("KomoranFactory cannot be instantiated");
    }

    public static Komoran createKomoran() {
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

        komoran.setUserDic(
            Objects.requireNonNull(KomoranFactory.class.getResource(USER_DIC_FILE_PATH)).getFile());

        return komoran;
    }
}
