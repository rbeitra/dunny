package org.chilon.dunny

class Source {
    def step(time: Float): Float = 0

    def +(s: Source) = Add(this, s)
    def +(s: Float) = Add(this, Constant(s))

    def -(s: Source) = Subtract(this, s)
    def -(s: Float) = Subtract(this, Constant(s))

    def *(s: Source) = Multiply(this, s)
    def *(s: Float) = Multiply(this, Constant(s))

    def /(s: Source) = Divide(this, s)
    def /(s: Float) = Divide(this, Constant(s))

    def length(l: Float) = new SourceWithLengthAdaptor(this, l)
}

class SourceWithLengthAdaptor[T<:Source](var source:T, l:Float)
    extends SourceWithLength(l)
{
    override def step(time: Float): Float = {
        idx += time
        source.step(time)
    }
}

class SourceWithLength(val length:Float)  extends Source {
    var idx = 0f

    def reset:Unit = { idx = 0f }
    def ++(s: SourceWithLength) = Following(this, s)
    def **(f: Float) = Speed(this, f)
}

class Following(var first:SourceWithLength, var second:SourceWithLength)
    extends SourceWithLength(first.length + second.length)
{
    var current = first

    override def reset() {
        first.reset
        second.reset
        current = first
    }

    override def step(time: Float): Float = {
        idx += time

        if (current.idx >= current.length) {
            if (current.eq(second)) reset()
            else current = second
        }
        current.step(time)
    }
}

object Following {
    def apply(a:SourceWithLength, b:SourceWithLength) = new Following(a, b)
}

// vim:smarttab:expandtab:ts=4 sw=4
