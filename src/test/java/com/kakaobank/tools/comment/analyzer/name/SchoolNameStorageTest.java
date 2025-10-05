package com.kakaobank.tools.comment.analyzer.name;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SchoolNameStorageTest {

    @Test
    void getSchoolNames() {
        assertThat(SchoolNameStorage.getSchoolNames()).isNotEmpty();
    }
}