package org.chilon.dunny

import java.lang.ProcessBuilder
import java.lang.Process

import java.io.DataInputStream
import java.io.File

class AudioFile(p: String, b:Int) {
    val path = p
    val bitRate = b

    if (! new File(p).exists()) throw new Error("sample file does not exist, have you run `make get_resources'?")

    // open a pipe to sox sample.mp3 -t raw -r 44100 -b 32 -e float
    // output.raw and pass it to AudioFileClip
    def clip(offset: Float, length: Float):AudioFileClip = {
        var pb = new ProcessBuilder(
            "sox", path, "-t", "raw", "-r", bitRate.toString,
            "-B", "-e", "float", "-b", "32", "-c", "1", "-", "trim", offset.toString)

        var process = pb.start()
        var stream = new DataInputStream(process.getInputStream())
        val nSamples = (bitRate * length).toInt
        var data:Array[Float] = new Array(nSamples)
        for (i <- 0 to nSamples - 1) {
            // TODO: exception handling
            data(i) = stream.readFloat()
        }

        new AudioFileClip(data, bitRate)
    }
}

object AudioFile {
    def apply(p: String, b:Int) = new AudioFile(p, b)
}

class AudioFileClip(d:Array[Float], b:Int) extends SourceWithLength(d.size) {
    val data = d
    val bitRate = b
    var idx = 0f

    override def step(time: Float): Float = {
        idx += time
        data((idx * bitRate).toInt % data.size)
    }
}

// vim:smarttab:expandtab:ts=4 sw=4
