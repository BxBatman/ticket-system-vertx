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
 * Converter for {@link pl.dmcs.order.dto.ReservationTicketDtoResult}.
 *
 * NOTE: This class has been automatically generated from the {@link pl.dmcs.order.dto.ReservationTicketDtoResult} original class using Vert.x codegen.
 */
public class ReservationTicketDtoResultConverter {

  public static void fromJson(JsonObject json, ReservationTicketDtoResult obj) {
    if (json.getValue("ticketNumbers") instanceof JsonArray) {
      java.util.ArrayList<java.lang.Integer> list = new java.util.ArrayList<>();
      json.getJsonArray("ticketNumbers").forEach( item -> {
        if (item instanceof Number)
          list.add(((Number)item).intValue());
      });
      obj.setTicketNumbers(list);
    }
  }

  public static void toJson(ReservationTicketDtoResult obj, JsonObject json) {
    if (obj.getTicketNumbers() != null) {
      JsonArray array = new JsonArray();
      obj.getTicketNumbers().forEach(item -> array.add(item));
      json.put("ticketNumbers", array);
    }
  }
}