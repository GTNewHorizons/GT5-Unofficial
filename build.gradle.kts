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
        maven {
            name = "forge"
            url = uri("http://files.minecraftforge.net/maven")
        }
        maven {
            name = "jitpack"
            url = uri("https://jitpack.io")
        }
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
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

val majorUpdate : String by project
val minorUpdate : String by project
val buildNumber : String by project

version = "$majorUpdate.$minorUpdate.$buildNumber"
group = "com.github.bartimaeusnek.bartworks"

//minecraft block
configure<UserExtension>{
    version = "1.7.10-10.13.4.1614-1.7.10"
    replaceIn("MainMod.java")
    replaceIn("API_REFERENCE.java")
    replace("@version@", project.version)
    val apiVersion : String by project
    replace("@apiversion@", apiVersion)
    runDir = "run"
}

repositories {
    mavenLocal()
    maven {
        name = "gt"
        url = uri("https://gregtech.overminddl1.com/")
    }
    maven {
        name = "ic2"
        url = uri("http://maven.ic2.player.to/")
    }
    maven { // GalacticGreg, YAMCore,..
        name = "UsrvDE"
        url = uri("http://jenkins.usrv.eu:8081/nexus/content/repositories/releases/")
    }
    ivy {
        name = "gtnh_download_source_stupid_underscore_typo"
        artifactPattern("http://downloads.gtnewhorizons.com/Mods_for_Jenkins/[module]_[revision].[ext]")
    }
    ivy {
        name = "gtnh_download_source"
        artifactPattern("http://downloads.gtnewhorizons.com/Mods_for_Jenkins/[module]-[revision].[ext]")
    }
    maven {
        name = "OC repo"
        url = uri("http://maven.cil.li/")
    }
    maven {
        name = "jabba"
        url = uri("http://default.mobiusstrip.eu/maven")
    }
    maven {
        name = "chickenbones"
        url = uri("http://chickenbones.net/maven/")
    }
    maven {
        name = "Apple"
        url = uri("http://www.ryanliptak.com/maven/")
    }
    ivy {
        name = "BuildCraft"
        artifactPattern("http://www.mod-buildcraft.com/releases/BuildCraft/[revision]/[module]-[revision](-[classifier]).[ext]")
    }
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    //Needed properties
    val ic2Version : String by project
    val galacticraftVersion : String by project
    val applecoreVersion : String by project
    val enderCoreVersion : String by project
    val enderioVersion : String by project
    //hard deps
    compile("net.industrial-craft:industrialcraft-2:$ic2Version:dev")
    //compile("com.github.GTNH2:GT5-Unofficial:experimental-SNAPSHOT:dev") //broken jitpack.io dep
    //soft deps
    compileOnly("com.azanor.baubles:Baubles:1.7.10-1.0.1.10:deobf")
    compileOnly("thaumcraft:Thaumcraft:1.7.10-4.2.3.5:dev")
    compileOnly("mods.railcraft:Railcraft_1.7.10:9.12.3.0:dev")
    compileOnly("micdoodle8.mods:MicdoodleCore:$galacticraftVersion:Dev")
    compileOnly("micdoodle8.mods:GalacticraftCore:$galacticraftVersion:Dev")
    compileOnly("micdoodle8.mods:Galacticraft-Planets:$galacticraftVersion:Dev")
    compileOnly("li.cil.oc:OpenComputers:MC1.7.10-1.5.+:api")
    compileOnly("net.sengir.forestry:forestry_1.7.10:4.2.16.64:dev")
    compileOnly("com.github.GTNewHorizons:GalacticGregGT5:master-SNAPSHOT")
    //compileOnly(fileTree(dir = "libs", include = "*.jar"))

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

val Project.minecraft : UserExtension
    get() = extensions.getByName<UserExtension>("minecraft")

tasks.withType<Jar> {
    // this will ensure that this task is redone when the versions change.
    inputs.properties += "version" to project.version
    inputs.properties += "mcversion" to project.minecraft.version
    archiveBaseName.set("bartworks[${project.minecraft.version}]")

    // replace stuff in mcmod.info, nothing else
    filesMatching("/mcmod.info") {
        expand(mapOf(
                "version" to project.version,
                "mcversion" to project.minecraft.version
        ))
    }
}

tasks.jar {
    exclude ("assets/gregtech/textures/items/materialicons/copy.bat")
    exclude ("assets/gregtech/textures/blocks/materialicons/copy.bat")
    manifest {
        attributes["FMLCorePlugin"] = "com.github.bartimaeusnek.ASM.BWCorePlugin"
        attributes["FMLCorePluginContainsFMLMod"] = "true"
    }
}

val apiJar by tasks.creating(Jar::class) {
    from(sourceSets.main.get().output) {
        include("com/github/bartimaeusnek/bartworks/API/**")
        include("com/github/bartimaeusnek/bartworks/util/**")
        include("com/github/bartimaeusnek/bartworks/system/material/Werkstoff.class")
        include("com/github/bartimaeusnek/crossmod/thaumcraft/util/**")
    }
    archiveClassifier.set("API")
}

val sourcesJar by tasks.creating(Jar::class) {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

val devJar by tasks.creating(Jar::class) {
    from(sourceSets.main.get().output)
    archiveClassifier.set("dev")
}

artifacts {
    add("archives", apiJar)
    add("archives", sourcesJar)
    add("archives", devJar)
}