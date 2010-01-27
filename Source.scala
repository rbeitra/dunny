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

class SourceWithLengthAdaptor[T<:Source](s:T, l:Float) extends SourceWithLength(l) {
    override def step(time: Float): Float = {
        idx += time
        super.step(time)
    }
}

class SourceWithLength(l:Float)  extends Source {
    val length = l
    var idx = 0f

    def reset:Unit = { idx = 0f }
    def ++(s: SourceWithLength) = Following(this, s)
}

class Following(a:SourceWithLength, b:SourceWithLength)
    extends SourceWithLength(a.length + b.length)
{
    var first = a
    var second = b
    var current = first

    override def step(time: Float): Float = {
        if (current.idx >= current.length) {
            if (current.eq(second)) {
                first.reset
                second.reset
                current = first
            }
            else current = second
        }

        current.step(time)
    }
}

object Following {
    def apply(a:SourceWithLength, b:SourceWithLength) = new Following(a, b)
}

// vim:smarttab:expandtab:ts=4 sw=4
