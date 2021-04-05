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

/** @module order-js/order_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JOrderService = Java.type('pl.dmcs.order.OrderService');
var Order = Java.type('pl.dmcs.order.Order');
var OrderDto = Java.type('pl.dmcs.order.dto.OrderDto');

/**
 @class
*/
var OrderService = function(j_val) {

  var j_orderService = j_val;
  var that = this;

  /**

   @public
   @param resultHandler {function} 
   @return {OrderService}
   */
  this.initializePersistence = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_orderService["initializePersistence(io.vertx.core.Handler)"](function(ar) {
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
   @param order {Object} 
   @param resultHandler {function} 
   @return {OrderService}
   */
  this.save = function(order, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_orderService["save(pl.dmcs.order.Order,io.vertx.core.Handler)"](order != null ? new Order(new JsonObject(Java.asJSONCompatible(order))) : null, function(ar) {
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
   @return {OrderService}
   */
  this.deleteOrder = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'function') {
      j_orderService["deleteOrder(java.lang.Integer,io.vertx.core.Handler)"](utils.convParamInteger(id), function(ar) {
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
   @param orderDto {Object} 
   @param resultHandler {function} 
   @return {OrderService}
   */
  this.checkAvailability = function(orderDto, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_orderService["checkAvailability(pl.dmcs.order.dto.OrderDto,io.vertx.core.Handler)"](orderDto != null ? new OrderDto(new JsonObject(Java.asJSONCompatible(orderDto))) : null, function(ar) {
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
   @param id {number} 
   @param resultHandler {function} 
   @return {OrderService}
   */
  this.getOrder = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'function') {
      j_orderService["getOrder(java.lang.Integer,io.vertx.core.Handler)"](utils.convParamInteger(id), function(ar) {
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
   @param orderDto {Object} 
   @param resultHandler {function} 
   @return {OrderService}
   */
  this.createOrder = function(orderDto, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_orderService["createOrder(pl.dmcs.order.dto.OrderDto,io.vertx.core.Handler)"](orderDto != null ? new OrderDto(new JsonObject(Java.asJSONCompatible(orderDto))) : null, function(ar) {
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
  this._jdel = j_orderService;
};

OrderService._jclass = utils.getJavaClass("pl.dmcs.order.OrderService");
OrderService._jtype = {
  accept: function(obj) {
    return OrderService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(OrderService.prototype, {});
    OrderService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
OrderService._create = function(jdel) {
  var obj = Object.create(OrderService.prototype, {});
  OrderService.apply(obj, arguments);
  return obj;
}
module.exports = OrderService;