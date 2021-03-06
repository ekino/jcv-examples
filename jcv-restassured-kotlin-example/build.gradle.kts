import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    java
    eclipse
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

val springBootVersion: String by project.extra

val junitJupiterVersion: String by project.extra
val jsonassertVersion: String by project.extra
val jcvVersion: String by project.extra

dependencies {

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.rest-assured:rest-assured:3.3.0")
    testImplementation("org.skyscreamer:jsonassert:$jsonassertVersion")
    testImplementation("org.hamcrest:hamcrest:2.1")
    testImplementation("com.ekino.oss.jcv:jcv-hamcrest:$jcvVersion")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }

    withType<Test> {
        useJUnitPlatform()
        jvmArgs("-Duser.language=en")
    }
}
