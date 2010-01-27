package org.chilon.dunny

object Dunny {
    val VERSION = "0.2.1"
    val VERSION_NAME = "frequence"
    val VERSION_STRING = VERSION + " (" + VERSION_NAME + ")"

    def main(args: Array[String]) {
        var sink = Sink(SongBook.BITRATE.toFloat)
        System.err.println("dunny " + VERSION_STRING)

        val music = SongBook.frequence

        System.err.println("This music is " + music.length.toString + " seconds long, please enjoy it whilst relaxing.")

        sink.output(System.out, music * 0.17f)
        Console.out.close()
    }
}

// vim:smarttab:expandtab:ts=4 sw=4
