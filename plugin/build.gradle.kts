taboolib {
    description {
        name("Quetiapine")
        desc("An advanced item plugin. ChoTen item management plugin.")
        contributors {
            // 作者名称
            name("AkaCandyKAngel")
            name("jhqwqmc")
        }
        dependencies {
            name("MythicMobs").optional(true)
            name("Chemdah").optional(true)
            name("ItemsAdder").optional(true)
            name("Oxaren").optional(true)
            name("Nexo").optional(true)
            name("CraftEngine").optional(true)
            name("PlaceholderAPI").optional(true)
            // name("ProtocolLib").optional(true)
            // 可选依赖.
            // name("XXX").optional(true)
        }
    }


    // Relocate 必须与META-INF/dependencies里面的relocate一致。
    // 同时，还需要修改io.github.zzzyyylllty.quetiapine.util.quetiapineLocalDependencyHelper.replaceTestTexts
    relocate("top.maplex.arim","io.github.zzzyyylllty.quetiapine.library.arim")
    relocate("ink.ptms.um","io.github.zzzyyylllty.quetiapine.library.um")
    // relocate("com.google", "io.github.zzzyyylllty.quetiapine.library.google")
    relocate("com.alibaba", "io.github.zzzyyylllty.quetiapine.library.alibaba")
    relocate("kotlinx.serialization", "kotlinx.serialization170")
    // relocate("de.tr7zw.changeme.nbtapi","io.github.zzzyyylllty.quetiapine.library.nbtapi")
    relocate("io.github.projectunified.uniitem","io.github.zzzyyylllty.quetiapine.library.uniitem")
    relocate("com.fasterxml.jackson","io.github.zzzyyylllty.quetiapine.library.jackson")
    relocate("com.mojang.datafixers","io.github.zzzyyylllty.quetiapine.library.datafixers")
    relocate("io.netty.handler.codec.http", "io.github.zzzyyylllty.quetiapine.library.http")
    relocate("io.netty.handler.codec.rtsp", "io.github.zzzyyylllty.quetiapine.library.rtsp")
    relocate("io.netty.handler.codec.spdy", "io.github.zzzyyylllty.quetiapine.library.spdy")
    relocate("io.netty.handler.codec.http2", "io.github.zzzyyylllty.quetiapine.library.http2")
    relocate("org.tabooproject.fluxon","io.github.zzzyyylllty.quetiapine.library.fluxon")
    relocate("com.github.benmanes.caffeine","io.github.zzzyyylllty.quetiapine.library.caffeine")
    relocate("org.kotlincrypto","io.github.zzzyyylllty.quetiapine.library.kotlincrypto")
//    relocate("com.oracle.truffle","io.github.zzzyyylllty.quetiapine.library.truffle")
//    relocate("org.graalvm.polyglot","io.github.zzzyyylllty.quetiapine.library.polyglot")
}

//tasks {
//    jar {
//        archiveFileName.set("${rootProject.name}-${archiveFileName.get().substringAfter('-')}")
//        rootProject.subprojects.forEach { from(it.sourceSets["main"].output) }
//    }
//}

tasks {

    val taboolibMainTask = named("taboolibMainTask")

    val baseJarFile = layout.buildDirectory.file("libs/${rootProject.name}-${rootProject.version}-Premium.jar")

    val freeJar by registering(Jar::class) {
        group = "build"
        description = "Generate FREE version jar by filtering premium classes"

        dependsOn(taboolibMainTask)

        archiveFileName.set("${rootProject.name}-${version}-Free.jar")

        // 从taboolibMainTask产物复制并过滤premium包
        from(zipTree(baseJarFile)) {
            exclude("io/github/zzzyyylllty/quetiapine/premium/*")
        }
    }

    named("build") {
        dependsOn(freeJar)
    }


    jar {
        archiveFileName.set("${rootProject.name}-${rootProject.version}-Premium.jar")
        rootProject.subprojects.forEach { from(it.sourceSets["main"].output) }
    }
}
