/**
 * @file {{ cls.get_java_iface }}.java
 * @author Adam Meadows
 *
 * Copyright 2011 Adam Meadows 
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * WARNING: This file is auto-generated, don't modify it directly,
 * instead modify core/model.py and re-generate
 *
*/

package com.googlecode.alliwant.client.model;
{% for imp in cls.get_extra_iface_imports %}
import {{ imp }};{% endfor %}

public interface {{ cls.get_java_iface }} {{% for f in cls.get_fields %}
  {{ f.get_java_iface_type|safe }} {{ f.get_java_getter_name }}();{% endfor %}
}
