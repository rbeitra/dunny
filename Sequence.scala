package org.chilon.dunny

class Addressable() {
    def discrete(time: Float): Float = 0;
    def linear(time: Float): Float = 0;
}

class Sequence(val sequence: Array[Float], val rate: Float) extends Addressable {
    override def discrete(time: Float): Float = sequence((time*rate).toInt%sequence.length)

    override def linear(time: Float): Float = {
        var position = sequence((time*rate).toInt % sequence.length)
        var next = sequence((time*rate + 1).toInt % sequence.length)
        var ratio = (time * rate) % 1

        (position*(1-ratio) + next*ratio)
    }
}

class Slice(val source: Addressable, val start: Float, val end: Float) extends Addressable {
    override def discrete(time: Float): Float = if(time >= start && time < end) source.discrete(time + start) else 0;
    override def linear(time: Float): Float = if(time >= start && time < end) source.linear(time + start) else 0;
}

class Loop(val source: Addressable, val start: Float, val end: Float) extends Addressable {
    val length = end - start;
    override def discrete(time: Float): Float = source.discrete(start + (time - start)%length)
    override def linear(time: Float): Float = source.linear(start + (time - start)%length)
}

class Appended(val sourcea: Addressable, val sourceb: Addressable, val switchtime: Float) extends Addressable {
    override def discrete(time: Float): Float = if(time < switchtime) sourcea.discrete(time) else sourceb.discrete(time - switchtime)
    override def linear(time: Float): Float = if(time < switchtime) sourcea.linear(time) else sourceb.linear(time - switchtime)
}

class TimeShift(val source: Addressable, val offset: Float) extends Addressable {
    override def discrete(time: Float): Float = source.discrete(time + offset);
    override def linear(time: Float): Float = source.linear(time + offset);
}

class Reverse(val source: Addressable) extends Addressable {
    override def discrete(time: Float): Float = source.discrete(-time);
    override def linear(time: Float): Float = source.linear(-time);
}


object Sequence {
    def apply(s: Array[Float], r: Float) = new Sequence(s, r)
}

// vim:smarttab:expandtab:ts=4 sw=4
