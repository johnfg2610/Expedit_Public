package io.github.johnfg10.expeditweb

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.security.Principal

@Controller
class ExpeditAdmin{
    companion object {
        val logger = LoggerFactory.getLogger(ExpeditAdmin::class.java)
    }

    @RequestMapping("/containermanager")
    fun ContainerManager(user: Principal, model: Model): String {
        if (ExpeditWebApplication.Companion.docker == null)
            throw IllegalStateException("Docker can not be null")
        else if (ExpeditWebApplication.Companion.docker.listContainers() == null)
            model.addAttribute("containersExist", false)
        else if (ExpeditWebApplication.Companion.docker.listContainers().isEmpty()){
            model.addAttribute("containersExist", false)
        }else{
            model.addAttribute("containersExist", true)
            model.addAttribute("", ExpeditWebApplication.docker.listContainers())
        }
        return "admin/ContainerManager"
    }

    @RequestMapping("/dockerinfo")
    fun ContainerInfo(user: Principal, model: Model): String {
        if (ExpeditWebApplication.docker != null){
            return ExpeditWebApplication.docker.info().toString()
        }
        return "unknown"
    }

/*    @RequestMapping("/health")
    fun HealthReport(user: Principal, model: Model): String {
        if (ExpeditWebApplication.rmiManager == null)
            throw IllegalStateException("RMI manager can not be null")

        try {
            val health = ExpeditWebApplication.rmiManager!!.expeditCommunicationStub.getHealthInfo()
            model.addAttribute("isDiscordReady", health.isReady)
            model.addAttribute("isDiscordLoggedIn", health.loginStatus)
            model.addAttribute("freeMemory", health.freeMemory)
            model.addAttribute("usedMemory", health.totalMemory-health.freeMemory)
            model.addAttribute("totalMemory", health.totalMemory)
            return "admin.health"
        }catch (e: RemoteException){
            throw e
        }
    }

    @RequestMapping("/initcontainer")
    fun initalizeExpeditContainer(model: Model): String {
        if (ExpeditWebApplication.getExpeditContain() != null){
            throw IllegalStateException("The contain has already been initalized")
        }
        ExpeditWebApplication.Companion.setExpeditContain(ExpeditContainer(ExpeditWebApplication.expeditConfig.expeditLocation, ExpeditWebApplication.expeditConfig.expeditExternConfigLocation))
        if (ExpeditWebApplication.Companion.getExpeditContain() != null)
            println("expedit contain is now present")

        runBlocking {
            delay(10, TimeUnit.SECONDS)
            ExpeditWebApplication.rmiManager = RMIManager()
        }
        return "admin/initcontainer"

    }*/

/*    @RequestMapping("/setconfig", method = [RequestMethod.GET])
    fun setConfig(@RequestParam(name = "configname", required = false) configName: String?, @RequestParam(name = "configvalue",  required = false) configValue: String?, user: Principal, model: Model): String {
        if (configName == null || configValue == null)
            return "admin/setconfig"

        val config = ExpeditWebApplication.expeditConfig

        when(configName){
            "expeditlocation" -> {
                config.expeditLocation = configValue
            }
            "expeditconfiglocation" -> {
                config.expeditExternConfigLocation = configValue
            }
        }
        ExpeditWebApplication.setExpeditConfiguration(config)

        return "admin/setconfig"
    }*/
}

data class containerInfo(val ID: Int, val created: Long, val portInfo: String)