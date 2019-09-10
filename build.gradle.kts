allprojects {
    repositories {
        jcenter()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }
}

subprojects {
    group = "com.ekino.oss.jcv.example"
}
