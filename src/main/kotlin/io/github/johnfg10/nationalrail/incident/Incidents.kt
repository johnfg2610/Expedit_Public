package io.github.johnfg10.nationalrail.incident

import org.simpleframework.xml.Root

@Root(name = "Incidents")
data class Incidents(val incidents: List<Incident>)