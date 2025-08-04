plugins {
    id("test-fixtures")
}

val syncNullMarkedPackageTestFixtures by tasks.registering(SyncTestFixtures::class) {
    intoSubPackage("nullmarkedpackage")
    preserve.include("package-info.java")
}

val syncNullUnmarkedClassesTestFixtures by tasks.registering(SyncTestFixtures::class) {
    intoSubPackage("nullunmarkedclass")
    addOuterTypeAnnotation("org.jspecify.annotations.NullUnmarked")
    preserve.include("package-info.java")
}

val syncNullMarkedClassesTestFixtures by tasks.registering(SyncTestFixtures::class) {
    intoSubPackage("nullmarkedclass")
    addOuterTypeAnnotation("org.jspecify.annotations.NullMarked")
}

tasks.register("syncTestFixtures") {
    dependsOn(syncNullMarkedPackageTestFixtures, syncNullUnmarkedClassesTestFixtures, syncNullMarkedClassesTestFixtures)
}