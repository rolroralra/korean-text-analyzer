plugins {
	java
	`java-test-fixtures`
	`java-library`
	`maven-publish`
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
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
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
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
