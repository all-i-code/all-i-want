#
# File: Makefile
# Description: makefile for jhb-web (the GAE version)
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

HIDE=@
PYTHON ?= python

include dev.mk

.PHONY : build clean

py-coverage: export PYTHON := coverage run -a
py-coverage:
	$(HIDE)make codegen-test
	$(HIDE)make pytest
	$(HIDE)echo -e "\nCoverage Stats:\n"
	$(HIDE)coverage report --omit /Applications/*.py

#TODO: Add an ant clean here too
clean:
	$(HIDE)echo "Removing *.pyc files"
	$(HIDE)find . -name \*.pyc | xargs rm -f


PY := python
PY_LONG := Python >= 2.6
PY_LINK := http://www.python.org/getit/

GAE := gae
GAE_LONG := Google App Engine for Python
GAE_LINK := http://code.google.com/appengine/downloads.html\#Google_App_Engine_SDK_for_Python

