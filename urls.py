"""
#
# File: urls.py
# Description: URL handler for html/js URLs
#
# Copyright 2014 Adam Meadows
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

import webapp2

from api.users import (
    CurrentUserHandler,
    OwnersHandler,
)

from views import (
    ListPage,
    GoodbyePage,
    NotFoundPage,
)


def not_found(request, response, e):
    response.out.write('not found')


urls = [
    ('/', ListPage),
    ('/api/v1/users/current', CurrentUserHandler),
    ('/api/v1/owners', OwnersHandler),
    ('/api/v1/owners/(.*)', OwnersHandler),
    ('/goodbye', GoodbyePage),
    ('/.*', NotFoundPage),
]
app = webapp2.WSGIApplication(urls, debug=True)
