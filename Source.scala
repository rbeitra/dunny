package org.chilon.dunny

class Source {
    def step(time: Float): Float = 0

    def +(s: Source) = Add(this, s)
    def +(s: Float) = Add(this, Constant(s))

    def *(s: Source) = Multiply(this, s)
    def *(s: Float) = Multiply(this, Constant(s))
}

// vim:smarttab:expandtab:ts=4 sw=4
