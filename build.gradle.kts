import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val kotlinVersion = "1.8.22"

	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	id("io.kotest") version "0.4.10"

	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	kotlin("plugin.jpa") version kotlinVersion
}

group = "com"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	val kotestVersion = "5.5.5"

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-web")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.15")

	implementation("org.redisson:redisson-spring-boot-starter:3.23.1")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	runtimeOnly("com.h2database:h2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

	testImplementation("io.kotest:kotest-runner-junit5-jvm:${kotestVersion}")
	testImplementation("io.kotest:kotest-assertions-core-jvm:${kotestVersion}")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
