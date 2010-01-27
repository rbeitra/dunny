package org.chilon.dunny

class Phasor(s: Source) extends Source {
    var frequency = s
    var phase = 0d
    override def step(time: Float): Float = {
        phase = (phase + frequency.step(time)*time)
        phase.toFloat
    }
}

object Phasor {
    def apply(s: Source) = new Phasor(s)
}

class SlowingPhasor(s: Source) extends Source{
    var frequency = s
    var phase = 0f
    override def step(time: Float): Float = {
        phase = (phase + frequency.step(time)*time)
        phase.toFloat
    }
}

object SlowingPhasor {
    def apply(s: Source) = new SlowingPhasor(s)
}

class Random() extends LoopedSource {
    override def step(time: Float): Float = Math.random.toFloat*2 - 1;
}
object Random {
    def apply() = new Random
}

class Chromatic(s:Source) extends LoopedSource {
    var source = s
    override def step(time: Float): Float = {
        Math.pow(2, (source.step(time)/12)+7).toFloat
    }
}

object Chromatic {
    def apply(f: Source) = new Chromatic(f)
}

class Constant(v: Float) extends LoopedSource {
    var value = v
    override def step(time: Float) = value
}

object Constant {
    def apply(v: Float) = new Constant(v)
}

class Silence extends Constant(0)

object Silence {
    def apply():Silence = new Silence
    def apply(l: Float) = new SourceWithLengthAdaptor[Silence](new Silence, l)
}

class TempoWithLength(s:SourceWithLength, val factor:Float) extends SourceWithLength(s.length / factor) {
    var source = s

    override def reset() {
        super.reset
        s.reset
    }

    override def step(time: Float):Float = {
        idx += time
        s.step(time * factor)
    }
}

class Tempo(s:Source, f:Float) extends LoopedSource {
    var source = s
    val factor = f

    override def step(time: Float):Float = {
        s.step(time * factor)
    }
}

object Tempo {
    def apply(s:SourceWithLength, f:Float) = new TempoWithLength(s, f)
    def apply(s:Source, f:Float) = new Tempo(s, f)
}

// vim:smarttab:expandtab:ts=4 sw=4
