'''
#
# File: access.py
# Description: Module to hold GaeAccess, a GAE implementation of 
#              jhb.core.access.JhbAccess
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

from jhb.core.access import JhbAccess
from jhb.core.exception import IdError
import jhb.core.model as model
import models as db

class GaeAccess(JhbAccess):
    '''GAE implementation of jhb.core.access.JhbAccess'''
    
    def __init__(self, user):
        self.user = user
 
    def get_data(self):
        '''Grab all tables into a model.Data object and return it''' 
        def pluralize(name):
            if name[-1] == 'y': return name[:-1] + 'ies'
            return name + 's' 

        d = {}
        ln = lambda x: x.__name__.lower() + 's'
        for cls in model.get_db_classes():
            q = getattr(db, cls.get_db_name()).all(self.user)
            d[ln(cls)] = [ cls.from_db(r) for r in q ]
       
        return model.Data(d)

    def filter(self, cls, field=None, value=None):
        '''Grab all record from given table that have given field value'''
        q = getattr(db, cls.get_db_name()).all(self.user)
        if field is not None and value is not None:
            q = q.filter("%s =" % field, value)
        return [ cls.from_db(r) for r in q ]
    
    def get(self, cls, id):
        '''Get a single record from given table that has given id'''
        q = getattr(db, cls.get_db_name()).all(self.user).filter('id', id)
        return cls.from_db(q.get())

    def save(self, obj):
        '''Save new jhb.core.model.* object 

        Adds the new object (excepting Data of course) to
        its appropriate table, then pull it out and return it in case
        the id was set during the put.
        '''
       
        cls = getattr(db, obj.get_db_name())
    
        if obj.id is not None:
            r = cls.all(self.user).filter("id =", obj.id).get()
            for k,v in obj.to_dict(): setattr(r, k, v)
        else:
            params = obj.to_dict()
            r = cls(**params)
        
        r.user = self.user
        r.put()
        
        # re-read it now to get possibly new id
        return obj.get_cls().from_db(cls.get(r.key()))

