# Goal
- 댓글리스트에서 유효한 학교 이름을 찾아낸다.
- 학교별 카운트를 산출한다.
- 결과는 `result.txt` 로 저장되어야 한다.
  - 출력 형식은 아래와 같다.
    - 학교 이름과 숫자 사이에는 탭문자가 들어간다.
      ```text
      xx중학교 192
      xxx고등학교 254
      ```
- 로그는 `result.log` 로 저장되어야 한다.

# Environment
- `Java 17`
- `Gradle 8.14.3`

# Constraints
- 외부 라이브러리, 외부 데이터 등의 사용은 Open Source 또는 무료의 경우 제한없이 사용 가능합니다. (누구나 제한 없이 접근 가능해야 함)
- Java 버전은 17이어야 한다.

# Backlogs
  - [ ] Parameterization for input file name, output file name
- [ ] Dynamic Configuration for `school_name_list.txt`, `user_dic.txt`
  - By using API Client, Database, Redis and so on.
  - It can be also implemented by just using application.yaml. 
- [ ] Optimization performance
- [ ] gradle cache & parallel setting (make faster build time)
- [ ] shadow fat jar (resolve KOMORAN library dependency)
  - remove `https://jitpack.io` in build.gradle.kts
- [ ] Gradle Catalog Version
- [ ] SpringBoot AutoConfiguration (for using autoconfigured bean)
  - `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
  - `@AutoConfiguration`

---
# Quick Start
## Gradle Build
```bash
./gradlew clean build installDist distTar distZip

# if you want to install more fastly, then skip test
./gradlew clean -x test build installDist distTar distZip
```

## Executable File 
- You can also download.
  - [Release 1.1.3](https://github.com/rolroralra/korean-text-analyzer/releases/tag/1.1.3) 
  - [Ask to email (rolroralra@gmail.com)](mailto:rolroralra@gmail.com)  
```bash
cp ${YOUR_PATH}/comments.csv build/install/comment-analyzer/bin

cd build/install/comment-analyzer/bin

./comment-analyzer

