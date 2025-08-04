import org.gradle.api.tasks.Sync

abstract class SyncTestFixtures : Sync() {
    init {
        from(
            project.project(sourceProject).projectDir
                .resolve("src/main/java")
                .resolve(sourcePackage.replace('.', '/'))
        )
    }

    fun intoSubPackage(subPackageName: String) {
        into(
            project.projectDir
                .resolve("src/main/java")
                .resolve(testFixturesBasePackage.replace('.', '/'))
                .resolve(subPackageName.replace('.', '/'))
        )
        filter {
            it.replace(
                Regex("^package " + Regex.escape(sourcePackage) + ";$"),
                "package $testFixturesBasePackage.$subPackageName;"
            )
        }
    }

    fun addOuterTypeAnnotation(annotationFqn: String) {
        filter {
            it.replace(Regex("^((?:public )?(?:record|class|interface))"), "@$annotationFqn\n$1")
        }
    }

    companion object {
        private const val testFixturesBasePackage = "de.joshuagleitze.jspecify.interpreter.fixtures"
        private const val sourceProject = ":test-fixtures:unnamed-module"
        private const val sourcePackage = "$testFixturesBasePackage.notmarked"
    }
}