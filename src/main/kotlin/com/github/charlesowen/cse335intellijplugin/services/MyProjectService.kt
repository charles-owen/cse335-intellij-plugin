package com.github.charlesowen.cse335intellijplugin.services

import com.github.charlesowen.cse335intellijplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
