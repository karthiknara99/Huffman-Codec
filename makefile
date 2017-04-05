JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $*.java

CLASSES = \
	Node.java \
	MyPriorityQueue.java \
	BinaryHeapPQ.java \
	CacheOptimizedFourWayHeapPQ.java \
	PairingHeapPQ.java \
	InbuiltPriorityQueue.java \
	HuffmanTree.java \
	encoder.java \
	decoder.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class