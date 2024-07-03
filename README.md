 Copyright (c) 2016-2020 Sequent Microsystems and (c) 2024 - Mike Bray.
 
This software is a Java implementation of the Sequent Microsystems C software for the 16 relay HAT. The Java software has been created by Mike Bray.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU Leser General Public License as published by the Free Software Foundation, either version 3 of the License, or	(at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.You should have received a copy of the GNU Lesser General Public License long with this program. If not, see http://www.gnu.org/licenses.

This is a Java translation of the C code provided by Sequent Microsystems.

The only code not translated is the communication with the HAT itself (comm.c).  In this implementation a shared library of comm.c is created and placed into /usr/lib. The code depends on the Java Native Access library
(see https://github.com/java-native-access/jna) to load this library.  The rest of the code has been translated to Java.

A Java Main class is the entry point.  It takes the run time arguments and places them into a C like call to the main function

To make this code you will need to install Maven.

To build the Java code go to the folder for the software and enter 'make makejava'.

Before running the Java code you need to build and load the shared library.  To do this enter 'sudo make clean install', this will compile and install the shared library.

