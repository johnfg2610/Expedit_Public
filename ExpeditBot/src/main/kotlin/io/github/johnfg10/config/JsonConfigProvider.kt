package io.github.johnfg10.config

import com.google.gson.Gson
import io.github.johnfg10.ExpeditConfig
import java.io.File

class JsonConfigProvider(val file: File) : IConfigProvider {

    init {
        if (!file.exists()){
            file.createNewFile()
            file.writeText(Gson().toJson(ExpeditConfig.getDefault()))
        }
    }

    override fun getConfig(): ExpeditConfig {
        return Gson().fromJson<ExpeditConfig>(file.readText(), ExpeditConfig::class.java)
    }
}