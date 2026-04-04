plugins {
    alias(libs.plugins.java.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.signing)
}

dependencies {
    api(libs.bundles.jpa.common)
}
