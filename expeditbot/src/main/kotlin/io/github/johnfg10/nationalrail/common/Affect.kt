package io.github.johnfg10.nationalrail.common

import io.github.johnfg10.nationalrail.common.Operator

data class Affect(val operators: List<Operator>, val routesEffected: List<String>)