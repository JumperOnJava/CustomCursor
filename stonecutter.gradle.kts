plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "26.1-fabric" /* [SC] DO NOT EDIT */

stonecutter parameters {
    constants{
        val loader = node.metadata.project.substringAfterLast('-')
        match(loader, "fabric", "neoforge", "forge")
    }
}

tasks.register("template_buildAll") {
    group = "build"
    dependsOn(sc.tree.nodes.map { "${it.hierarchy}:build" })

    doLast {
        val rootBuildDir = layout.buildDirectory.asFile.get().resolve("libs");
        sc.tree.nodes.forEach { node ->
            val subprojectBuildDir = project(node.hierarchy.toString()).layout.buildDirectory.asFile.get()
            copy {
                from(fileTree("$subprojectBuildDir/libs").matching { include("*.jar") })
                into(rootBuildDir.resolve(node.name.split(":").last()))
            }
        }
    }
}
