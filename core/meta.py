'''
#
# File: meta.py
# Description: Meta-class and base classes for AllIWant model objects
#
# Copyright 2011-2013 Adam Meadows
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

from core.util import camelize, uncamelize, pluralize

class Field(object):
    '''Class to represent a field in a Model object'''

    @classmethod
    def get_extra_java_imports(cls):
        return getattr(cls, 'extra_java_imports', [])

    @classmethod
    def get_extra_iface_imports(cls):
        return getattr(cls, 'extra_iface_imports', [])

    @classmethod
    def get_cls_default(cls):
        return getattr(cls, 'default', None)

    @classmethod
    def get_java_type(cls):
        return getattr(cls, 'java_type', None)

    @classmethod
    def get_java_iface_type(cls):
        return cls.get_java_type()

    @classmethod
    def get_java_test_getter_name(cls):
        return getattr(cls, 'java_test_getter', None)

    def __init__(self, name=None, default=None):
        self.name = name
        self.default = default

    def get_java_test_type(self):
        return self.get_java_type()

    def get_java_getter_name(self):
        return 'get' + camelize(self.name)

    def get_java_getter(self):
        template = '@Override\n' +\
            '  public final native %s %s() /*-{\n' +\
            '    return this.%s;\n' +\
            '  }-*/;\n'
        jt, gn = (self.get_java_type(), self.get_java_getter_name())
        return template % (jt, gn, self.json_name)

    def get_java_test_getter(self):
        template = '@Override\n' +\
            '  public %s %s() {\n' +\
            '    return %s("%s");\n' +\
            '  }\n'
        jt, gn = (self.get_java_test_type(), self.get_java_getter_name())
        jn, tgn = (self.json_name, self.get_java_test_getter_name())
        return template % (jt, gn, tgn, jn)

    def get_java_name(self):
        return camelize(self.name, trailing=True)

    def get_name(self):
        return self.name

    def get_default(self):
        return self.default

class FieldInt(Field):
    java_type = 'int'
    default = -1
    java_test_getter = 'getInt'

class FieldString(Field):
    java_type = 'String'
    default = ''
    java_test_getter = 'getStr'

class FieldText(Field):
    java_type = 'String'
    default = ''
    java_test_getter = 'getStr'

class FieldFloat(Field):
    java_type = 'double'
    default = 0.0
    java_test_getter = 'getDbl'

class FieldUser(Field):
    java_type = 'String'
    default = ''
    java_test_getter = 'getStr'

class FieldBoolean(Field):
    java_type = 'boolean'
    default = True
    java_test_getter = 'getBool'

    def get_java_getter_name(self):
        return camelize(self.name, trailing=True)

class FieldModelArray(Field):
    java_test_getter = 'getArray'
    extra_iface_imports = (
        'java.util.List',
    )

    def __init__(self, type=None, name=None, default=[]):
        _ = lambda x: pluralize(uncamelize(x.__name__))
        self.type = type
        self.name = name if name is not None else _(x)
        self.default = default

    def get_java_test_type(self):
        return self.get_java_iface_type()

    def get_java_iface_type(self):
        return 'List<%s>' % self.type.get_java_iface()

    def get_java_test_getter(self):
        template = '@Override\n' +\
            '  public %s %s() {\n' +\
            '    return %s.parseArray(getArray("%s"));\n' +\
            '  }\n'
        jt, gn = (self.get_java_test_type(), self.get_java_getter_name())
        jn, tgn = (self.json_name, self.type.get_java_test_class())
        return template % (jt, gn, tgn, jn)

    def get_java_get_helper(self):
        _ = lambda x: x + 'Js'
        template = 'private final native JsArray<%s> %s()/*-{\n' +\
            '    return this.%s;\n' +\
            '  }-*/;\n'
        jt, gn = (self.type.get_java_class(), _(self.get_java_getter_name()))
        return template % (jt, gn, self.json_name)

    def get_java_getter(self):
        _ = lambda x: x + 'Js'
        helper = self.get_java_get_helper()
        template = \
            '  @Override\n' +\
            '  public final %s %s() {\n' +\
            '    return %s.decodeList(%s());\n' +\
            '  }\n'
        it = self.get_java_iface_type()
        gn = self.get_java_getter_name()
        jt, hn = (self.type.get_java_class(), _(gn))
        getter = template % (it, gn, jt, hn)
        return '\n'.join((helper, getter))

class ModelMeta(type):
    '''Meta class for  object classes'''
    def __new__(cls, class_name, bases, class_dict):
        nc = type.__new__(cls, class_name, bases, class_dict)
        ModelManager.register(nc)
        letters = 'abcdefghijklmnopqrstuvwxyz'
        if len(nc.get_fields()) > len(letters):
            base = 'Class %s has too many fields, max of %s'
            raise Exception(base % (nc.__name__, len(letters)))

        for i,f in enumerate(nc.get_fields()): f.json_name = letters[i]
        nc.field_dict = dict((f.get_name(), f) for f in nc.get_fields())
        return nc

class ModelManager(object):
    models = []

    @classmethod
    def register(cls, mc):
        if mc.is_abstract(): return
        assert(mc not in cls.models)
        cls.models.append(mc)

class Model(object):
    '''Base class for all model objects'''
    __metaclass__ = ModelMeta
    abstract = True

    @classmethod
    def is_abstract(cls):
        return cls.__dict__.get('abstract', False)

    @classmethod
    def in_db(cls):
        return getattr(cls, 'db', True)

    @classmethod
    def get_name(cls):
        return cls.__name__

    @classmethod
    def get_db_name(cls):
        return cls.get_name() + 'Db'

    @classmethod
    def get_json_name(cls, field_name):
        return cls.field_dict.get(field_name).json_name

    @classmethod
    def get_fields(cls):
        return getattr(cls, 'fields', [])

    @classmethod
    def get_types(cls):
        return getattr(cls, 'types', [])

    @classmethod
    def get_field_names(cls):
        return ( f.get_name() for f in cls.get_fields() )

    @classmethod
    def get_java_iface(cls):
        return cls.get_name()

    @classmethod
    def get_java_class(cls):
        return cls.get_name() + 'Impl'

    @classmethod
    def get_java_create_params(cls):
        _ = lambda f: '%s %s' % (f.get_java_type(), f.get_java_name())
        return ',\n   '.join( (_(f) for f in cls.get_fields()) )

    @classmethod
    def get_java_create_eval(cls):
        _ = lambda f: '"%s": %s' % (f.json_name, f.get_java_name())
        return ',\n      '.join( (_(f) for f in cls.get_fields()) )

    @classmethod
    def get_extra_java_imports(cls):
        imports = []
        _ = lambda x : x not in imports
        for f in cls.get_fields():
            imports.extend((i for i in f.get_extra_java_imports() if _(i)))
        return imports

    @classmethod
    def get_extra_iface_imports(cls):
        imports = []
        _ = lambda x : x not in imports
        for f in cls.get_fields():
            imports.extend((i for i in f.get_extra_iface_imports() if _(i)))
        return imports

    @classmethod
    def get_java_test_class(cls):
        return cls.get_name() + 'TestImpl'

    @classmethod
    def from_json_dict(cls, json_dict):
        j = lambda n: cls.get_json_name(n)
        v = lambda n: json_dict.get(j(n))
        return cls(dict((n, v(n)) for n in cls.get_field_names() ))

    def __init__(self, **kwargs):
        fields = ((f.get_name(), f.get_default()) for f in self.get_fields())
        for n, d in fields:
            setattr(self, n, kwargs.get(n, d))

    def get_cls(self):
        return self.__class__

    def get_value(self, name):
        v = getattr(self, name, None)
        if v.__class__ == list:
            v = [ i.to_json_dict() for i in v ]
        return v

    def to_dict(self):
        v = lambda f: self.get_value(f.get_name())
        n = lambda f: f.get_name()
        return dict( (n(f), v(f)) for f in self.get_fields() )

    def to_json_dict(self):
        v = lambda f: self.get_value(f.get_name())
        n = lambda f: self.get_json_name(f.get_name())
        return dict( (n(f), v(f)) for f in self.get_fields() )

    def clone(self):
        d = self.to_dict()
        return self.__class__(**d)

    def equals(self, obj):
        for n in (f.get_name() for f in self.get_fields()):
            if getattr(self, n) != getattr(obj, n): return False
        return True

