#
# File: env.sh
# Description: BASH commands to set up the environment for GAE Python
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

if [ "${GAE}" == "" ]
then
    export GAE=/usr/local/google_appengine
fi

export PYTHONPATH=${PWD}:${GAE}/lib/yaml/lib:${GAE}/lib/webob-1.1.1:${GAE}/lib/django-1.4:${GAE}/lib:${GAE}
