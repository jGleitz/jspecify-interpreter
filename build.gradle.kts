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
    testImplementation(project(":test-fixtures:unnamed-module"))

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


val testFixturesBasePackage = "de.joshuagleitze.jspecify.interpreter.fixtures"
val testFixturesBaseDir = project.layout.projectDirectory.dir("test-fixtures/unnamed-module/src/main/java")
    .dir(testFixturesBasePackage.replace('.', '/'))

fun Sync.fromTestFixturesPackage(packageName: String) {
    from(testFixturesBaseDir.dir(packageName.replace('.', '/')))
    inputs.property("inputPackage", "$testFixturesBasePackage.$packageName")
}

fun Sync.intoTestFixturesPackage(packageName: String) {
    into(testFixturesBaseDir.dir(packageName.replace('.', '/')))
    inputs.property("outputPackage", "$testFixturesBasePackage.$packageName")
    filter {
        val inputPackage: String by inputs.properties
        val outputPackage: String by inputs.properties
        it.replace(
            Regex("^package " + Regex.escape(inputPackage) + ";$"),
            "package $outputPackage;"
        )
    }
}

val syncNullMarkedPackageTestFixtures by tasks.registering(Sync::class) {
    fromTestFixturesPackage("notmarked")
    intoTestFixturesPackage("nullmarkedpackage")
    preserve.include("package-info.java")
}

val syncNullUnmarkedClassesTestFixtures by tasks.registering(Sync::class) {
    fromTestFixturesPackage("notmarked")
    intoTestFixturesPackage("nullunmarkedclass")
    filter {
        it.replace(Regex("^((?:public )?(?:record|class|interface))"), "@org.jspecify.annotations.NullUnmarked\n$1")
    }
    preserve.include("package-info.java")
}

val syncNullMarkedClassesTestFixtures by tasks.registering(Sync::class) {
    fromTestFixturesPackage("notmarked")
    intoTestFixturesPackage("nullmarkedclass")
    filter {
        it.replace(Regex("^((?:public )?(?:record|class|interface))"), "@org.jspecify.annotations.NullMarked\n$1")
    }
}

tasks.register("syncTestFixtures") {
    dependsOn(syncNullMarkedPackageTestFixtures, syncNullUnmarkedClassesTestFixtures, syncNullMarkedClassesTestFixtures)
}