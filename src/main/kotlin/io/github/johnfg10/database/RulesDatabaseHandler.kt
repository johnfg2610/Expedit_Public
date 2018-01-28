package io.github.johnfg10.database

import com.github.benmanes.caffeine.cache.*
import com.github.benmanes.caffeine.cache.stats.CacheStats
import io.github.johnfg10.Expedit
import org.h2.bnf.Rule
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import sx.blah.discord.Discord4J
import sx.blah.discord.api.DiscordStatus
import sx.blah.discord.api.IDiscordClient
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class RulesDatabaseHandler(val jdbc: String, val driver: String, val username: String?, val password: String?) {

    val defaultCache: LoadingCache<Long, RulesObj> = Caffeine.newBuilder().maximumSize(10000)
            .expireAfterWrite(2, TimeUnit.HOURS).refreshAfterWrite(10, TimeUnit.MINUTES).build({
        ConnectToDb()
        transaction {
            val ruleQry = RulesDatabase.select( RulesDatabase.guildId eq it)
            if (ruleQry.count() == 0)
                return@transaction null
            RulesObj(ruleQry.first()[RulesDatabase.isLinksEnabled], ruleQry.first()[RulesDatabase.isAttachmentsEnabled])
        }
    })

    val channelCache: LoadingCache<Long, RulesObj> = Caffeine.newBuilder().maximumSize(10000)
            .expireAfterWrite(2, TimeUnit.HOURS).refreshAfterWrite(10, TimeUnit.MINUTES).build({
        ConnectToDb()
        transaction {
            val ruleQry = RulesDatabase.select( RulesDatabase.channelId eq it)
            if (ruleQry.count() == 0)
                return@transaction null
            RulesObj(ruleQry.first()[RulesDatabase.isLinksEnabled], ruleQry.first()[RulesDatabase.isAttachmentsEnabled])
        }
    })

    val ignoreChannels: MutableList<Long> = mutableListOf()
    val ignoreDefaults: MutableList<Long> = mutableListOf()

    init {
        ConnectToDb()
        transaction { SchemaUtils.create(RulesDatabase) }

    }

    fun SetChannelRules(channelId: Long, rules: RulesObj){
        ConnectToDb()
        if (transaction { RulesDatabase.select(RulesDatabase.channelId eq channelId).count() } == 0){
            println("creating new")
            transaction {
                RulesDatabase.insert {
                    it[RulesDatabase.channelId] = channelId
                    it[RulesDatabase.isDefault] = false
                    it[RulesDatabase.isAttachmentsEnabled] = rules.isAttachmentsEnabled ?: false
                    it[RulesDatabase.isLinksEnabled] = rules.isLinksEnabled ?: false
                }
            }
        }else{
            println("updating existing")
            transaction {
                    RulesDatabase.update({RulesDatabase.channelId eq channelId}) {
                        if (rules.isLinksEnabled != null)
                            it[RulesDatabase.isLinksEnabled] = rules.isLinksEnabled!!
                        if (rules.isAttachmentsEnabled != null)
                            it[RulesDatabase.isAttachmentsEnabled] = rules.isAttachmentsEnabled!!
                    }

            }
        }

        if (ignoreChannels.contains(channelId))
            ignoreChannels.remove(channelId)

        channelCache.invalidate(channelId)
    }

    fun ConnectToDb(){
        if (username == null || password == null){
            Database.connect(jdbc, driver)
        }else{
            Database.connect(jdbc, driver, username, password)
        }
    }
}
