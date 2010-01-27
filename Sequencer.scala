package org.chilon.dunny

class Sequencer(var phase: Source, var sequence: Sequence) extends Source {
    override def step(time: Float): Float = {
        sequence.discrete(phase.step(time))
    }
}

object Sequencer {
    def apply(p: Source, s: Sequence) = new Sequencer(p, s)
}

class LinearSequencer(var phase: Source, var sequence: Sequence)
    extends Source
{
    override def step(time: Float): Float = {
        sequence.linear(phase.step(time))
    }
}

object LinearSequencer {
    def apply(p: Source, s: Sequence) = new LinearSequencer(p, s)
}

// vim:smarttab:expandtab:ts=4 sw=4
