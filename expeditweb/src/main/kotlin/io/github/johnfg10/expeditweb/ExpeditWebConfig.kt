package io.github.johnfg10.expeditweb

import com.beust.klaxon.Klaxon
import java.io.File

data class ExpeditWebConfig(var expeditLocation: String, var expeditExternConfigLocation: String){
    companion object {
        val configFile: File = File("./configs/webconfig.json")

        init {
            SetExpeditConfig(this.DefaultConfig(), true)
        }

        fun DefaultOrCurrent(): ExpeditWebConfig {
            if (configFile.exists()){
                val configNullable = Klaxon().parse<ExpeditWebConfig>(configFile)
                if (configNullable != null)
                    return configNullable
            }

            return DefaultConfig()
        }

        fun DefaultConfig(): ExpeditWebConfig{
            return ExpeditWebConfig("./expedit/expedit.jar", "./expedit/expdit.json")
        }

        fun SetExpeditConfig(config: ExpeditWebConfig, onlyWriteOnCreate: Boolean = false){
            if (!configFile.exists()){
                File("./configs").mkdirs()
                configFile.createNewFile()
                if (onlyWriteOnCreate){
                    configFile.writeText(Klaxon().toJsonString(config))
                }
            }

            if (!onlyWriteOnCreate)
                configFile.writeText(Klaxon().toJsonString(config))
        }
    }
}