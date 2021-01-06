import net.minecraftforge.gradle.user.UserExtension
import java.io.FileInputStream
import java.util.*
import java.io.*

buildscript {
    repositories {
        mavenCentral()
        maven("http://files.minecraftforge.net/maven")
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.github.GTNH2:ForgeGradle:FG_1.2-SNAPSHOT")
    }
}

plugins {
    idea
    java
    signing
}

apply(plugin = "forge")

//Downloads Javadocs and sources by default
idea {
    module {
        this.isDownloadJavadoc = true
        this.isDownloadSources = true
    }
}

//Set Java to version 1.8
java {
    this.sourceCompatibility = JavaVersion.VERSION_1_8
    this.targetCompatibility = JavaVersion.VERSION_1_8
}

//Set standard encoding
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

//Add extra sources here
sourceSets.getByName("main") {
    java.srcDir("src/main/java")
    java.srcDir("AVRcore/src")
}

//Load Minecraft Version
val Project.minecraft: UserExtension
    get() = extensions.getByName<UserExtension>("minecraft")

//TODO Delete this! This exists to load the configs from the real properties file, which is needed for Jenkins to build
//Gradle will load gradle.properties from it's home, it's install, and the project folder. Clearly whoever setup the
//Jenkins jar signing needs to be reminded of this!
val prop = Properties()
prop.load(FileInputStream("real.gradle.properties"))
val propSign = Properties()
propSign.load(FileInputStream("gradle.properties"))

//TODO Delete
val projectVersion: String = prop.getProperty("projectVersion")
//TODO Uncomment
//val projectVersion: String by project

//Generates a hash for each new commit to differentiate versions
var commitHash = Runtime
        .getRuntime()
        .exec("git rev-parse --short HEAD")
        .let { process ->
            process.waitFor()
            val output = process.inputStream.use {
                it.bufferedReader().use(BufferedReader::readText)
            }
            process.destroy()
            output.trim()
        }

minecraft.version = "1.7.10-10.13.4.1614-1.7.10"
version = "$projectVersion-$commitHash"
group = "com.github.technus"

//Minecraft Block
configure<UserExtension> {
    //Replaces version inside the mod
    this.includes.addAll(
            arrayOf(
                    "Reference.java"
            )
    )
    this.replacements.putAll(
            mapOf(
                    Pair("GRADLETOKEN_VERSION", project.version)
            )
    )

    //This is sometimes called 'eclipse' instead
    this.runDir = "run"
}

repositories {
    mavenLocal()
    maven("https://gregtech.overminddl1.com/") { this.name = "GT6Maven" }
    maven("http://maven.ic2.player.to/") { this.name = "ic2" }
    maven("http://jenkins.usrv.eu:8081/nexus/content/repositories/releases/") { this.name = "UsrvDE/GTNH" }
    ivy {
        this.name = "gtnh_download_source_underscores"
        this.artifactPattern("http://downloads.gtnewhorizons.com/Mods_for_Jenkins/[module]_[revision].[ext]")
    }
    ivy {
        this.name = "gtnh_download_source"
        this.artifactPattern("http://downloads.gtnewhorizons.com/Mods_for_Jenkins/[module]-[revision].[ext]")
    }
    ivy {
        this.name = "BuildCraft"
        this.artifactPattern("http://www.mod-buildcraft.com/releases/BuildCraft/[revision]/[module]-[revision](-[classifier]).[ext]")
    }
    maven("http://maven.cil.li/") { this.name = "OpenComputers" }
    maven("http://default.mobiusstrip.eu/maven") { this.name = "Jabba" }
    maven("http://chickenbones.net/maven/") { this.name = "CodeChicken" }
    maven("http://www.ryanliptak.com/maven/") { this.name = "appleCore" }
    maven("https://jitpack.io")
}

