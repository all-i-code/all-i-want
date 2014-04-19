"""
#
# File: params.py
# Description: Module for defining API Parameter classes
#
# Copyright 2011-2014 Adam Meadows
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
import json


class Param(object):
    """Base class for API parameters"""

    def __init__(self, name):
        self.name = name

    def get_json_name(self):
        from jhb.core.util import camelize
        return camelize(self.name)

    def get_value(self, value):
        return value


class Integer(Param):
    def get_value(self, value):
        return int(value)


class String(Param):
    def get_value(self, value):
        return str(value)


class Float(Param):
    def get_value(self, value):
        return float(value)


class Boolean(Param):

    def get_value(self, value):
        return str(value) not in ('false', 'False', '0')


class Model(Param):

    def __init__(self, name, model):
        self.name = name
        self.model = model

    def get_value(self, value):
        return self.model.from_json_dict(json.loads(value))


class List(Param):

    def __init__(self, name, param):
        self.name = name
        self.param = param

    def get_value(self, value):
        return [self.param.get_value(v) for v in json.loads(value)]
