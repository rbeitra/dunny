.PHONY: dunny raw run get_resources

default: run

COMPILER = $(if ${FSC_BROKEN},scalac,fsc)

dunny: Dunny.scala Sequence.scala Sequencer.scala Waves.scala \
	   Operators.scala Sample.scala Musical.scala Source.scala \
	   AudioFile.scala
	@${COMPILER} $^

raw:
	@scala -classpath . org.chilon.dunny.Dunny

run: dunny
	@scala -classpath . org.chilon.dunny.Dunny | play 2> /dev/null -t raw -r 44100 -B -b 32 -e float -

get_resources:
	wget -c http://chilon.net/~james/dunny/sample1.mp3
