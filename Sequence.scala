package org.chilon.dunny

class Sequence(s: Array[Float], r: Float) {
    val sequence = s
    val rate = r

    def discrete(time: Float) = sequence((time*rate).toInt%sequence.length)

    def linear(time: Float): Float = {
        var position = sequence((time*rate).toInt % sequence.length)
        var next = sequence((time*rate + 1).toInt % sequence.length)
        var ratio = (time * rate) % 1

        (position*(1-ratio) + next*ratio)
    }
}

object Sequence {
    def apply(s: Array[Float], r: Float) = new Sequence(s, r)
}

// vim:smarttab:expandtab:ts=4 sw=4
