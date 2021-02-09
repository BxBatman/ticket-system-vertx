/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package pl.dmcs.catalog;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link pl.dmcs.catalog.Ticket}.
 *
 * NOTE: This class has been automatically generated from the {@link pl.dmcs.catalog.Ticket} original class using Vert.x codegen.
 */
public class TicketConverter {

  public static void fromJson(JsonObject json, Ticket obj) {
    if (json.getValue("date") instanceof String) {
      obj.setDate((String)json.getValue("date"));
    }
    if (json.getValue("id") instanceof Number) {
      obj.setId(((Number)json.getValue("id")).intValue());
    }
    if (json.getValue("price") instanceof String) {
      obj.setPrice((String)json.getValue("price"));
    }
    if (json.getValue("reserved") instanceof Boolean) {
      obj.setReserved((Boolean)json.getValue("reserved"));
    }
    if (json.getValue("title") instanceof String) {
      obj.setTitle((String)json.getValue("title"));
    }
  }

  public static void toJson(Ticket obj, JsonObject json) {
    if (obj.getDate() != null) {
      json.put("date", obj.getDate());
    }
    json.put("id", obj.getId());
    if (obj.getPrice() != null) {
      json.put("price", obj.getPrice());
    }
    json.put("reserved", obj.isReserved());
    if (obj.getTitle() != null) {
      json.put("title", obj.getTitle());
    }
  }
}