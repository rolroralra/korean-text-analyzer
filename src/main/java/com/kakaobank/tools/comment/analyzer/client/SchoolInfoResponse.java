package com.kakaobank.tools.comment.analyzer.client;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public record SchoolInfoResponse(
    List<SchoolInfoData> schoolInfo
) {
    public record SchoolInfoData(
        List<Head> head,
        List<Row> row
    ) {}

    public record Head(
        @SerializedName("list_total_count")
        Integer listTotalCount,

        @SerializedName("RESULT")
        Result result
    ) {}

    public record Result(
        @SerializedName("CODE")
        String code,

        @SerializedName("MESSAGE")
        String message
    ) {}

    public record Row(
        @SerializedName("ATPT_OFCDC_SC_CODE")
        String atptOfcdcScCode,          // 시도교육청코드

        @SerializedName("ATPT_OFCDC_SC_NM")
        String atptOfcdcScNm,            // 시도교육청명

        @SerializedName("SD_SCHUL_CODE")
        String sdSchulCode,              // 표준학교코드

        @SerializedName("SCHUL_NM")
        String schulNm,                  // 학교명

        @SerializedName("ENG_SCHUL_NM")
        String engSchulNm,               // 영문학교명

        @SerializedName("SCHUL_KND_SC_NM")
        String schulKndScNm,             // 학교종류명

        @SerializedName("LCTN_SC_NM")
        String lctnScNm,                 // 소재지명

        @SerializedName("JU_ORG_NM")
        String juOrgNm,                  // 관할조직명

        @SerializedName("FOND_SC_NM")
        String fondScNm,                 // 설립명

        @SerializedName("ORG_RDNZC")
        String orgRdnzc,                 // 도로명우편번호

        @SerializedName("ORG_RDNMA")
        String orgRdnma,                 // 도로명주소

        @SerializedName("ORG_RDNDA")
        String orgRdnda,                 // 도로명상세주소

        @SerializedName("ORG_TELNO")
        String orgTelno,                 // 전화번호

        @SerializedName("HMPG_ADRES")
        String hmpgAdres,                // 홈페이지주소

        @SerializedName("COEDU_SC_NM")
        String coeduScNm,                // 남녀공학구분명

        @SerializedName("ORG_FAXNO")
        String orgFaxno,                 // 팩스번호

        @SerializedName("HS_SC_NM")
        String hsScNm,                   // 고등학교구분명

        @SerializedName("INDST_SPECL_CCCCL_EXST_YN")
        String indstSpeclCccclExstYn,    // 산업체특별학급존재여부

        @SerializedName("HS_GNRL_BUSNS_SC_NM")
        String hsGnrlBusnsScNm,          // 고등학교일반실업구분명

        @SerializedName("SPCLY_PURPS_HS_ORD_NM")
        String spclyPurpsHsOrdNm,        // 특수목적고등학교계열명

        @SerializedName("ENE_BFE_SEHF_SC_NM")
        String eneBfeSeHfScNm,           // 입시전후기구분명

        @SerializedName("DGHT_SC_NM")
        String dghtScNm,                 // 주야구분명

        @SerializedName("FOND_YMD")
        String fondYmd,                  // 설립일자

        @SerializedName("FOAS_MEMRD")
        String foasMemrd,                // 개교기념일

        @SerializedName("LOAD_DTM")
        String loadDtm                   // 수정일자
    ) {}
}