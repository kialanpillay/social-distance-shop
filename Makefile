JAVAC=/usr/bin/javac
.SUFFIXES: .java .class

SRCDIR=./socialDistanceShopSampleSolution
BINDIR=./bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR):$(SRCDIR) $<

CLASSES=PeopleCounter.class \
	GridBlock.class \
	CustomerLocation.class \
	CounterDisplay.class \
	Inspector.class \
	ShopGrid.class \
	ShopView.class \
	Customer.class \
	SocialDistancingShop.class

CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

SRC_FILES=$(SRC:%.java=$(SRCDIR)/%.java)

default: $(CLASS_FILES)

clean:
	rm -rf $(BINDIR)/*.class

run: 
	cd bin && java socialDistanceShopSampleSolution.SocialDistancingShop 50 10 10 30 > output.txt