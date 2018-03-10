package io.github.johnfg10.config

import io.github.johnfg10.ExpeditConfig

interface IConfigProvider {
    fun getConfig() : ExpeditConfig
}