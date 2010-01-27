package org.chilon.dunny

class Add(a:Source, b:Source) extends Source {
    var sourcea = a
    var sourceb = b
    override def step(time: Float): Float = {
        sourcea.step(time) + sourceb.step(time)
    }
}

object Add {
    def apply(a: Source, b: Source) = new Add(a, b)
}

class Subtract(a:Source, b:Source) extends Source {
    var sourcea = a
    var sourceb = b
    override def step(time: Float): Float = {
        sourcea.step(time) - sourceb.step(time)
    }
}

object Subtract {
    def apply(a: Source, b: Source) = new Subtract(a, b)
}

class Multiply(a:Source, b:Source) extends Source {
    var sourcea = a
    var sourceb = b
    override def step(time: Float): Float = {
        sourcea.step(time) * sourceb.step(time)
    }
}

object Multiply {
    def apply(a:Source, b:Source) = new Multiply(a, b)
}

class Divide(a:Source, b:Source) extends Source {
    var sourcea = a
    var sourceb = b
    override def step(time: Float): Float = {
        sourcea.step(time) / sourceb.step(time)
    }
}

object Divide {
    def apply(a:Source, b:Source) = new Divide(a, b)
}

// vim:smarttab:expandtab:ts=4 sw=4
