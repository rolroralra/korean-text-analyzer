val lombokVersion: String by project
val komoranVersion: String by project
val openCsvVersion: String by project
val slf4jVersion: String by project
val logbackVersion: String by project
val junitVersion: String by project

plugins {
	java
	`java-test-fixtures`
	`java-library`
	`maven-publish`
}

group = "com.kakaobank.tools"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

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
	implementation("org.slf4j:slf4j-api:$slf4jVersion")
	implementation("ch.qos.logback:logback-classic:$logbackVersion")

	testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
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

tasks.withType<Test> {
	useJUnitPlatform()
}
