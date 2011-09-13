'''
#
# File: models.py
# Description: JHB Model objects
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
# WARNING: This file is auto-generated, don't modify directly, 
# instead modify templates/models.py and re-generate
#
'''

from google.appengine.ext import db
from models_meta import JhbDb

{% for cls in classes %}
class {{ cls.get_db_name }}(JhbDb):{% for field in cls.get_fields %}{%if field.get_name != 'id' %}{{ field.get_name }} = {{ field.get_ae_type }}{% endif %}
    {% endfor %}{% endfor %}

