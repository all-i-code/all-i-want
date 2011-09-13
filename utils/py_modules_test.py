#
# File: py_modules_test.py
# Description: Test that simply tries to import required python modules to
#              ensure that they are installed
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

import sys
v = [ int(p) for p in sys.version.split('(')[0].strip().split('.') ]
assert(v[0] == 2)
assert(v[1] >= 6)

from google.appengine.dist import use_library
use_library('django', '1.2')
from google.appengine.ext.webapp import template
from optparse import OptionParser
import jhb.core

