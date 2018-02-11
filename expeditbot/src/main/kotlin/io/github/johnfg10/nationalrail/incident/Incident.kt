package io.github.johnfg10.nationalrail.incident

import io.github.johnfg10.nationalrail.common.Affect
import io.github.johnfg10.nationalrail.common.ChangeHistory
import io.github.johnfg10.nationalrail.common.InfoLink
import java.util.*

data class Incident
(
        @JvmField val creationTime: Date,
        @JvmField val changeHistory: ChangeHistory,
        @JvmField val participantRef: String,
        @JvmField val incidentNumber: String,
        @JvmField val version: String,
        @JvmField val source: String,
        @JvmField val twitterHashtag: String,
        @JvmField val validityPeriods: List<ValidityPeriod>,
        @JvmField val planned: Boolean,
        @JvmField val summary: String,
        @JvmField val description: String,
        @JvmField val infoLinks: List<InfoLink>,
        @JvmField val affect: Affect,
        @JvmField val clearIncident: Boolean,
        @JvmField val incidentPriority: Int,
        @JvmField val POSummary: String
)