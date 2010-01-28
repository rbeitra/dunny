.PHONY: dunny raw run get_resources mp3 test_sample1_sox

default: run

COMPILER = $(if ${FSC_BROKEN},scalac,fsc)

dunny: Dunny.scala Sequence.scala Sequencer.scala Waves.scala \
	   Operators.scala Sink.scala Musical.scala Source.scala \
	   AudioFile.scala SongBook.scala
	@${COMPILER} $^

raw:
	@scala -classpath . org.chilon.dunny.Dunny

get_version := `grep VERSION Dunny.scala | head -n1 | sed 's/.*"\(.*\)".*/\1/'`

mp3: dunny
	@version=${get_version}; \
	name=`grep VERSION_NAME Dunny.scala | head -n1 | sed 's/.*"\(.*\)".*/\1/'`; \
	name="dunny - $$version - $$name"; \
	scala -classpath . org.chilon.dunny.Dunny ${args} | sox -t raw -r 44100 -e float -b32 -B - "$$name.mp3"; \
	id3v2 -a chilon -A 'dunny sings the blues' -t "$$name" -y 2010 -g 0 -c 'This music is 58.7619 seconds long, please enjoy it whilst relaxing.' -T $${version#0.} "$$name.mp3"

run: dunny
	@version=${get_version}; \
	scala -classpath . org.chilon.dunny.Dunny $(if ${args},${args},${get_version}) | play 2> /dev/null -t raw -r 44100 -B -b32 -e float -

get_resources:
	wget -c http://chilon.net/~james/dunny/sample1.mp3

test_sample1_sox:
	sox sample1.mp3 -t raw -r 44100 -B -e float -b32 -c 1 - trim 0 4         | play -t raw -r 44100 -B -e float -b32 -c1 -
