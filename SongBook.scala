package org.chilon.dunny

import collection.immutable.HashMap

object SongBook {
    val BITRATE = 44100

    def frequence():SourceWithLength = {
        val secondsInABar = 8f
        val secondsInAnIteration = secondsInABar * 2

        def sawwave(freq: Source) = Saw(Phasor(freq))
        def sqrwave(freq: Source) = Square(Phasor(freq))
        def sinwave(freq: Source) = Sin(Phasor(freq))
        def noteseq(notes: Sequence) = Chromatic(Sequencer(Phasor(Constant(1)), notes))
        def seq(notes: Sequence) = Sequencer(Phasor(Constant(1)), notes)
        def linseq(notes: Sequence) = LinearSequencer(Phasor(Constant(1)), notes)

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
            sqrwave(Chromatic(seq(key) + linseq(bass) + Random())) +
            sqrwave(Chromatic(seq(key) + linseq(crash)) * (Random() / 3)) +
            sqrwave(Chromatic(seq(key) + seq(notes))) +
            sawwave(Chromatic(seq(key) + seq(notes2)) * 2) +
            sqrwave(Chromatic(seq(key) + thereminseq) * (sinwave(Constant(5.13127f))*0.1f+3))*0.4f +
            sqrwave(Chromatic(seq(key) + linseq(notes3)) * (sinwave(Constant(5.43f))*0.1f+3))*0.2f

        thereminseq.phase.asInstanceOf[Phasor].phase = 0.3//get one of them to change notes slightly sooner

        ((intro length secondsInAnIteration) ++
        ((intro length secondsInAnIteration) ** 1.2f) ++
        ((intro length secondsInAnIteration) ** 1.4f) ++
        ((intro length secondsInAnIteration) ** 1.6f) ++
        ((intro length secondsInAnIteration) ** 2.0f)) * 0.17f
    }

    def streetbeat():SourceWithLength = {
        var file1 = AudioFile("sample1.mp3", BITRATE)
        return file1.clip(0f, 30f)
    }

    def find(key:String):SourceWithLength = {
        val lookup = HashMap[String, SourceWithLength](
            "0.3" -> streetbeat()
        )

        return lookup.get(key).getOrElse {
            val lastDot = key.lastIndexOf('.')
            if (lastDot == -1)
                frequence()
            else
                find(key.substring(0, lastDot))
        }
    }
}

// vim:smarttab:expandtab:ts=4 sw=4
