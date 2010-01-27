package org.chilon.dunny

class Add(var sourcea:Source, var sourceb:Source) extends Source {
    override def step(time: Float): Float = {
        sourcea.step(time) + sourceb.step(time)
    }
}

object Add {
    def apply(a: Source, b: Source) = new Add(a, b)
}

class Subtract(var sourcea:Source, var sourceb:Source) extends Source {
    override def step(time: Float): Float = {
        sourcea.step(time) - sourceb.step(time)
    }
}

object Subtract {
    def apply(a: Source, b: Source) = new Subtract(a, b)
}

class Multiply(var sourcea:Source, var sourceb:Source) extends Source {
    override def step(time: Float): Float = {
        sourcea.step(time) * sourceb.step(time)
    }
}

class MultiplyWithLength(var sourcea:SourceWithLength, var sourceb:Source)
    extends SourceWithLength(sourcea.length)
{
    override def step(time: Float): Float = {
        idx += time
        sourcea.step(time) * sourceb.step(time)
    }
}

object Multiply {
    def apply(a:Source, b:Source) = new Multiply(a, b)
    def apply(a:SourceWithLength, b:Source) = new MultiplyWithLength(a, b)
}

class Divide(var sourcea:Source, var sourceb:Source) extends Source {
    override def step(time: Float): Float = {
        sourcea.step(time) / sourceb.step(time)
    }
}

object Divide {
    def apply(a:Source, b:Source) = new Divide(a, b)
}

// vim:smarttab:expandtab:ts=4 sw=4
