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
}

class SourceWithLength(l:Int)  extends Source {
    val length = l
}

// vim:smarttab:expandtab:ts=4 sw=4
