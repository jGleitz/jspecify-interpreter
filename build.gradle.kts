plugins {
	java
}

group = "de.joshuagleitze"
version = "1.0-SNAPSHOT"
val javaVersion = 17

repositories {
	mavenCentral()
}

dependencies {
	compileOnly("org.projectlombok:lombok:1.18.38")

	annotationProcessor("org.projectlombok:lombok:1.18.38")

	implementation("org.jspecify:jspecify:1.0.0")

	testCompileOnly("org.projectlombok:lombok:1.18.38")

	testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

	testImplementation(platform("org.junit:junit-bom:5.13.4"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.assertj:assertj-core:3.27.3")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(javaVersion)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = javaVersion
	options.compilerArgs.add("-parameters")
}

tasks.test {
	useJUnitPlatform()
}