```

## Result File and Log File
```angular2html
$ ls -lt result.txt result.log
result.log result.txt
```

<details>
  <summary>result.txt</summary>

  <p>

```txt
영덕중학교      159
율현중학교      47
은행중학교      37
난우중학교      36
석우중학교      30
경기국제통상고등학교    26
대구여자상업고등학교    23
경원중학교      23
양덕여자중학교  23
대기고등학교    22
소하고등학교    22https://github.com/rolroralra/korean-text-analyzer/releases/download/1.1.3/comment-analyzer-1.1.3.zip
태장중학교      22
하양여자중학교  15
고촌중학교      12
행신중학교      12
동두천여자중학교        11
명현중학교      11
목포하당중학교  10
강남중학교      10
대전글꽃중학교  9
잠신중학교      8
정화중학교      8
서울공연예술고등학교    8
대전가양중학교  7
부원여자중학교  7
삼호중학교      7
연무중학교      7
영천여자고등학교        7
개봉중학교      6
해강중학교      6
성남여자중학교  6
진선여자중학교  6
대부중학교      6
양청중학교      6
성수고등학교    6
명문고등학교    6
경산여자고등학교        5
문산중학교      5
천안월봉초등학교        5
영천여자중학교  4
신정중학교      4
안성여자중학교  4
온양중앙초등학교        3
화정중학교      3
홍익디자인고등학교      3
충북과학고등학교        3
세현고등학교    3
이충고등학교    3
월봉초등학교    3
명지중학교      3
동국대학교      3
문시중학교      3
송화초등학교    3
오금중학교      3
인제대학교      3
충렬여자중학교  3
성원중학교      2
서울송화초등학교        2
동대문중학교    2
수월중학교      2
문정초등학교    2
천안여자중학교  2
민족사관고등학교        2
원주삼육중학교  2
부산중앙중학교  2
서곶중학교      2
경북대학교사범대학부설중학교    2
서강대학교      2
병점중학교      2
청원중학교      2
은성중학교      2
상산고등학교    2
안곡중학교      2
천안용곡초등학교        2
부양초등학교    2
제물포여자중학교        2
창일중학교      2
상지여자고등학교        2
광양마동초등학교        2
중앙여자중학교  2
김포여자중학교  2
창덕여자고등학교        2
대전용운중학교  1
경남관광고등학교        1
용운중학교      1
석정여자중학교  1
장평중학교      1
인천약산초등학교        1
옥동중학교      1
태종대중학교    1
국립전통예술중학교      1
경북대학교      1
발산중학교      1
경주여자중학교  1
금곡고등학교    1
부천여자중학교  1
화북초등학교    1
고운초등학교    1
양동여자중학교  1
포항제철중학교  1
당곡중학교      1
신일비즈니스고등학교    1
함지고등학교    1
인천체육고등학교        1
도농초등학교    1
문화고등학교    1
인천해원초등학교        1
양영중학교      1
용문중학교      1
조원중학교      1
비전중학교      1
푸른중학교      1
문수고등학교    1
거창여자중학교  1
한내여자중학교  1
계성고등학교    1
이목중학교      1
모현중학교      1
대구운암초등학교        1
근명중학교      1
대전탄방중학교  1
부산가톨릭대학교        1
부곡여자중학교  1
김포고등학교    1
중앙여자고등학교        1
진주고등학교    1
오산정보고등학교        1
동덕여자대학교  1
경안여자중학교  1
장호원중학교    1
유신고등학교    1
구리고등학교    1
봉개초등학교    1
천안봉서중학교  1
부곡중학교      1
우석중학교      1
인천청라중학교  1
강원중학교      1
상원중학교      1
대구가톨릭대학교        1
구갈초등학교    1
김천대학교      1
홍익대학교      1
가락중학교      1
김해중앙여자중학교      1
호원고등학교    1
성화중학교      1
동학중학교      1
창현고등학교    1
정화여자고등학교        1
부산예술중학교  1
예산여자중학교  1
부천중학교      1
토현중학교      1
동주여자중학교  1
성남고등학교    1
영문중학교      1
상산초등학교    1
평택안일초등학교        1
양당초등학교    1
과천중학교      1
청량중학교      1
가재울중학교    1
아미초등학교    1
후평중학교      1
선린인터넷고등학교      1
인천송도초등학교        1
예일여자중학교  1
서정중학교      1
인천청라초등학교        1
문선초등학교    1
송도중학교      1
인하대학교      1
수지중학교      1
도담중학교      1
영운중학교      1
용원중학교      1
성신여자대학교  1
청라중학교      1
서천중학교      1
성일중학교      1
삼괴고등학교    1
운암초등학교    1
창원여자고등학교        1
만월중학교      1
서산석림중학교  1
대전전민고등학교        1
성당중학교      1
동수영중학교    1
구일중학교      1
창덕여자중학교  1
수원다산중학교  1
신반포중학교    1
궁내중학교      1
명륜초등학교    1
증평초등학교    1
가톨릭대학교    1
배문중학교      1
신연중학교      1
광주숭일중학교  1
전주동중학교    1
```

  </p>

</details>

<details>
  <summary>result.log</summary>

  <p>


```log
SLF4J(I): Connected with provider of type [ch.qos.logback.classic.spi.LogbackServiceProvider]
19:23:55.380 [main] INFO  c.k.t.c.a.SchoolAnalyzerApplication - 학교명 분석 시작
19:23:57.344 [main] INFO  c.k.t.c.a.csv.DefaultCsvReader - CSV 파일 읽기 시작: comments.csv
19:23:57.433 [main] INFO  c.k.t.c.a.csv.DefaultCsvReader - CSV 파일 읽기 완료: comments.csv
19:24:00.955 [main] INFO  c.k.t.c.analyzer.SchoolAnalyzer - 발견된 고유 학교 이름 수: 201
19:24:00.956 [main] INFO  c.k.t.c.analyzer.SchoolAnalyzer - 전체 댓글 수: 1000
19:24:00.956 [main] INFO  c.k.t.c.analyzer.SchoolAnalyzer - 학교 이름이 포함된 댓글 수: 853
19:24:00.962 [main] INFO  c.k.t.c.analyzer.SchoolAnalyzer - 0개 학교가 포함된 댓글: 147
19:24:00.963 [main] INFO  c.k.t.c.analyzer.SchoolAnalyzer - 1개 학교가 포함된 댓글: 821
19:24:00.963 [main] INFO  c.k.t.c.analyzer.SchoolAnalyzer - 2개 학교가 포함된 댓글: 10
19:24:00.963 [main] INFO  c.k.t.c.analyzer.SchoolAnalyzer - 3개 학교가 포함된 댓글: 22
19:24:00.963 [main] INFO  c.k.t.c.a.csv.DefaultResultWriter - 결과 파일 작성 시작: result.txt
19:24:00.971 [main] INFO  c.k.t.c.a.csv.DefaultResultWriter -   1. 영덕중학교 - 159건
19:24:00.972 [main] INFO  c.k.t.c.a.csv.DefaultResultWriter -   2. 율현중학교 - 47건
19:24:00.972 [main] INFO  c.k.t.c.a.csv.DefaultResultWriter -   3. 은행중학교 - 37건
19:24:00.972 [main] INFO  c.k.t.c.a.csv.DefaultResultWriter -   4. 난우중학교 - 36건
19:24:00.972 [main] INFO  c.k.t.c.a.csv.DefaultResultWriter -   5. 석우중학교 - 30건
19:24:00.972 [main] INFO  c.k.t.c.a.csv.DefaultResultWriter -   6. 경기국제통상고등학교 - 26건
19:24:00.972 [main] INFO  c.k.t.c.a.csv.DefaultResultWriter -   7. 대구여자상업고등학교 - 23건
19:24:00.972 [main] INFO  c.k.t.c.a.csv.DefaultResultWriter -   8. 경원중학교 - 23건
19:24:00.972 [main] INFO  c.k.t.c.a.csv.DefaultResultWriter -   9. 양덕여자중학교 - 23건
19:24:00.972 [main] INFO  c.k.t.c.a.csv.DefaultResultWriter -   10. 대기고등학교 - 22건
19:24:00.972 [main] INFO  c.k.t.c.a.csv.DefaultResultWriter - 결과 파일 작성 완료: result.txt
19:24:00.972 [main] INFO  c.k.t.c.a.SchoolAnalyzerApplication - 학교명 분석 완료
```

  </p>

</details>

# Guide for using library
## Gradle Setting
- add repository `"https://jitpack.io"` 
  - `com.github.shin285:KOMORAN` 
- add repository `https://maven.pkg.github.com/rolroralra/korean-text-analyzer`
  - You should have `GRP_USER` and `GRP_TOKEN`.
  - [Ask to email (rolroralra@gmail.com)](mailto:rolroralra@gmail.com)

```kotlin
repositories {
	mavenCentral()
	maven {
		url = uri("https://jitpack.io")
	}
	maven {
		name = "GitHubPackages"
		url = uri("https://maven.pkg.github.com/rolroralra/korean-text-analyzer")
		credentials {
			username = System.getenv("GPR_USERNAME") ?: project.findProperty("gpr.user") as String?
			password = System.getenv("GPR_TOKEN") ?: project.findProperty("gpr.token") as String?
		}
	}
}
```

## Dependency
```kotlin
implementation("com.kakaobank.tools:comment-analyzer:1.1.3")
```

## Code Exmample
```java
import com.kakaobank.tools.comment.analyzer.SchoolAnalyzerApplication;

public class DemoApplication {

  public static void main(String[] args) {
    SchoolAnalyzerApplication.run(args);
  }

}
```

```java
SchoolAnalyzer schoolAnalyzer = new SchoolAnalyzer();

Set<String> schoolNames = schoolAnalyzer.extractSchoolNames("성산중학교 학생입니다!");
```
