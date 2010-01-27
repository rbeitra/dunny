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

class Random() extends Source {
    override def step(time: Float): Float = Math.random.toFloat*2 - 1;
}
object Random {
    def apply() = new Random
}

class Chromatic(s:Source) extends Source {
    var source = s
    override def step(time: Float): Float = {
        Math.pow(2, (source.step(time)/12)+7).toFloat
    }
}

object Chromatic {
    def apply(f: Source) = new Chromatic(f)
}

class Constant(v: Float) extends Source{
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

class SpeedWithLength(s:SourceWithLength, f:Float) extends SourceWithLength(s.length / f) {
    var source = s
    val factor = f

    override def step(time: Float):Float = {
        idx += time * factor
        s.step(time * factor)
    }
}

class Speed(s:Source, f:Float) extends Source {
    var source = s
    val factor = f

    override def step(time: Float):Float = {
        s.step(time * factor)
    }
}

object Speed {
    def apply(s:SourceWithLength, f:Float) = new SpeedWithLength(s, f)
    def apply(s:Source, f:Float) = new Speed(s, f)
}

// vim:smarttab:expandtab:ts=4 sw=4
