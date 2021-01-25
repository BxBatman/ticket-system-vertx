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

package pl.dmcs.order.dto;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link pl.dmcs.order.dto.OrderDto}.
 *
 * NOTE: This class has been automatically generated from the {@link pl.dmcs.order.dto.OrderDto} original class using Vert.x codegen.
 */
public class OrderDtoConverter {

  public static void fromJson(JsonObject json, OrderDto obj) {
    if (json.getValue("personIdentificationNumber") instanceof String) {
      obj.setPersonIdentificationNumber((String)json.getValue("personIdentificationNumber"));
    }
    if (json.getValue("tickets") instanceof JsonArray) {
      java.util.ArrayList<java.lang.Integer> list = new java.util.ArrayList<>();
      json.getJsonArray("tickets").forEach( item -> {
        if (item instanceof Number)
          list.add(((Number)item).intValue());
      });
      obj.setTickets(list);
    }
  }

  public static void toJson(OrderDto obj, JsonObject json) {
    if (obj.getPersonIdentificationNumber() != null) {
      json.put("personIdentificationNumber", obj.getPersonIdentificationNumber());
    }
    if (obj.getTickets() != null) {
      JsonArray array = new JsonArray();
      obj.getTickets().forEach(item -> array.add(item));
      json.put("tickets", array);
    }
  }
}