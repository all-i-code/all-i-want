#
# File: dev.mk
# Description: makefile for dev setup of jhb-web (the GAE version)
#
# Copyright 2011 Adam Meadows
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.
#

PY := python
PY_LONG := Python >= 2.6
PY_LINK := http://www.python.org/getit/

GAE := gae
GAE_LONG := Google App Engine for Python
GAE_LINK := http://code.google.com/appengine/downloads.html\#Google_App_Engine_SDK_for_Python

NOSE := nose
NOSE_LONG := nose: Python test runner
NOSE_LINK := http://pypi.python.org/pypi/nose/1.1.2

JVA := java
JVA_LONG := Java JDK
JVA_LINK := http://www.oracle.com/technetwork/java/javase/downloads/index.html

ANT := ant
ANT_LONG := Apache Ant
ANT_LINK := http://ant.apache.org/

#TODO Add tests for GWT once I have an ant gwt build up and running

envreq:
	$(HIDE)echo "Packages required for JHB Development:\n"
	$(HIDE)echo "  $(PY): $(PY_LONG)"
	$(HIDE)echo "    - $(PY_LINK)\n"
	$(HIDE)echo "  $(GAE): $(GAE_LONG)"
	$(HIDE)echo "    - $(GAE_LINK)\n"
	$(HIDE)echo "  $(NOSE): $(NOSE_LONG)"
	$(HIDE)echo "    - $(NOSE_LINK)\n"
	$(HIDE)echo "  $(JVA): $(JVA_LONG)"
	$(HIDE)echo "    - $(JVA_LINK)\n"
	$(HIDE)echo "  $(ANT): $(ANT_LONG)"
	$(HIDE)echo "    - $(ANT_LINK)\n"

envtest:
	$(HIDE)echo "Testing JHB Development Environment Requirements"
	$(HIDE)echo "\n-Testing Python Module Imports"
	$(HIDE)python utils/py_modules_test.py
	$(HIDE)echo "\n-Testing $(NOSE) installed"
	$(HIDE)nosetests --version
	$(HIDE)echo "\n-Testing $(JVA) installed"
	$(HIDE)java -version
	$(HIDE)echo "\n-Testing $(ANT) installed"
	$(HIDE)ant -version
