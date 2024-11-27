plugins {
    application
    id("org.openjfx.javafxplugin") version "0.0.13" // JavaFX plugin
    java
    //kotlin("jvm") version "1.8.0"

}

group = "org.app"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjfx:javafx-controls:22.0.2") //JavaFX version, adjust if needed
    implementation("org.openjfx:javafx-fxml:22.0.2")    // using FXML
    implementation("com.google.code.gson:gson:2.10.1")  //add Gson dependency
    implementation("mysql:mysql-connector-java:8.0.33") //add sql connector

    implementation("com.squareup.okhttp3:okhttp:4.10.0") //updated OkHttp dependency
    implementation("com.google.code.gson:gson:2.10.1") //Gson dependency
    implementation("org.mindrot:jbcrypt:0.4") //check user password

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

javafx {
    version = "22.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("com.library.LibraryManagementApplication")
}

tasks.test {
    useJUnitPlatform()
}
