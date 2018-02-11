package io.github.johnfg10.database

import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database

object RulesDatabase : LongIdTable() {
    val guildId: Column<Long> = long("guildId").uniqueIndex()
    val channelId: Column<Long> = long("channelId").uniqueIndex()
    val isDefault: Column<Boolean> = bool("isDefault")
    val isLinksEnabled: Column<Boolean> = bool("isLinksEnabled")
    val isAttachmentsEnabled: Column<Boolean> = bool("isAttachmentsEnabled")
}