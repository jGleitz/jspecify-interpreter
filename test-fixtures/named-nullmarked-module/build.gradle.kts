plugins {
    id("test-fixtures")
}

val syncNotMarkedTestFixtures by tasks.registering(SyncTestFixtures::class) {
    intoSubPackage("nullmarkedmodule.notmarked")
}

val syncNullUnmarkedPackageTestFixtures by tasks.registering(SyncTestFixtures::class) {
    intoSubPackage("nullmarkedmodule.nullunmarkedpackage")
    preserve.include("package-info.java")
}

val syncNullUnmarkedClassesTestFixtures by tasks.registering(SyncTestFixtures::class) {
    intoSubPackage("nullmarkedmodule.nullunmarkedclass")
    addOuterTypeAnnotation("org.jspecify.annotations.NullUnmarked")
}

tasks.register("syncTestFixtures") {
    dependsOn(syncNotMarkedTestFixtures, syncNullUnmarkedPackageTestFixtures, syncNullUnmarkedClassesTestFixtures)
}