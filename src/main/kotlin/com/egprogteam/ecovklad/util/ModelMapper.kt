package com.egprogteam.ecovklad.util

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.stereotype.Component

@Component
class ModelMapper : ModelMapper() {
    init {
        configuration.matchingStrategy = MatchingStrategies.STRICT
    }
}