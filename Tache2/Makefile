JAVAC = javac
JAVA = java
JVM_FLAGS = -Xmx2g
DATA = ../dblp-2026-01-01.xml.gz
DTD = ../dblp.dtd
CP = -cp ".;.."
SOURCES = ../base/DblpPublicationGenerator.java ../Tache2.java
MAIN = Tache2

all: compile run

compile:
	$(JAVAC) $(SOURCES)

run:
	$(JAVA) $(JVM_FLAGS) $(CP) $(MAIN) $(DATA) $(DTD)

clean:
	rm -f *.class ../*.class ../base/*.class