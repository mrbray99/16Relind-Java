DESTDIR?=/usr


##ifneq ($V,1)
##Q ?= @
##endif

CC	= gcc
CFLAGS	= $(DEBUG) -fPIC -Wall -Wextra $(INCLUDE) -Winline -pipe

LDFLAGS	= -L$(DESTDIR)/lib
LIBS    = -lpthread -lrt -lm -lcrypt

SRC	=	src/main/C/comm.c

OBJ	=	$(SRC:.c=.o)

define buildjava
	mvn clean package
endef

all:	16relind

16relind:	compileshared createlibrary compilejava

.PHONY:compileshared
compileshared:
	$Q echo [Compile]
	$Q $(CC) -c -fPIC $(CFLAGS) $(SRC) -o build/C/comm.o

.PHONY:createlibrary
createlibrary: $(OBJ)
	$Q echo "[Create Library]"
	$Q $(CC) -shared build/C/comm.o -o build/C/libcomm.so

.PHONY:makejava
makejava:
	$(call buildjava)

.PHONY:	clean
clean:
	$Q echo "[Clean]"
	$Q rm -f $(OBJ) 16relind *~ core tags *.bak


.PHONY:	install
install: createlibrary
	$Q echo "[Install]"
	$Q cp build/C/libcomm.so	$(DESTDIR)/lib
	$Q chmod 755		$(DESTDIR)/lib/libcomm.so

.PHONY:	uninstall
uninstall:
	$Q echo "[UnInstall]"
	$Q rm -f $(DESTDIR)/lib/libcomm.so
