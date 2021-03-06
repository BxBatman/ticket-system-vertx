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

/** @module catalog-js/ticket_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTicketService = Java.type('pl.dmcs.catalog.TicketService');
var ReservationTicketDtoResult = Java.type('pl.dmcs.catalog.dto.ReservationTicketDtoResult');
var TicketDto = Java.type('pl.dmcs.catalog.dto.TicketDto');
var ReservationTicketDto = Java.type('pl.dmcs.catalog.dto.ReservationTicketDto');
var Ticket = Java.type('pl.dmcs.catalog.Ticket');

/**
 @class
*/
var TicketService = function(j_val) {

  var j_ticketService = j_val;
  var that = this;

  /**

   @public
   @param resultHandler {function} 
   @return {TicketService}
   */
  this.initializePersistence = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_ticketService["initializePersistence(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param ticket {Object} 
   @param resultHandler {function} 
   @return {TicketService}
   */
  this.save = function(ticket, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_ticketService["save(pl.dmcs.catalog.Ticket,io.vertx.core.Handler)"](ticket != null ? new Ticket(new JsonObject(Java.asJSONCompatible(ticket))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param id {number} 
   @param resultHandler {function} 
   @return {TicketService}
   */
  this.get = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'function') {
      j_ticketService["get(java.lang.Integer,io.vertx.core.Handler)"](utils.convParamInteger(id), function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param ticketDto {Object} 
   @param resultHandler {function} 
   @return {TicketService}
   */
  this.saveMultiplesSameTickets = function(ticketDto, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_ticketService["saveMultiplesSameTickets(pl.dmcs.catalog.dto.TicketDto,io.vertx.core.Handler)"](ticketDto != null ? new TicketDto(new JsonObject(Java.asJSONCompatible(ticketDto))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param title {string} 
   @param quantity {number} 
   @param resultHandler {function} 
   @return {TicketService}
   */
  this.checkAvailability = function(title, quantity, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] ==='number' && typeof __args[2] === 'function') {
      j_ticketService["checkAvailability(java.lang.String,java.lang.Integer,io.vertx.core.Handler)"](title, utils.convParamInteger(quantity), function(ar) {
      if (ar.succeeded()) {
        resultHandler(ar.result(), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param resultHandler {function} 
   @return {TicketService}
   */
  this.getAll = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_ticketService["getAll(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnListSetDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param reservationTicketDto {Object} 
   @param resultHandler {function} 
   @return {TicketService}
   */
  this.reserveTickets = function(reservationTicketDto, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_ticketService["reserveTickets(pl.dmcs.catalog.dto.ReservationTicketDto,io.vertx.core.Handler)"](reservationTicketDto != null ? new ReservationTicketDto(new JsonObject(Java.asJSONCompatible(reservationTicketDto))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param ticketIds {Array.<number>} 
   @param resultHandler {function} 
   @return {TicketService}
   */
  this.getSpecificTickets = function(ticketIds, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'object' && __args[0] instanceof Array && typeof __args[1] === 'function') {
      j_ticketService["getSpecificTickets(java.util.List,io.vertx.core.Handler)"](utils.convParamListBasicOther(ticketIds), function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnListSetDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param title {string} 
   @param resultHandler {function} 
   @return {TicketService}
   */
  this.deleteTicket = function(title, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_ticketService["deleteTicket(java.lang.String,io.vertx.core.Handler)"](title, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_ticketService;
};

TicketService._jclass = utils.getJavaClass("pl.dmcs.catalog.TicketService");
TicketService._jtype = {
  accept: function(obj) {
    return TicketService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(TicketService.prototype, {});
    TicketService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
TicketService._create = function(jdel) {
  var obj = Object.create(TicketService.prototype, {});
  TicketService.apply(obj, arguments);
  return obj;
}
module.exports = TicketService;