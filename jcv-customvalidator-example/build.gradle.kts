plugins {
    java
    eclipse
}

val jsonassertVersion: String by project.extra
val jcvVersion: String by project.extra
val junitJupiterVersion: String by project.extra

dependencies {

    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")

    testImplementation("org.skyscreamer:jsonassert:$jsonassertVersion")
    testImplementation("org.assertj:assertj-core:3.12.2")
    testImplementation("com.ekino.oss.jcv:jcv-assertj:$jcvVersion")
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
