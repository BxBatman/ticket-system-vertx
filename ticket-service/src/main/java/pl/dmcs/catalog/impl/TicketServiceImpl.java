package pl.dmcs.catalog.impl;

import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import pl.dmcs.catalog.Ticket;
import pl.dmcs.catalog.TicketService;
import pl.dmcs.catalog.dto.ReservationTicketDto;
import pl.dmcs.catalog.dto.ReservationTicketDtoResult;
import pl.dmcs.catalog.dto.TicketDto;
import pl.dmcs.common.service.PostgresRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TicketServiceImpl extends PostgresRepository implements TicketService {


  public TicketServiceImpl(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  @Override
  public TicketService initializePersistence(Handler<AsyncResult<Void>> resultHandler) {
    client.getConnection(connectionHandler(resultHandler, connection -> {
      connection.execute(CREATE_STATEMENT, r -> {
        resultHandler.handle(r);
        connection.close();
      });
    }));
    return this;
  }

  @Override
  public TicketService save(Ticket ticket, Handler<AsyncResult<Void>> resultHandler) {
    JsonArray params = new JsonArray()
      .add(ticket.getTitle())
      .add(ticket.getDate())
      .add(ticket.getPrice())
      .add(ticket.isReserved());
    this.executeWithoutResult(params, INSERT_STATEMENT, resultHandler);
    return this;
  }

  @Override
  public TicketService get(Integer id, Handler<AsyncResult<Ticket>> resultHandler) {
    this.getOne(id, FETCH_STATEMENT)
      .map(option -> option.map(Ticket::new).orElse(null))
      .setHandler(resultHandler);
    return this;
  }

  @Override
  public TicketService saveMultiplesSameTickets(TicketDto ticketDto, Handler<AsyncResult<Void>> resultHandler) {
    Integer quantity = ticketDto.getQuantity();


    for (int i=0; i<quantity; i++) {
      JsonArray params = new JsonArray()
              .add(ticketDto.getTitle())
              .add(ticketDto.getDate())
              .add(ticketDto.getPrice())
              .add(false);
      this.executeWithoutResult(params, INSERT_STATEMENT, r->{});
    }

    resultHandler.handle(Future.succeededFuture());

    return this;
  }

  @Override
  public TicketService checkAvailability(String title, Integer quantity, Handler<AsyncResult<Boolean>> resultHandler) {
    this.getAll(FETCH_ALL_STATEMENT)
            .map(rawList -> rawList.stream()
                    .map(Ticket::new).count() >= quantity
            )
            .setHandler(resultHandler);
    return this;
  }

  @Override
  public TicketService getAll(Handler<AsyncResult<List<Ticket>>> resultHandler) {
    this.getAll(FETCH_ALL_STATEMENT)
            .map(rawList -> rawList.stream()
                    .map(Ticket::new)
                    .collect(Collectors.toList())
            )
            .setHandler(resultHandler);
    return this;
  }

  @Override
  public TicketService reserveTickets(ReservationTicketDto reservationTicketDto, Handler<AsyncResult<ReservationTicketDtoResult>> resultHandler) {
    JsonArray params = new JsonArray()
            .add(reservationTicketDto.getTitle());

    ReservationTicketDtoResult reservationTicketDtoResult = new ReservationTicketDtoResult();

    List<Integer> ticketIds = new ArrayList<>();
    List<Ticket> tickets = new ArrayList<>();

    getMany(params, FETCH_BY_TITLE_STATEMENT)
            .map(rawList -> rawList.stream()
                    .map(Ticket::new)
                    .collect(Collectors.toList())
            ).setHandler(ar -> {
      if (ar.succeeded()) {
        List<Ticket> result = ar.result();
        tickets.addAll(result);
        int i=0;

        for (Ticket ticket : tickets) {
          if (!ticket.isReserved() && i<reservationTicketDto.getQuantity()) {
            JsonArray parameters = new JsonArray()
                    .add(true)
                    .add(ticket.getId());
            executeWithoutResult(parameters, UPDATE_STATEMENT, r->{});
            ticketIds.add(ticket.getId());
            i++;
          }
        }
        reservationTicketDtoResult.setTicketNumbers(ticketIds);
        resultHandler.handle(Future.succeededFuture(reservationTicketDtoResult));

      } else {
        ar.cause().printStackTrace();
      }
    });


    return this;
  }


  @Override
  public TicketService getSpecificTickets(List<Integer> ticketIds ,Handler<AsyncResult<List<Ticket>>> resultHandler) {

    List<Ticket> tickets = new ArrayList<>();
    List<Future> registrationFutures = new ArrayList<>(ticketIds.size());


    for (Integer id : ticketIds) {
      Future<Ticket> future = Future.future();
      registrationFutures.add(future);
      getTicket(id, future.completer());
    }
    CompositeFuture.all(registrationFutures).setHandler(ar -> {
      if (ar.succeeded())  {
        for (int i=0 ;i < ticketIds.size();i++) {
          Ticket ticket = ar.result().result().resultAt(i);
          tickets.add(ticket);
        }
      }
        resultHandler.handle(Future.succeededFuture(tickets));
    });

    return this;
  }



  private void getTicket(int id,Handler<AsyncResult<Ticket>> resultHandler) {
    getOne(id, FETCH_BY_ID_STATEMENT)
            .map(option -> option.map(Ticket::new).orElse(null))
            .setHandler(ar ->{
              if (ar.succeeded()) {
                resultHandler.handle(Future.succeededFuture(ar.result()));
              } else  {
                ar.cause().printStackTrace();
              }
            });
  }




  private static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS `tickets` (\n" +
    "  `id` serial NOT NULL,\n" +
    "  `title` character varying(255) NOT NULL,\n" +
    "  `date` character varying(255) NOT NULL,\n" +
    "  `price` character varying(255) NOT NULL,\n" +
    "  `reserved` boolean NOT NULL,\n" +
    "  CONSTRAINT `tickets_pkey` PRIMARY KEY ('id') )";
  private static final String INSERT_STATEMENT = "INSERT INTO tickets (title, date, price, reserved) VALUES (?, ?, ?, ?)";
  private static final String FETCH_STATEMENT = "SELECT * FROM tickets WHERE id = ?";
  private static final String FETCH_BY_TITLE_STATEMENT = "SELECT * FROM tickets WHERE title = ?";
  private static final String FETCH_ALL_STATEMENT = "SELECT * FROM tickets";
  private static final String UPDATE_STATEMENT = "UPDATE tickets SET reserved = ? WHERE id = ?";
  private static final String FETCH_BY_ID_STATEMENT = "SELECT * FROM tickets WHERE id = ?";
}
