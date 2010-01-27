package org.chilon.dunny

class Sin(var phase: Source) extends LoopedSource {
    override def step(time: Float): Float = {
        Math.sin(phase.step(time)*2*Math.Pi).toFloat
    }
}

object Sin {
    def apply(s: Source) = new Sin(s)
}

class Square(var phase: Source) extends LoopedSource {
    override def step(time: Float): Float = {
        if(phase.step(time)%1<0.5) -1 else 1
    }
}

object Square {
    def apply(s: Source) = new Square(s)
}

class Saw(var phase: Source) extends LoopedSource {
    override def step(time: Float): Float = (phase.step(time)%1)*2-1
}

object Saw {
    def apply(s: Source) = new Saw(s)
}

// vim:smarttab:expandtab:ts=4 sw=4
