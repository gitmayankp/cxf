/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.cxf.jaxrs.json.basic;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.helpers.CastUtils;

public class JsonMapObject implements Serializable {
    private static final long serialVersionUID = 2620765136328623790L;
    Map<String, Integer> updateCount;
    Map<String, Object> values = new LinkedHashMap<>();
    public JsonMapObject() {

    }

    public JsonMapObject(Map<String, Object> values) {
        this.values = values;
    }

    public void setProperty(String name, Object value) {
        if (values.containsKey(name)) {
            if (updateCount == null) {
                updateCount = new LinkedHashMap<>();
            }
            final Integer count = updateCount.getOrDefault(name, 1) + 1;
            updateCount.put(name, count);
        }
        if (value instanceof JsonMapObject) {
            value = ((JsonMapObject)value).asMap();
        }
        values.put(name, value);
    }

    public boolean containsProperty(String name) {
        return values.containsKey(name);
    }

    public Object getProperty(String name) {
        return values.get(name);
    }
    
    public Map<String, Object> getMapProperty(String name) {
        Object value = getProperty(name);
        if (value != null) {
            return CastUtils.cast((Map<?, ?>)value);
        }
        return null;
    }
    
    public JsonMapObject getJsonMapProperty(String name) {
        Map<String, Object> value = getMapProperty(name);
        if (value != null) {
            return new JsonMapObject(value);
        }
        return null;
    }

    public Map<String, Object> asMap() {
        return values;
    }
    public Integer getIntegerProperty(String name) {
        Object value = getProperty(name);
        if (value != null) {
            return value instanceof Integer ? (Integer)value : Integer.parseInt(value.toString());
        }
        return null;
    }
    public Long getLongProperty(String name) {
        Object value = getProperty(name);
        if (value != null) {
            return value instanceof Long ? (Long)value : Long.parseLong(value.toString());
        }
        return null;
    }
    public Boolean getBooleanProperty(String name) {
        Object value = getProperty(name);
        if (value != null) {
            return value instanceof Boolean ? (Boolean)value : Boolean.parseBoolean(value.toString());
        }
        return null;
    }
    public String getStringProperty(String name) {
        Object value = getProperty(name);
        if (value != null) {
            return value.toString();
        }
        return null;
    }
    public List<String> getListStringProperty(String name) {
        Object value = getProperty(name);
        if (value != null) {
            return CastUtils.cast((List<?>)value);
        }
        return null;
    }
    public List<Map<String, Object>> getListMapProperty(String name) {
        Object value = getProperty(name);
        List<Map<String, Object>> list = null;
        if (value != null) {
            list = CastUtils.cast((List<?>)value);
        }
        return list;
    }
    public int hashCode() {
        return values.hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof JsonMapObject && ((JsonMapObject)obj).values.equals(this.values);
    }
    
    public int size() {
        return values.size();
    }
    
    public Map<String, Object> getUpdateCount() {
        return updateCount == null ? null : Collections.<String, Object>unmodifiableMap(updateCount);
    }
    
    public Object removeProperty(String name) {
        return values.remove(name);
    }
}
