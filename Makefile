.PHONY: dunny raw run get_resources mp3

default: run

COMPILER = $(if ${FSC_BROKEN},scalac,fsc)

dunny: Dunny.scala Sequence.scala Sequencer.scala Waves.scala \
	   Operators.scala Sink.scala Musical.scala Source.scala \
	   AudioFile.scala
	@${COMPILER} $^

raw:
	@scala -classpath . org.chilon.dunny.Dunny

mp3: dunny
	@version=`grep VERSION Dunny.scala | head -n1 | sed 's/.*"\(.*\)".*/\1/'`; \
	name=`grep VERSION_NAME Dunny.scala | head -n1 | sed 's/.*"\(.*\)".*/\1/'`; \
	name="dunny - $$version - $$name"; \
	scala -classpath . org.chilon.dunny.Dunny | sox -t raw -r 44100 -e float -b32 -B - "$$name.mp3"; \
	id3v2 -a chilon -A 'dunny sings the blues' -t "$$name" -y 2010 -g 0 -c 'This music is 58.7619 seconds long, please enjoy it whilst relaxing.' -T $${version#0.} "$$name.mp3"

run: dunny
	@scala -classpath . org.chilon.dunny.Dunny | play 2> /dev/null -t raw -r 44100 -B -b32 -e float -

get_resources:
	wget -c http://chilon.net/~james/dunny/sample1.mp3
