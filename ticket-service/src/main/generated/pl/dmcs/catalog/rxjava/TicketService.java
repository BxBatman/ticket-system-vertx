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

package pl.dmcs.catalog.rxjava;

import java.util.Map;
import rx.Observable;
import rx.Single;
import java.util.List;
import pl.dmcs.catalog.dto.ReservationTicketDtoResult;
import pl.dmcs.catalog.dto.TicketDto;
import io.vertx.core.AsyncResult;
import pl.dmcs.catalog.dto.ReservationTicketDto;
import io.vertx.core.Handler;
import pl.dmcs.catalog.Ticket;


@io.vertx.lang.rxjava.RxGen(pl.dmcs.catalog.TicketService.class)
public class TicketService {

  public static final io.vertx.lang.rxjava.TypeArg<TicketService> __TYPE_ARG = new io.vertx.lang.rxjava.TypeArg<>(
    obj -> new TicketService((pl.dmcs.catalog.TicketService) obj),
    TicketService::getDelegate
  );

  private final pl.dmcs.catalog.TicketService delegate;
  
  public TicketService(pl.dmcs.catalog.TicketService delegate) {
    this.delegate = delegate;
  }

  public pl.dmcs.catalog.TicketService getDelegate() {
    return delegate;
  }

  public TicketService initializePersistence(Handler<AsyncResult<Void>> resultHandler) { 
    delegate.initializePersistence(resultHandler);
    return this;
  }

  public Single<Void> rxInitializePersistence() { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      initializePersistence(fut);
    }));
  }

  public TicketService save(Ticket ticket, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.save(ticket, resultHandler);
    return this;
  }

  public Single<Void> rxSave(Ticket ticket) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      save(ticket, fut);
    }));
  }

  public TicketService get(Integer id, Handler<AsyncResult<Ticket>> resultHandler) { 
    delegate.get(id, resultHandler);
    return this;
  }

  public Single<Ticket> rxGet(Integer id) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      get(id, fut);
    }));
  }

  public TicketService saveMultiplesSameTickets(TicketDto ticketDto, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.saveMultiplesSameTickets(ticketDto, resultHandler);
    return this;
  }

  public Single<Void> rxSaveMultiplesSameTickets(TicketDto ticketDto) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      saveMultiplesSameTickets(ticketDto, fut);
    }));
  }

  public TicketService checkAvailability(String title, Integer quantity, Handler<AsyncResult<Boolean>> resultHandler) { 
    delegate.checkAvailability(title, quantity, resultHandler);
    return this;
  }

  public Single<Boolean> rxCheckAvailability(String title, Integer quantity) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      checkAvailability(title, quantity, fut);
    }));
  }

  public TicketService getAll(Handler<AsyncResult<List<Ticket>>> resultHandler) { 
    delegate.getAll(resultHandler);
    return this;
  }

  public Single<List<Ticket>> rxGetAll() { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      getAll(fut);
    }));
  }

  public TicketService reserveTickets(ReservationTicketDto reservationTicketDto, Handler<AsyncResult<ReservationTicketDtoResult>> resultHandler) { 
    delegate.reserveTickets(reservationTicketDto, resultHandler);
    return this;
  }

  public Single<ReservationTicketDtoResult> rxReserveTickets(ReservationTicketDto reservationTicketDto) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      reserveTickets(reservationTicketDto, fut);
    }));
  }

  public TicketService getSpecificTickets(List<Integer> ticketIds, Handler<AsyncResult<List<Ticket>>> resultHandler) { 
    delegate.getSpecificTickets(ticketIds, resultHandler);
    return this;
  }

  public Single<List<Ticket>> rxGetSpecificTickets(List<Integer> ticketIds) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      getSpecificTickets(ticketIds, fut);
    }));
  }


  public static TicketService newInstance(pl.dmcs.catalog.TicketService arg) {
    return arg != null ? new TicketService(arg) : null;
  }
}
