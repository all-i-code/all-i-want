"""
#
# File: owners.py
# Description: Module for defining Owners API Resource
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

import logging

from api.meta import Resource
from core.model import (
    ListOwner as JsOwner,
)


class Owners(Resource):
    """ Owners resource"""

    def get(self, resource_id=None):
        """ Fetch owner """

        logging.info('Owners::get owner_id = [{}]'.format(resource_id))
        if resource_id is None:
            self.abort(404)

        owner = self.db.get_owner(int(resource_id))
        if owner is None:
            self.abort(404)

        js_owner = JsOwner.from_db(owner)
        self.dump(js_owner)
