/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import net.minecraftforge.gradle.user.UserExtension

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
}

apply(plugin = "forge")

idea {
    module {
        this.isDownloadJavadoc = true
        this.isDownloadSources = true
    }
}

java {
    this.sourceCompatibility = JavaVersion.VERSION_1_8
    this.targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

val majorUpdate: String by project
val minorUpdate: String by project
val buildNumber: String by project

version = "$majorUpdate.$minorUpdate.$buildNumber"
group = "com.github.bartimaeusnek.bartworks"

//minecraft block
configure<UserExtension> {
    this.version = "1.7.10-10.13.4.1614-1.7.10"
    this.includes.addAll(
            arrayOf(
                    "MainMod.java",
                    "API_REFERENCE.java"
            )
    )
    val apiVersion: String by project
    this.replacements.putAll(
            mapOf(
                Pair("@version@", project.version),
                Pair("@apiversion@", apiVersion)
            )
    )
    this.runDir = "run"
}

repositories {
    mavenLocal()
    maven("https://gregtech.overminddl1.com/") { this.name = "GT6Maven" }
    maven("http://maven.ic2.player.to/") { this.name = "ic2" }
    maven("http://jenkins.usrv.eu:8081/nexus/content/repositories/releases/") { this.name = "UsrvDE/GTNH" }
    ivy {
        this.name = "gtnh_download_source_stupid_underscore_typo"
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
    //Needed properties
    val ic2Version: String by project
    val galacticraftVersion: String by project
    val applecoreVersion: String by project
    val enderCoreVersion: String by project
    val enderioVersion: String by project
    //hard deps
    compile("net.industrial-craft:industrialcraft-2:$ic2Version:dev")
    //jitpack
    compile("com.github.GTNewHorizons:GT5-Unofficial:experimental-SNAPSHOT:dev")
    //soft deps
    compileOnly("com.azanor.baubles:Baubles:1.7.10-1.0.1.10:deobf")
    compileOnly("thaumcraft:Thaumcraft:1.7.10-4.2.3.5:dev")
    compileOnly("mods.railcraft:Railcraft_1.7.10:9.12.3.0:dev")
    compileOnly("micdoodle8.mods:MicdoodleCore:$galacticraftVersion:Dev")
    compileOnly("micdoodle8.mods:GalacticraftCore:$galacticraftVersion:Dev")
    compileOnly("micdoodle8.mods:Galacticraft-Planets:$galacticraftVersion:Dev")
    compileOnly("li.cil.oc:OpenComputers:MC1.7.10-1.5.+:api")
    compileOnly("net.sengir.forestry:forestry_1.7.10:4.2.16.64:dev")
    //jitpack
    compileOnly("com.github.GTNewHorizons:GalacticGregGT5:master-SNAPSHOT")
    //compileOnly("com.github.Technus:TecTech:BassAddons-SNAPSHOT")
    //Files
    compileOnly(fileTree("libs") { this.include("*.jar") })

    //CoreLibs for compile-age
    //compileOnly("com.github.GTNH2:Yamcl:master-SNAPSHOT") //broken jitpack.io dep
    compileOnly("applecore:AppleCore:$applecoreVersion:api")
    compileOnly("com.enderio.core:EnderCore:$enderCoreVersion:dev")
    compileOnly("com.enderio:EnderIO:$enderioVersion:dev")

    //NEI
    compile("codechicken:CodeChickenLib:1.7.10-1.1.3.140:dev")
    compile("codechicken:CodeChickenCore:1.7.10-1.0.7.47:dev")
    compile("codechicken:NotEnoughItems:1.7.10-1.0.5.120:dev")
}

val Project.minecraft: UserExtension
    get() = extensions.getByName<UserExtension>("minecraft")

tasks.withType<Jar> {
    // this will ensure that this task is redone when the versions change.
    this.inputs.properties += "version" to project.version
    this.inputs.properties += "mcversion" to project.minecraft.version
    this.archiveBaseName.set("bartworks[${project.minecraft.version}]")

    // replace stuff in mcmod.info, nothing else
    this.filesMatching("/mcmod.info") {
        this.expand(
                mapOf(
                    "version" to project.version,
                    "mcversion" to project.minecraft.version
                )
        )
    }
}

tasks.jar {
    this.exclude(
            "assets/gregtech/textures/items/materialicons/copy.bat",
            "assets/gregtech/textures/blocks/materialicons/copy.bat"
    )
    this.manifest.attributes(
                mapOf(
                        Pair("FMLCorePlugin","com.github.bartimaeusnek.ASM.BWCorePlugin"),
                        Pair("FMLCorePluginContainsFMLMod","true")
                )
        )
}

val apiJar by tasks.creating(Jar::class) {
    this.from(sourceSets.main.get().output) {
        this.include(
                "com/github/bartimaeusnek/bartworks/API/**",
                "com/github/bartimaeusnek/bartworks/util/**",
                "com/github/bartimaeusnek/bartworks/system/material/Werkstoff.class",
                "com/github/bartimaeusnek/crossmod/thaumcraft/util/**"
        )
    }
    this.archiveClassifier.set("API")
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
    this.archives(apiJar)
    this.archives(sourcesJar)
    this.archives(devJar)
}