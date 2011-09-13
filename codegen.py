'''
#
# File: codegen.py
# Description: Code generation script
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
'''

from google.appengine.dist import use_library
use_library('django', '1.2')
from google.appengine.ext.webapp import template
from optparse import OptionParser
import os
import core.model as model

def gen_json(cls, type, directory):
    t = lambda x: 'templates/json/%s.java' % x
    f = lambda x: dict(iface='', impl='Impl', test='TestImpl').get(x)
    filename = '%s%s.java' % (cls.__name__, f(type))
    t_path = os.path.join(os.path.dirname(__file__), t(type))
    d_path = os.path.join(directory, filename)
    with open(d_path, 'w') as f:
        f.write(template.render(t_path, dict(cls=cls)))

def gen_all_json(directory):
    for cls in model.get_all_classes():
        gen_json(cls, 'iface', directory)
        gen_json(cls, 'impl', directory)
        gen_json(cls, 'test', directory.replace('src', 'test'))

def gen_makefile():
    _ = lambda x: x.__name__.lower()
    i = lambda x: '%s.java' % x.__name__
    m = lambda x: '%sImpl.java' % x.__name__
    t = lambda x: '%sTestImpl.java' % x.__name__
    dest = 'eclipse/AllIWant/src/com/googlecode/alliwant/client/model'
    tdest = dest.replace('src', 'test')
    p = lambda x: (dest, x, x)
    pt = lambda x: (tdest, x, x)
    print 'CODEGEN_TARGETS += \\'
    print '\t test.codegen.models \\'
    for cls in model.get_all_classes():
        print '\t test.codegen.%s \\' % _(cls)
    
    print '\t test.codegen.json.cleanup'
    print ''
    
    print 'test.codegen.json.prep:'
    print '\t$(HIDE). env.sh'
    print '\t$(HIDE)mkdir test.codegen.json'
    print '\t$(HIDE)$(PYTHON) codegen.py -j test.codegen.json'
    print ''
    
    for cls in model.get_all_classes():
        print 'test.codegen.%s: test.codegen.json.prep' % _(cls)
        print '\t$(HIDE)echo "Checking %s/%s"' % (dest, i(cls))
        print '\t$(HIDE)diff %s/%s test.codegen.json/%s' % p(i(cls))
        print '\t$(HIDE)echo "Checking %s/%s"' % (dest, m(cls))
        print '\t$(HIDE)diff %s/%s test.codegen.json/%s' % p(m(cls))
        print '\t$(HIDE)echo "Checking %s/%s"' % (tdest, t(cls))
        print '\t$(HIDE)diff %s/%s test.codegen.json/%s' % pt(t(cls))
        print ''
    
    print 'test.codegen.json.cleanup:'
    print '\t$(HIDE)rm -rf test.codegen.json'
    print ''

if __name__ == "__main__":
    parser = OptionParser()
    parser.add_option('-j', '--json', dest='json_dir',
        help='generate json files in DIR', metavar='DIR')
    parser.add_option('-m', '--makefile', action='store_true', dest='mk',
        help='generate test makefile')

    (options, args) = parser.parse_args()
    if options.json_dir is not None:
        gen_all_json(options.json_dir)
    elif options.mk:
        gen_makefile()

