
apply { plugin("kotlin") }

val usedIntellijPlugins = arrayOf(
        "properties",
        "gradle",
        "Groovy",
        "coverage",
        "maven",
        "android",
        "junit",
        "testng",
        "IntelliLang",
        "testng",
        "copyright",
        "properties",
        "java-i18n",
        "java-decompiler")

dependencies {
    testRuntime(intellijCoreDep()) { includeJars("intellij-core") }
    testRuntime(intellijDep())

    compile(projectDist(":kotlin-stdlib"))
    compile(project(":compiler:frontend"))
    compile(project(":compiler:frontend.java"))
    compile(project(":compiler:light-classes"))
    compile(project(":compiler:util"))
    compileOnly(intellijCoreDep()) { includeJars("intellij-core") }

    testCompile(project(":idea"))
    testCompile(project(":idea:idea-test-framework"))
    testCompile(project(":compiler:light-classes"))
    testCompile(projectDist(":kotlin-test:kotlin-test-junit"))
    testCompile(commonDep("junit:junit"))

    testRuntime(project(":plugins:kapt3-idea")) { isTransitive = false }
    testRuntime(projectDist(":kotlin-compiler"))
    testRuntime(project(":idea:idea-jvm"))
    testRuntime(project(":idea:idea-android"))
    testRuntime(project(":plugins:android-extensions-ide"))
    testRuntime(project(":sam-with-receiver-ide-plugin"))
    testRuntime(project(":allopen-ide-plugin"))
    testRuntime(project(":noarg-ide-plugin"))
    usedIntellijPlugins.forEach {
        testRuntime(intellijPluginDep(it))
    }
}

sourceSets {
    "main" { projectDefault() }
    "test" { projectDefault() }
}

projectTest {
    dependsOnTaskIfExistsRec("dist", project = rootProject)
    workingDir = rootDir
}

testsJar()


val testForWebDemo by task<Test> {
    include("**/*JavaToKotlinConverterForWebDemoTestGenerated*")
    classpath = the<JavaPluginConvention>().sourceSets["test"].runtimeClasspath
    workingDir = rootDir
}
val cleanTestForWebDemo by tasks

val test: Test by tasks
test.apply {
    exclude("**/*JavaToKotlinConverterForWebDemoTestGenerated*")
    dependsOn(testForWebDemo)
}
val cleanTest by tasks
cleanTest.dependsOn(cleanTestForWebDemo)

