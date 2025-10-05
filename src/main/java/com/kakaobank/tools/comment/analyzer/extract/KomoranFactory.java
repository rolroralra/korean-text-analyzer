package com.kakaobank.tools.comment.analyzer.extract;

import com.kakaobank.tools.comment.analyzer.exception.CommentAnalyzeGlobalException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;

public class KomoranFactory {

    private static final String USER_DIC_FILE_PATH = "user_dic.txt";

    private KomoranFactory() {
        throw new UnsupportedOperationException("KomoranFactory cannot be instantiated");
    }

    public static Komoran createKomoran() {
        try (InputStream is = KomoranFactory.class.getClassLoader().getResourceAsStream(USER_DIC_FILE_PATH)) {
            if (is == null) {
                throw new CommentAnalyzeGlobalException(USER_DIC_FILE_PATH +  " not found in resources");
            }

            Path tempFile = Files.createTempFile("user_dic", ".txt");

            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);

            tempFile.toFile().deleteOnExit();

            Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

            komoran.setUserDic(tempFile.toString());  // 실제 파일 경로 전달

            return komoran;
        } catch (IOException e) {
            throw new CommentAnalyzeGlobalException(e);
        }
    }
}
