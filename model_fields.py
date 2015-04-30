"""
#
# File: model_fields.py
# Description: Model field classes
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
from datetime import datetime


class Field(object):
    def __init__(self, name, prop):
        self.name = name
        self.prop = prop

    def to_json_value(self, raw_value):
        return raw_value

    def to_raw_value(self, json_value):
        return json_value


class String(Field):
    def to_json_value(self, raw_value):
        return str(raw_value)

    def to_raw_value(self, json_value):
        return str(json_value)


class Integer(Field):
    def to_json_value(self, raw_value):
        return int(raw_value)

    def to_raw_value(self, json_value):
        return int(json_value)


class Boolean(Field):
    def to_json_value(self, raw_value):
        return bool(raw_value)

    def to_raw_value(self, json_value):
        return bool(json_value)


class DateTime(Field):
    FORMAT = '%Y-%m-%d %H:%M:%S'

    def to_json_value(self, raw_value):
        return raw_value.strftime(self.FORMAT)

    def to_raw_value(self, json_value):
        return datetime.strptime(json_value, self.FORMAT)


class Reference(Field):
    def to_json_value(self, raw_value):
        return raw_value.key().id()

    def to_raw_value(self, json_value, lookup=False):
        if lookup:
            return self.prop.data_type.get_by_id(json_value)
        return json_value


class User(Field):
    def to_json_value(self, raw_value):
        return raw_value.email

    def to_raw_value(self, json_value):
        raise Exception('User fields are read-only')
