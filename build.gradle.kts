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


var testFixturesBasePackage = "de.joshuagleitze.jspecify.interpreter.fixtures"
var testFixturesBaseDir = project.layout.projectDirectory.dir("test-fixtures/unnamed-module/src/main/java")
    .dir(testFixturesBasePackage.replace('.', '/'))

fun Sync.fromTestFixturesPackage(packageName: String) {
    from(testFixturesBaseDir.dir(packageName.replace('.', '/')))
}

fun Sync.intoTestFixturesPackage(packageName: String) {
    into(testFixturesBaseDir.dir(packageName.replace('.', '/')))
    filter {
        it.replace(
            Regex("^package " + Regex.escape(testFixturesBasePackage) + """\..*;$"""),
            "$testFixturesBasePackage.$packageName"
        )
    }
}

val syncNullMarkedPackageTestFixtures by tasks.registering(Sync::class) {
    fromTestFixturesPackage("notmarked")
    intoTestFixturesPackage("nullmarkedpackage")
    preserve.include("package-info.java")
}

tasks.register<Sync>("syncTestFixtures") {
    var basePackage = "de.joshuagleitze.jspecify.interpreter.fixtures"
    var baseDir = "test-fixtures/unnamed-module/src/main/java/${basePackage.replace('.', '/')}"
    from("$baseDir/notmarked")
    into("$baseDir/nullmarkedpackage") {
        /*filter {
            it.replace("package $basePackage.notmarked", "package $basePackage.nullmarkedpackage")
        }*/
    }
    into(baseDir)

    preserve { include("**/package-info.java") }
}