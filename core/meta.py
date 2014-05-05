"""
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
"""

from core.util import camelize, uncamelize, pluralize


class Field(object):
    """Class to represent a field in a Model object"""

    @classmethod
    def get_cls_default(cls):
        return getattr(cls, 'default', None)

    def __init__(self, name=None, default=None):
        self.name = name
        self.default = default

    def get_name(self):
        return self.name

    def get_default(self):
        return self.default


class Integer(Field):
    default = -1


class String(Field):
    default = ''


class Text(Field):
    default = ''


class Float(Field):
    default = 0.0


class User(Field):
    default = ''


class Boolean(Field):
    default = True


class ModelArray(Field):
    def __init__(self, type=None, name=None, default=None):
        default = default or []
        _ = lambda x: pluralize(uncamelize(x.__name__))
        self.type = type
        self.name = name if name is not None else _(self.__class__)
        self.default = default


class ModelMeta(type):
    """Meta class for  object classes"""
    def __new__(cls, class_name, bases, class_dict):
        nc = type.__new__(cls, class_name, bases, class_dict)
        ModelManager.register(nc)
        nc.field_dict = dict((f.get_name(), f) for f in nc.get_fields())
        return nc


class ModelManager(object):
    models = []

    @classmethod
    def register(cls, mc):
        if mc.is_abstract():
            return
        assert(mc not in cls.models)
        cls.models.append(mc)


class Model(object):
    """Base class for all model objects"""
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
        return camelize(field_name, trailing=True)

    @classmethod
    def get_fields(cls):
        return getattr(cls, 'fields', [])

    @classmethod
    def get_types(cls):
        return getattr(cls, 'types', [])

    @classmethod
    def get_field_names(cls):
        return (f.get_name() for f in cls.get_fields())

    @classmethod
    def from_json_dict(cls, json_dict):
        j = lambda n: cls.get_json_name(n)
        v = lambda n: json_dict.get(j(n))
        return cls(dict((n, v(n)) for n in cls.get_field_names()))

    def __init__(self, **kwargs):
        fields = ((f.get_name(), f.get_default()) for f in self.get_fields())
        for n, d in fields:
            setattr(self, n, kwargs.get(n, d))

    def get_cls(self):
        return self.__class__

    def get_value(self, name):
        v = getattr(self, name, None)
        if v.__class__ == list:
            v = [i.to_json_dict() for i in v]
        return v

    def to_dict(self):
        v = lambda f: self.get_value(f.get_name())
        n = lambda f: f.get_name()
        return dict((n(f), v(f)) for f in self.get_fields())

    def to_json_dict(self):
        v = lambda f: self.get_value(f.get_name())
        n = lambda f: self.get_json_name(f.get_name())
        return dict((n(f), v(f)) for f in self.get_fields())

    def clone(self):
        d = self.to_dict()
        return self.__class__(**d)

    def equals(self, obj):
        for n in (f.get_name() for f in self.get_fields()):
            if getattr(self, n) != getattr(obj, n):
                return False
        return True
