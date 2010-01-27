package org.chilon.dunny

class Sample(b: Float) {
    var time: Float = 0
    val bitrate = b
    def increment: Float = {
        time+=1/bitrate
        time
    }
}

object Sample {
    def apply(b: Float) = new Sample (b)
}

// vim:smarttab:expandtab:ts=4 sw=4
