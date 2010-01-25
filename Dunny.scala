// vim:smarttab:expandtab:ts=4 sw=4
package org.chilon.dunny

import java.io.DataOutputStream

class Source {
    def step(time: Float): Float = 0

    def +(s: Source) = new Add(this, s)
    def +(s: Float) = new Add(this, new Constant(s))

    def *(s: Source) = new Mul(this, s)
    def *(s: Float) = new Mul(this, new Constant(s))
}
class Constant(v: Float) extends Source{
    var value = v
    override def step(time: Float) = value
}
class Phasor(f: Source) extends Source{
    var frequency = f
    var phase = 0d
    override def step(time: Float): Float = {
        phase = (phase + frequency.step(time)*time)
        return phase.toFloat
    }
}
class SlowingPhasor(f: Source) extends Source{
    var frequency = f
    var phase = 0f
    override def step(time: Float): Float = {
        phase = (phase + frequency.step(time)*time)
        return phase.toFloat
    }
}
class Saw(p: Source) extends Source{
    var phase = p
    override def step(time: Float): Float = (phase.step(time)%1)*2-1
}
class Sin(p: Source) extends Source{
    var phase = p
    override def step(time: Float): Float = {
        return Math.sin(phase.step(time)*2*Math.Pi).toFloat
    }
}
class Sqr(p: Source) extends Source{
    var phase = p
    override def step(time: Float): Float = {
        return if(phase.step(time)%1<0.5) -1 else 1
    }
}
class Add(a:Source, b:Source) extends Source{
    var sourcea = a
    var sourceb = b
    override def step(time: Float): Float = {
        return sourcea.step(time) + sourceb.step(time)
    }
}
class Mul(a:Source, b:Source) extends Source{
    var sourcea = a
    var sourceb = b
    override def step(time: Float): Float = {
        return sourcea.step(time) * sourceb.step(time)
    }
}
class Chromatic(s:Source) extends Source{
    var source = s
    override def step(time: Float): Float = {
        return Math.pow(2, (source.step(time)/12)+7).toFloat
    }
}
class Sequencer(p: Source, s: Sequence) extends Source{
    var phase = p
    var sequence = s
    override def step(time: Float): Float = {
        return sequence.discrete(phase.step(time))
    }
}

class Sample (b: Float){
    var time: Float = 0
    val bitrate = b
    def increment: Float = {
        time+=1/bitrate
        return time
    }
}
class Sequence(s: Array[Float], r: Float){
    val sequence = s
    val rate = r

    def discrete(time: Float) = sequence((time*rate).toInt%sequence.length)

    def linear(time: Float): Float = {
        var position = sequence((time*rate).toInt % sequence.length)
        var next = sequence((time*rate + 1).toInt % sequence.length)
        var ratio = (time * rate) % 1

        return (position*(1-ratio) + next*ratio)
    }
}

object Dunny {
    val BITRATE = 44100f
    val SAMPLELENGTH = 1/BITRATE
    val LENGTH = 256
    def main(args: Array[String]) {
        var b = new DataOutputStream(System.out)
        var s = new Sample(BITRATE)
        var notes = new Sequence(Array(0, 5, 7, 5, 0, 0, 7, 12, 19), 4)
        var notes2 = new Sequence(Array(0, 7, 12, 0), 1)
        var notes3 = new Sequence(Array(0, 12, 19, 0, 7), 2)
        var key = new Sequence(Array(0, 3, -2, 1), 0.25f)
        var prev = 0f
        
        var output =
            sqrwave(
                chromatic(
                    add(
                        seq(key),
                        seq(notes)
                    )
                )
            ) +
            sawwave(
                chromatic(
                    add(
                        seq(key),
                        seq(notes2)
                    )
                ) * 2
            )

        while (s.increment < LENGTH) {
            var mix = 0f
            mix += output.step(SAMPLELENGTH)
            b.writeFloat(mix*0.25f)
            b.flush()
            prev = s.time
        }
    }
       
    def const(v: Float): Source = return new Constant(v);
    def sawwave(freq: Source): Source = return new Saw(new Phasor(freq))
    def sqrwave(freq: Source): Source = return new Sqr(new Phasor(freq))
    def sinwave(freq: Source): Source = return new Sin(new Phasor(freq))
    def add(a: Source, b: Source): Source = return new Add(a, b);
    def chromatic(pitch: Source): Source = return new Chromatic(pitch);
    def noteseq(notes: Sequence): Source = return chromatic(new Sequencer(new Phasor(new Constant(1)), notes))
    def seq(notes: Sequence): Source = return new Sequencer(new Phasor(new Constant(1)), notes)
}
