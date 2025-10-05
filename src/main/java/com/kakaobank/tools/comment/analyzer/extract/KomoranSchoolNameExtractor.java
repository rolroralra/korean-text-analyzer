package com.kakaobank.tools.comment.analyzer.extract;

import com.kakaobank.tools.comment.analyzer.name.SchoolNameShortenConverter;
import com.kakaobank.tools.comment.analyzer.name.SchoolNameValidator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KomoranSchoolNameExtractor implements SchoolNameExtractor {

    private static final Pattern FULL_PATTERN = Pattern.compile("([가-힣]+?(?:초등학교|중학교|고등학교|대학교))", Pattern.CANON_EQ);

    private static final Pattern SHORT_PATTERN = Pattern.compile("([가-힣]+?(?:초|중|고|대|대학))", Pattern.CANON_EQ);

    public static final int KOMORAN_MAX_ALLOWED_TEXT_LENGTH = 500;

    public static final String SPACE = " ";

    private final Komoran komoran = KomoranFactory.createKomoran();

    private final SchoolNameValidator schoolNameValidator = new SchoolNameValidator();


    @Override
    public Set<String> extractSchoolNames(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptySet();
        }

        if (text.length() >= KOMORAN_MAX_ALLOWED_TEXT_LENGTH) {
            List<String> texts = Arrays.stream(text.split(SPACE)).toList();

           return texts.stream()
                .flatMap(t -> this.extractSchoolNamesInternal(t).stream())
                .collect(Collectors.toSet());
        }

        return extractSchoolNamesInternal(text);
    }

    private Set<String> extractSchoolNamesInternal(String text) {
        if (text.isBlank()) {
            return Collections.emptySet();
        }

        KomoranResult result = komoran.analyze(text);

        List<String> nouns = result.getNouns();

        log.debug("추출된 명사: {}", nouns);

        return nouns.stream()
            .filter(schoolNameValidator::isValidSchoolName)
            .map(this::normalizeSchoolName)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    private String normalizeSchoolName(String schoolName) {
        Matcher fullPatternMatcher = FULL_PATTERN.matcher(schoolName);
        Matcher shortPatternmatcher = SHORT_PATTERN.matcher(schoolName);

        if (fullPatternMatcher.find()) {
            return schoolName;
        } else if (shortPatternmatcher.find()) {
            return SchoolNameShortenConverter.convertShortenSchoolName(schoolName);
        }

        // Invalid School Name
        return null;
    }
}
