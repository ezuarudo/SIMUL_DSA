OSTYPE ?= $(shell uname)

ifeq ($(OSTYPE),linux)
OSTYPE=Linux
endif

ifneq ($(findstring MINGW,$(OSTYPE)),)
OSTYPE=Windows
endif

ifeq ($(OSTYPE),msys)
OSTYPE=Windows
endif

ifeq ($(OSTYPE),Window)
ROSMAKE=echo 'ROS controllers are not supported under Windows'
endif

ifeq ($(OSTYPE),Darwin)
ROSMAKE=echo 'ROS controllers not compiled under Mac OS X'
endif

ifeq ($(OSTYPE),Linux)
ROSMAKE=rosmake 
endif

compil: PROYECTOTP 

PROYECTOTP: autonomous_vehicle.c
	@cp Makefile Makefile.backup
	@cp Makefile.ros Makefile
	@-$(ROSMAKE) PROYECTOTP 
	@mv Makefile.backup Makefile
	

clean:
	@rm -rf bin build  autonomous_vehicle
