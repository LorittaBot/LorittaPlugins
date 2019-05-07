package net.perfectdreams.loritta

import com.fasterxml.jackson.annotation.JsonCreator

class QuirkyConfig @JsonCreator constructor(
        val randomReactions: RandomReactionsConfig,
        val thankYouLori: ThankYouLoriConfig,
        val tioDoPave: TioDoPaveConfig
) {
    class RandomReactionsConfig @JsonCreator constructor(
            val enabled: Boolean,
            val maxBound: Int,
            val reactions: Map<Int, String>,
            val contextAwareReactions: List<ContextAwareReaction>
    ) {
        class ContextAwareReaction@JsonCreator constructor(
                val match: String,
                val chanceOf: Double,
                val reactions: List<String>
        )
    }

    class TioDoPaveConfig @JsonCreator constructor(
            val enabled: Boolean,
            val chance: Double
    )

    class ThankYouLoriConfig @JsonCreator constructor(
            val enabled: Boolean,
            val channelId: Long,
            val reactions: List<String>
    )
}