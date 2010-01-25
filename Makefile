default: run

COMPILER = $(if ${FSC_BROKEN},scalac,fsc)

dunny: Dunny.scala
	@${COMPILER} $^

raw:
	@scala -classpath . org.chilon.dunny.Dunny

run: dunny
	@scala -classpath . org.chilon.dunny.Dunny | play 2> /dev/null -t raw -r 44100 -B -b 32 -e float -
