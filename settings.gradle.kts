plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "thz-cli"

// Composite Build: em desenvolvimento, resolve thz.lang:thz-core direto do
// nÃºcleo vizinho (../thz-core-jvm), sem precisar publicar no Maven Local.
// Em CI/consumidores externos, publique o core (`publishToMavenLocal`) e a
// dependÃªncia declarada serÃ¡ baixada como artefato.
includeBuild("../thz-core-jvm")
includeBuild("../thz-gui-jvm")
includeBuild("../thz-agent-jvm")
