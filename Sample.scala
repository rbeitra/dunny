package org.chilon.dunny

import java.io.DataOutputStream
import java.io.OutputStream

class Sample(b: Float) {
    var time: Float = 0
    val bitrate = b
    val sampleLength = 1/bitrate

    def increment: Float = {
        time+=1/bitrate
        time
    }

    def output(s: OutputStream, music:SourceWithLength) {
        var output = new DataOutputStream(s)

        val CACHE_LENGTH = 0.1f
        while (time < music.length) {
            val nextFlushPoint =
                if (time + CACHE_LENGTH > music.length) music.length
                else time + CACHE_LENGTH

            do {
                var mix = music.step(sampleLength)
                // TODO: move this correction factor
                output.writeFloat(mix * 0.17f)
            } while (increment < nextFlushPoint)

            output.flush()
        }
    }

}

object Sample {
    def apply(b: Float) = new Sample (b)
}

// vim:smarttab:expandtab:ts=4 sw=4
