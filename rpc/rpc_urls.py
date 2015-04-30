"""
#
# File: rpc_urls.py
# Description: GAE app to handle RPCs
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

from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app

from rpc.rpc_user import UserRpcReqHandler
from rpc.rpc_group import GroupRpcReqHandler
from rpc.rpc_list import ListRpcReqHandler

urls = [
    ('/rpc/user/.*', UserRpcReqHandler),
    ('/rpc/group/.*', GroupRpcReqHandler),
    ('/rpc/list/.*', ListRpcReqHandler),
]
application = webapp.WSGIApplication(urls, debug=True)


def main():
    run_wsgi_app(application)

if __name__ == '__main__':
    main()
