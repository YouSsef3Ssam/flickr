package extenstions

import AnnotationProcessors
import Dependencies
import TestDependencies
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * Adds a dependency to the `implementation` configuration.
 *
 * @param dependencyNotation name of dependency to add at specific configuration
 *
 * @return the dependency
 */
fun DependencyHandler.implementation(dependencyNotation: String): Dependency? =
    add("implementation", dependencyNotation)

/**
 * Adds a dependency to the `releaseImplementation` configuration.
 *
 * @param dependencyNotation name of dependency to add at specific configuration
 *
 * @return the dependency
 */
fun DependencyHandler.releaseImplementation(dependencyNotation: String): Dependency? =
    add("releaseImplementation", dependencyNotation)

/**
 * Adds a dependency to the `debugImplementation` configuration.
 *
 * @param dependencyNotation name of dependency to add at specific configuration
 *
 * @return the dependency
 */
fun DependencyHandler.debugImplementation(dependencyNotation: String): Dependency? =
    add("debugImplementation", dependencyNotation)

/**
 * Adds a dependency to the `testImplementation` configuration.
 *
 * @param dependencyNotation name of dependency to add at specific configuration
 *
 * @return the dependency
 */
fun DependencyHandler.testImplementation(dependencyNotation: String): Dependency? =
    add("testImplementation", dependencyNotation)

/**
 * Adds a dependency to the `androidTestImplementation` configuration.
 *
 * @param dependencyNotation name of dependency to add at specific configuration
 *
 * @return the dependency
 */
fun DependencyHandler.androidTestImplementation(dependencyNotation: String): Dependency? =
    add("androidTestImplementation", dependencyNotation)

/**
 * Adds a dependency to the `kapt` configuration.
 *
 * @param dependencyNotation name of dependency to add at specific configuration
 *
 * @return the dependency
 */
fun DependencyHandler.kapt(dependencyNotation: String): Dependency? =
    add("kapt", dependencyNotation)

fun DependencyHandler.androidComponent() {
    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.core)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.materialDesign)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.swipeRefresh)
    implementation(Dependencies.splash)
    implementation(Dependencies.timber)
    implementation(Dependencies.coil)
    implementation(Dependencies.easyPref)
    implementation(Dependencies.coroutines)
    implementation(Dependencies.lifecycle)
    addRecyclerView()
}

fun DependencyHandler.addRecyclerView() {
    implementation(Dependencies.recyclerView)
}

fun DependencyHandler.addHilt() {
    implementation(Dependencies.hilt)
    kapt(AnnotationProcessors.hilt)
}

fun DependencyHandler.addNetwork() {
    implementation(Dependencies.retrofit)
    implementation(Dependencies.gsonConverter)
    implementation(Dependencies.gson)
    implementation(Dependencies.okhttp)
    implementation(Dependencies.okhttpLogging)
    debugImplementation(Dependencies.chuckerDebug)
    releaseImplementation(Dependencies.chuckerRelease)
}

fun DependencyHandler.addNavigation() {
    implementation(Dependencies.navigationFragment)
    implementation(Dependencies.navigationUI)
    implementation(Dependencies.navigationUIKtx)
}

fun DependencyHandler.addRoom() {
    implementation(Dependencies.room)
    implementation(Dependencies.roomKtx)
    kapt(AnnotationProcessors.room)
}

fun DependencyHandler.addTestsDependencies() {
    addUnitTestDependencies()
    addUiTestDependencies()
}

fun DependencyHandler.addUnitTestDependencies() {
    testImplementation(TestDependencies.junit)
    testImplementation(TestDependencies.mockitoFramework)
    testImplementation(TestDependencies.mockk)
    testImplementation(TestDependencies.coroutinesTest)
    testImplementation(TestDependencies.coreTest)
}

fun DependencyHandler.addUiTestDependencies() {
    androidTestImplementation(TestDependencies.junitTest)
    androidTestImplementation(TestDependencies.espresso)
    androidTestImplementation(TestDependencies.espressoIntents)
    androidTestImplementation(TestDependencies.espressoContrib)
    implementation(TestDependencies.espressoIdling)
}

fun DependencyHandler.addUtils() {
    implementation(Dependencies.utils)
}