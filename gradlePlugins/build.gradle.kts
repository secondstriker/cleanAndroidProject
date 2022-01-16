plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("dependencies") {
            id = "com.codewithmohsen.dependencies"
            implementationClass = "com.codewithmohsen.gradle.DependenciesPlugin"
        }
    }
}
