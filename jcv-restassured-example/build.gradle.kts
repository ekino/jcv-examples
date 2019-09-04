plugins {
    java
    eclipse
    id("org.springframework.boot") version "2.1.7.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}

val springBootVersion: String by project.extra

val junitJupiterVersion: String by project.extra
val jsonassertVersion: String by project.extra
val jcvVersion: String by project.extra

dependencies {

    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.rest-assured:rest-assured:3.3.0")
    testImplementation("org.skyscreamer:jsonassert:$jsonassertVersion")
    testImplementation("org.hamcrest:hamcrest:2.1")
    testImplementation("com.ekino.oss.jcv:jcv-hamcrest:$jcvVersion")
    testImplementation("commons-io:commons-io:2.6")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    withType<Test> {
        useJUnitPlatform()
        jvmArgs("-Duser.language=en")
    }
}
