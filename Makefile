#
# File: Makefile
# Description: makefile for all-i-want
#
# Copyright 2011-2015 Adam Meadows
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

HIDE := @
PYTHON ?= python

.PHONY: coverage flake8-test python-test test clean

flake8-test:
	$(HIDE)flake8 .

python-test:
	$(HIDE)nosetests

test: flake8-test python-test

coverage:
	$(HIDE)nosetests --with-coverage --cover-package=all-i-want

clean:
	$(HIDE)echo "Removing *.pyc files"
	$(HIDE)find . -name \*.pyc | xargs rm -f
	$(HIDE)echo "Removing *.py-e files"
	$(HIDE)find . -name \*.py-e | xargs rm -f

# Continuous Integration targets
CI_VARS := PYTHONPATH=$(PWD):$(PWD)/ci_mocks
ci-test: flake8-test ci-python-test
ci-python-test:
	$(HIDE) PYTHONPATH=$${PWD}:$${PWD}/ci_mocks make test

ci-coverage:
	$(HIDE) PYTHONPATH=$${PWD}:$${PWD}/ci_mocks make coverage
