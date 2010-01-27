package org.chilon.dunny

import java.io.DataOutputStream

object Dunny {
    val VERSION = "0.1"
    val VERSION_NAME = "no regrets"
    val VERSION_STRING = VERSION + " (" + VERSION_NAME + ") - (c) 2010: Everyone"

    val BITRATE = 44100
    val SAMPLELENGTH = 1/BITRATE.toFloat
    var secondsInABar = 7.1f
    var secondsInAnIteration = secondsInABar * 2

    def main(args: Array[String]) {
        var output = new DataOutputStream(System.out)
        var sample = Sample(BITRATE.toFloat)
        var notes = Sequence(Array(0, 5, 7, 5, 0, 0, 7, 12, 19), 4)
        var notes2 = Sequence(Array(0, 7, 12, 0), 1)
        var notes3 = Sequence(Array(0, 0, 12, 12, 19, 19, 0, 0, 7, 7), 4)
        var bass = Sequence(Array(-12, -4), 0.125f)
        var crash = Sequence(Array(-10, -4), 0.125f)
        var key = Sequence(Array(0, 3, -2, 1), 0.25f)
        var thereminseq = linseq(notes3);

        var file1 = AudioFile("sample1.mp3", BITRATE)

        // ** means change speed factor by rhs
        // ++ means temporal adjoinment of lhs and rhs, only works on types
        //          that have an inherent length. A length can be given to a
        //          type by calling "length" on that type to give it a length.
        //          Silence(3.0f) == Silence() length 3.0f
        var intro =
            ((file1.clip(3.8f, 4.0f) ** 0.8f) ++ Silence(3f)) * 19f +
            Random() *((sawwave(Constant(0.25f)) / 2f) + 0.5f) +
            // sqrwave(Chromatic(seq(key) + linseq(bass))) +
            sqrwave(Chromatic(seq(key) + linseq(crash)) * (Random() / 3)) +
            sqrwave(Chromatic(seq(key) + seq(notes))) +
            sawwave(Chromatic(seq(key) + seq(notes2)) * 2) +
            sqrwave(Chromatic(seq(key) + thereminseq) * (sinwave(Constant(5.13127f))*0.1f+3))*0.4f +
            sqrwave(Chromatic(seq(key) + linseq(notes3)) * (sinwave(Constant(5.43f))*0.1f+3))*0.2f

        thereminseq.phase.asInstanceOf[Phasor].phase = 0.3//get one of them to change notes slightly sooner

        var music =
            (intro length secondsInAnIteration) ++
            ((intro length secondsInAnIteration) ** 1.2f) ++
            ((intro length secondsInAnIteration) ** 1.4f) ++
            ((intro length secondsInAnIteration) ** 1.6f) ++
            ((intro length secondsInAnIteration) ** 2.0f)

        System.err.println(VERSION_STRING)
        System.err.println("This music is " + music.length.toString + " seconds long, please enjoy it whilst relaxing.")

        val CACHE_LENGTH = 1
        while (sample.time < music.length) {
            val nextFlushPoint =
                if (sample.time + CACHE_LENGTH > music.length) music.length
                else sample.time + CACHE_LENGTH

            do {
                var mix = music.step(SAMPLELENGTH)
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
