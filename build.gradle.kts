val lombokVersion: String by project
val komoranVersion: String by project
val openCsvVersion: String by project
val okHttp3Version: String by project
val gsonVersion: String by project
val slf4jVersion: String by project
val logbackVersion: String by project
val junitVersion: String by project
val assertjVersion: String by project

plugins {
	java
	`java-test-fixtures`
	`java-library`
	`maven-publish`
}

group = "com.kakaobank.tools"
version = project.findProperty("version") as String
description = "Comment Text Analyzer for School Names"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://jitpack.io")
	}
}

dependencies {
	annotationProcessor("org.projectlombok:lombok:$lombokVersion")
	compileOnly("org.projectlombok:lombok:$lombokVersion")

	implementation("com.github.shin285:KOMORAN:$komoranVersion")
	implementation("com.opencsv:opencsv:$openCsvVersion")
	implementation("com.squareup.okhttp3:okhttp:$okHttp3Version")
	implementation("com.google.code.gson:gson:$gsonVersion")
	implementation("org.slf4j:slf4j-api:$slf4jVersion")
	implementation("ch.qos.logback:logback-classic:$logbackVersion")

	testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
	testImplementation("org.assertj:assertj-core:$assertjVersion")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

publishing {
	repositories {
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/rolroralra/korean-text-analyzer")
			credentials {
				username = System.getenv("GPR_USERNAME") ?: project.findProperty("gpr.user") as String?
				password = System.getenv("GPR_TOKEN") ?: project.findProperty("gpr.token") as String?
			}
		}
	}
	publications {
		create<MavenPublication>("gpr") {
			from(components["java"])
			artifactId = project.name
		}
	}
}

sourceSets {
	test {
		resources {
			srcDirs("src/main/resources", "src/test/resources")
		}
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.testFixturesJar {
	enabled = false
}