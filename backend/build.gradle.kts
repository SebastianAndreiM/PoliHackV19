plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.assetguard"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
val ktor_version = "2.3.12"
val exposed_version = "0.52.0"
val kotlin_version = "2.0.0"
val logback_version = "1.4.14"
val postgresql_version = "42.7.3"
val koin_version = "4.0.2"

dependencies {
    // ── Ktor core ────────────────────────────────────────────────
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")

    // ── Ktor plugins ─────────────────────────────────────────────
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-default-headers-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-request-validation:$ktor_version")

    // ── Ktor client (pentru AI Gateway → LLM API) ────────────────
    implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-cio-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-logging-jvm:$ktor_version")

    // ── Exposed ORM ──────────────────────────────────────────────
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-json:$exposed_version")    // pentru JSONB columns

    // ── PostgreSQL driver ─────────────────────────────────────────
    implementation("org.postgresql:postgresql:$postgresql_version")

    // ── Connection pool ──────────────────────────────────────────
    implementation("com.zaxxer:HikariCP:5.1.0")

    // ── Kotlinx Serialization ────────────────────────────────────
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")

    // ── Coroutines ───────────────────────────────────────────────
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // ── Koin DI (opțional, alternativă la wiring manual) ─────────
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    // ── Logging ──────────────────────────────────────────────────
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // ── Config ───────────────────────────────────────────────────
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version")

    // ── Test ─────────────────────────────────────────────────────
    testImplementation("io.ktor:ktor-server-test-host-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:1.13.11")
}
