package com.kakaobank.tools.comment.analyzer.client;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeisApiClient {
    private static final Logger logger = LoggerFactory.getLogger(NeisApiClient.class);
    private static final String BASE_URL = "https://open.neis.go.kr/hub";

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String apiKey;

    public NeisApiClient(String apiKey) {
        this(apiKey, createDefaultHttpClient());
    }

    public NeisApiClient(String apiKey, OkHttpClient httpClient) {
        this.apiKey = apiKey;
        this.httpClient = httpClient;
        this.gson = new Gson();
    }

    private static OkHttpClient createDefaultHttpClient() {
        return new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    }

    /**
     * 학교 정보 조회
     *
     * @param pageIndex 페이지 번호 (1부터 시작)
     * @param pageSize 페이지당 결과 수
     * @return 학교 정보 응답
     * @throws NeisApiException API 호출 실패 시
     */
    public SchoolInfoResponse getSchoolInfo(int pageIndex, int pageSize) throws NeisApiException {
        return getSchoolInfo(pageIndex, pageSize, null);
    }

    /**
     * 학교 정보 조회 (학교명 검색)
     *
     * @param pageIndex 페이지 번호 (1부터 시작)
     * @param pageSize 페이지당 결과 수
     * @param schoolName 학교명 (선택사항)
     * @return 학교 정보 응답
     * @throws NeisApiException API 호출 실패 시
     */
    public SchoolInfoResponse getSchoolInfo(int pageIndex, int pageSize, String schoolName) throws NeisApiException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "/schoolInfo").newBuilder()
            .addQueryParameter("Type", "json")
            .addQueryParameter("pIndex", String.valueOf(pageIndex))
            .addQueryParameter("pSize", String.valueOf(pageSize))
            .addQueryParameter("KEY", apiKey);

        if (schoolName != null && !schoolName.isBlank()) {
            urlBuilder.addQueryParameter("SCHUL_NM", schoolName);
        }

        String url = urlBuilder.build().toString();
        logger.trace("Request URL: {}", url);

        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new NeisApiException("HTTP error: " + response.code() + " - " + response.message());
            }

            String responseBody = response.body().string();
            logger.trace("Response: {}", responseBody);

            SchoolInfoResponse result = gson.fromJson(responseBody, SchoolInfoResponse.class);

            // API 에러 체크
            if (result.schoolInfo() != null && !result.schoolInfo().isEmpty()) {
                var head = result.schoolInfo().get(0).head();
                if (head != null && !head.isEmpty() && head.get(1).result() != null) {
                    var apiResult = head.get(1).result();
                    if (!"INFO-000".equals(apiResult.code())) {
                        throw new NeisApiException("API error: " + apiResult.code() + " - " + apiResult.message());
                    }
                }
            }

            return result;
        } catch (IOException e) {
            throw new NeisApiException("Failed to call NEIS API", e);
        }
    }

    /**
     * API 클라이언트 종료 (리소스 정리)
     */
    public void close() {
        httpClient.dispatcher().executorService().shutdown();
        httpClient.connectionPool().evictAll();
    }

    public static class NeisApiException extends RuntimeException {
        public NeisApiException(String message) {
            super(message);
        }

        public NeisApiException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