dependencies {
    //Local Libraries
    compile(fileTree("libs") { this.include("*.jar") })

    //TODO Uncomment
    //Versions from properties
    //val ic2Version: String by project
    //val gt5uVersion: String by project
    //val yamcoreVersion: String by project
    //val opencomputersVersion: String by project
    //val computercraftVersion: String by project
    //val baublesVersion: String by project
    //val thaumcraftVersion: String by project
    //val codechickenlibVersion: String by project
    //val codechickencoreVersion: String by project
    //val neiVersion: String by project
    //val wailaVersion: String by project
    //val galacticraftVersion: String by project
    //val galacticGregVersion: String by project
    //val buildcraftVersion: String by project
    //val forestryVersion: String by project
    //val enderioVersion: String by project
    //val enderCoreVersion: String by project

    //TODO Delete
    val ic2Version: String = prop.getProperty("ic2Version")
    val gt5uVersion: String = prop.getProperty("gt5uVersion")
    val yamcoreVersion: String = prop.getProperty("yamcoreVersion")
    val opencomputersVersion: String = prop.getProperty("opencomputersVersion")
    val computercraftVersion: String = prop.getProperty("computercraftVersion")
    val baublesVersion: String = prop.getProperty("baublesVersion")
    val thaumcraftVersion: String = prop.getProperty("thaumcraftVersion")
    val codechickenlibVersion: String = prop.getProperty("codechickenlibVersion")
    val codechickencoreVersion: String = prop.getProperty("codechickencoreVersion")
    val neiVersion: String = prop.getProperty("neiVersion")
    val wailaVersion: String = prop.getProperty("wailaVersion")
    val galacticraftVersion: String = prop.getProperty("galacticraftVersion")
    val galacticGregVersion: String = prop.getProperty("galacticGregVersion")
    val buildcraftVersion: String = prop.getProperty("buildcraftVersion")
    val forestryVersion: String = prop.getProperty("forestryVersion")
    val enderioVersion: String = prop.getProperty("enderioVersion")
    val enderCoreVersion: String = prop.getProperty("enderCoreVersion")

    //Hard Dependencies
    compile("net.industrial-craft:industrialcraft-2:$ic2Version:dev")
    compile("com.github.GTNewHorizons:GT5-Unofficial:$gt5uVersion:dev"){
        exclude("net.industrial-craft", "industrialcraft-2")
    }
    compile("eu.usrv:YAMCore:$yamcoreVersion:deobf")

    //Compile Dependencies
    compileOnly("li.cil.oc:OpenComputers:$opencomputersVersion:dev")
    compileOnly("dan200.computercraft:ComputerCraft:$computercraftVersion")
    compile("com.azanor.baubles:Baubles:$baublesVersion:deobf")
    compile("thaumcraft:Thaumcraft:$thaumcraftVersion:dev")
    compile("codechicken:CodeChickenLib:$codechickenlibVersion:dev")
    compile("codechicken:CodeChickenCore:$codechickencoreVersion:dev")
    compile("codechicken:NotEnoughItems:$neiVersion:dev")

    //Optional Libraries for Testing
    runtimeOnly("mcp.mobius.waila:Waila:$wailaVersion")

    //runtimeOnly("micdoodle8.mods:MicdoodleCore:$galacticraftVersion:Dev")
    //runtimeOnly("micdoodle8.mods:GalacticraftCore:$galacticraftVersion:Dev")
    //runtimeOnly("micdoodle8.mods:Galacticraft-Planets:$galacticraftVersion:Dev")
    //runtimeOnly("com.github.GTNewHorizons:GalacticGregGT5:$galacticGregVersion")
    //runtimeOnly("com.mod-buildcraft:buildcraft:$buildcraftVersion:dev")
    //runtimeOnly("net.sengir.forestry:forestry_1.7.10:$forestryVersion:dev")
    //runtimeOnly("com.enderio.core:EnderCore:$enderCoreVersion:dev")
    //runtimeOnly("com.enderio:EnderIO:$enderioVersion:dev"){
    //    exclude("com.enderio.core", "EnderCore")
    //    exclude("mcp.mobius.waila", "Waila")
    //}
}

tasks.withType<Jar> {
    //Mark as outdated if versions change
    this.inputs.properties += "version" to project.version
    this.inputs.properties += "mcversion" to project.minecraft.version
    this.archiveBaseName.set("TecTech-${project.minecraft.version}")

    //Replace versions in mcmod.info
    this.filesMatching("/mcmod.info") {
        this.expand(
                mapOf(
                        "version" to project.version,
                        "mcversion" to project.minecraft.version
                )
        )
    }
}

//Load AVRcore
val submodulesUpdate by tasks.creating(Exec::class) {
    this.description = "Updates (and inits) git submodules"
    this.group = "Build Setup"
    this.commandLine("git", "submodule", "update", "--init", "--recursive", "--remote")
}
tasks.named("setupCIWorkspace") {
    dependsOn(":submodulesUpdate")
}
tasks.named("setupDevWorkspace") {
    dependsOn(":submodulesUpdate")
}
tasks.named("setupDecompWorkspace") {
    dependsOn(":submodulesUpdate")
}
tasks.named("compileJava") {
    dependsOn(":submodulesUpdate")
}
tasks.named("sourceMainJava") {
    dependsOn(":submodulesUpdate")
}

tasks.jar {
    //Needed for access transformer which allows nerfing hardness of blocks
    this.manifest.attributes(
            mapOf(
                    Pair("FMLAT", "tectech_at.cfg")
            )
    )
}

val sourcesJar by tasks.creating(Jar::class) {
    this.from(sourceSets.main.get().allSource)
    this.archiveClassifier.set("sources")
}

val devJar by tasks.creating(Jar::class) {
    this.from(sourceSets.main.get().output)
    this.archiveClassifier.set("dev")
}

artifacts {
    this.archives(sourcesJar)
    this.archives(devJar)
}

tasks.register("signJar") {
    dependsOn("reobf")
}

//TODO Fix, but technically it was never fully implemented
//signing {
//    sign(tasks["jar"])
//}
//
//tasks.named("signJar") {
//    allprojects {
//        extra["signing.keyId"] = propSign.getProperty("keyStoreAlias")
//        extra["signing.secretKeyRingFile"] = propSign.getProperty("keyStore")
//        extra["signing.password"] = propSign.getProperty("keyStorePass")
//    }
//    dependsOn(":reobf")
//}