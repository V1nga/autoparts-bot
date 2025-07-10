plugins {
    id("java")
}

group = "ru.v1nga.autoparts"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.telegram:telegrambots-springboot-longpolling-starter:9.0.0")
    implementation("org.telegram:telegrambots-client:9.0.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}