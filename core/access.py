"""
#
# File: access.py
# Description: AllIWant interface for accessing data (stored model objects)
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

from core.exception import OverrideError


class Access(object):
    """Base class for all AllIWant Access interfaces. Should be overridden"""

    def filter(self, cls, field=None, value=None):
        """Retrieve all model objects by class and a field"""
        raise OverrideError('filter')

    def get(self, cls, id):
        """Retrieve a particular model object(s) by class and id"""
        raise OverrideError('get')

    def save(self, obj):
        """Save a core.model.* object.

        All fields should already be validated, except id,
        which can None to autofill it with a sequence number.
        A new copy of the object is returned (with id populated if None)
        """
        raise OverrideError('save')


class MemoryAccess(Access):
    """In-memory implementation of Access (currently for testing)"""

    def nm(self, cls):
        return cls.get_name()

    def pl(self, cls):
        from core.util import uncamelize, pluralize
        return pluralize(uncamelize(self.nm(cls)))

    def load_data(self, data):
        from core.model import get_db_classes
        g = lambda x: getattr(data, self.pl(x))
        _ = lambda x: (self.nm(x), [n.clone() for n in g(x)])
        self.data = dict((_(cls) for cls in get_db_classes()))

    def filter(self, cls, field=None, value=None):
        """Retrieve all model objects by class and a field"""
        e = lambda f, v: f is not None and v is not None
        _ = lambda r, f, v: getattr(r, f) == v if e(f, v) else True
        return [
            r
            for r in self.data.get(self.nm(cls))
            if _(r, field, value)
        ]

    def get(self, cls, id):
        """Retrieve a particular  model object(s) by class and id"""
        r = self.filter(cls, 'id', id)
        return r[0] if len(r) > 0 else None

    def save(self, obj):
        """Save a core.model.* object.

        All fields should already be validated, except id,
        which can None to autofill it with a sequence number.
        A new copy of the object is returned (with id populated if None)
        """
        if obj.id is None:
            mid = 0
            for r in self.data.get(self.nm(obj.get_cls())):
                mid = max(mid, r.id)
            obj.id = mid + 1

        self.data.get(self.nm(obj.get_cls())).append(obj)
