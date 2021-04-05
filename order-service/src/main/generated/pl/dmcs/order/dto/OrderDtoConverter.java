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
    if (json.getValue("ticketDto") instanceof JsonObject) {
      obj.setTicketDto(new pl.dmcs.order.dto.TicketDto((JsonObject)json.getValue("ticketDto")));
    }
  }

  public static void toJson(OrderDto obj, JsonObject json) {
    if (obj.getPersonIdentificationNumber() != null) {
      json.put("personIdentificationNumber", obj.getPersonIdentificationNumber());
    }
    if (obj.getTicketDto() != null) {
      json.put("ticketDto", obj.getTicketDto().toJson());
    }
  }
}