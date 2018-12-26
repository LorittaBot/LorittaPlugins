package net.perfectdreams.loritta.commands.minecraft

enum class SignType(val localizedNames: Array<String>) {
    DARK_OAK( // Em primeiro pois "carvalho" será detectado antes de "carvalho escuro", mesmo que essa esteja em primeiro
        arrayOf(
            "dark oak", "carvalho escuro"
        )
    ),
    OAK(
        arrayOf(
            "oak", "carvalho"
        )
    ),
    SPRUCE(
        arrayOf(
            "spruce", "pinheiro"
        )
    ),
    BIRCH(
        arrayOf(
            "birch", "eucalipto"
        )
    ),
    JUNGLE(
        arrayOf(
            "jungle", "floresta"
        )
    ),
    ACACIA(
        arrayOf(
            "acacia", "acácia"
        )
    )
}