package org.chilon.dunny

import java.io.DataOutputStream

object Dunny {
    val BITRATE = 44100f
    val SAMPLELENGTH = 1/BITRATE
    val LENGTH = 256f
    def main(args: Array[String]) {
        var output = new DataOutputStream(System.out)
        var sample = Sample(BITRATE)
        var notes = Sequence(Array(0, 5, 7, 5, 0, 0, 7, 12, 19), 4)
        var notes2 = Sequence(Array(0, 7, 12, 0), 1)
        var notes3 = Sequence(Array(0, 0, 12, 12, 19, 19, 0, 0, 7, 7), 4)
        var key = Sequence(Array(0, 3, -2, 1), 0.25f)
        var thereminseq = linseq(notes3);
        var music =
            Random() *(sawwave(Constant(0.25f))*(-0.5f) + 0.5f) +
            sqrwave(Chromatic(seq(key) + seq(notes))) +
            sawwave(Chromatic(seq(key) + seq(notes2)) * 2) +
            sqrwave(Chromatic(seq(key) + thereminseq) * (sinwave(Constant(5.13127f))*0.1f+3))*0.4f +
            sqrwave(Chromatic(seq(key) + linseq(notes3)) * (sinwave(Constant(5.43f))*0.1f+3))*0.2f
        thereminseq.phase.asInstanceOf[Phasor].phase = 0.3//get one of them to change notes slightly sooner

        val CACHE_LENGTH = 1
        while (sample.increment < LENGTH) {
            val nextFlushPoint =
                if (sample.time + CACHE_LENGTH > LENGTH) LENGTH
                else sample.time + CACHE_LENGTH

            do {
                var mix = 0f
                mix += music.step(SAMPLELENGTH)
                output.writeFloat(mix*0.25f)
            } while (sample.increment < nextFlushPoint)

            output.flush()
        }
    }

    def sawwave(freq: Source) = Saw(Phasor(freq))
    def sqrwave(freq: Source) = Square(Phasor(freq))
    def sinwave(freq: Source) = Sin(Phasor(freq))
    def noteseq(notes: Sequence) = Chromatic(Sequencer(Phasor(Constant(1)), notes))
    def seq(notes: Sequence) = Sequencer(Phasor(Constant(1)), notes)
    def linseq(notes: Sequence) = LinearSequencer(Phasor(Constant(1)), notes)
}

// vim:smarttab:expandtab:ts=4 sw=4
