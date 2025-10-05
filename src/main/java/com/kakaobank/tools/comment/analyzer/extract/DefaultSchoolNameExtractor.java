package com.kakaobank.tools.comment.analyzer.extract;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * School Name Extractor by regular expression
 *
 * @see KomoranSchoolNameExtractor
 * @deprecated instead <code>KomoranSchoolNameExtractor</code>
 */
@Deprecated(forRemoval = true)
public class DefaultSchoolNameExtractor implements SchoolNameExtractor {

    private static final Pattern FULL_PATTERN = Pattern.compile("([가-힣]+?(?:초등학교|중학교|고등학교|대학교|학교))", Pattern.CANON_EQ);

    private static final Pattern SHORT_PATTERN = Pattern.compile("([가-힣]+?[초중고대])", Pattern.CANON_EQ);
    public static final String SCHOOL_NAME_POSTFIX = "학교";

    @Override
    public Set<String> extractSchoolNames(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptySet();
        }

        Set<String> fullSchoolNames = extractSchoolNames(text, FULL_PATTERN);
        Set<String> shortSchoolNames = extractSchoolNames(text, SHORT_PATTERN)
            .stream().map(it -> it.replace(SCHOOL_NAME_POSTFIX, "") +  SCHOOL_NAME_POSTFIX).collect(Collectors.toSet());

        return Stream.concat(fullSchoolNames.stream(), shortSchoolNames.stream()).collect(
            Collectors.toSet());
    }

    private Set<String> extractSchoolNames(String text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);

        Set<String> schools = new HashSet<>();

        while (matcher.find()) {
            String school = matcher.group(1);
            schools.add(school);
        }

        return schools;
    }
}
