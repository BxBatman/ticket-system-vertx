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

package pl.dmcs.order.rxjava;

import java.util.Map;
import rx.Observable;
import rx.Single;
import pl.dmcs.order.Order;
import pl.dmcs.order.dto.OrderDto;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;


@io.vertx.lang.rxjava.RxGen(pl.dmcs.order.OrderService.class)
public class OrderService {

  public static final io.vertx.lang.rxjava.TypeArg<OrderService> __TYPE_ARG = new io.vertx.lang.rxjava.TypeArg<>(
    obj -> new OrderService((pl.dmcs.order.OrderService) obj),
    OrderService::getDelegate
  );

  private final pl.dmcs.order.OrderService delegate;
  
  public OrderService(pl.dmcs.order.OrderService delegate) {
    this.delegate = delegate;
  }

  public pl.dmcs.order.OrderService getDelegate() {
    return delegate;
  }

  public OrderService initializePersistence(Handler<AsyncResult<Void>> resultHandler) { 
    delegate.initializePersistence(resultHandler);
    return this;
  }

  public Single<Void> rxInitializePersistence() { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      initializePersistence(fut);
    }));
  }

  public OrderService save(Order order, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.save(order, resultHandler);
    return this;
  }

  public Single<Void> rxSave(Order order) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      save(order, fut);
    }));
  }

  public OrderService deleteOrder(Integer id, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.deleteOrder(id, resultHandler);
    return this;
  }

  public Single<Void> rxDeleteOrder(Integer id) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      deleteOrder(id, fut);
    }));
  }

  public OrderService checkAvailability(OrderDto orderDto, Handler<AsyncResult<Boolean>> resultHandler) { 
    delegate.checkAvailability(orderDto, resultHandler);
    return this;
  }

  public Single<Boolean> rxCheckAvailability(OrderDto orderDto) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      checkAvailability(orderDto, fut);
    }));
  }

  public OrderService getOrder(Integer id, Handler<AsyncResult<Order>> resultHandler) { 
    delegate.getOrder(id, resultHandler);
    return this;
  }

  public Single<Order> rxGetOrder(Integer id) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      getOrder(id, fut);
    }));
  }

  public OrderService createOrder(OrderDto orderDto, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.createOrder(orderDto, resultHandler);
    return this;
  }

  public Single<Void> rxCreateOrder(OrderDto orderDto) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      createOrder(orderDto, fut);
    }));
  }


  public static OrderService newInstance(pl.dmcs.order.OrderService arg) {
    return arg != null ? new OrderService(arg) : null;
  }
}
