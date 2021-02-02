package pl.dmcs.catalog.impl;

import io.vertx.core.Future;
import pl.dmcs.catalog.dto.ReservationTicketDtoResult;
import pl.dmcs.common.service.JdbcRepositoryWrapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.dmcs.catalog.Ticket;
import pl.dmcs.catalog.TicketService;
import pl.dmcs.catalog.dto.ReservationTicketDto;
import pl.dmcs.catalog.dto.TicketDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JdbcTicketServiceImpl extends JdbcRepositoryWrapper implements TicketService {

  private static final Logger logger = LoggerFactory.getLogger(JdbcTicketServiceImpl.class);

  public JdbcTicketServiceImpl(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  @Override
  public TicketService initializePersistence(Handler<AsyncResult<Void>> resultHandler) {
    client.getConnection(connHandler(resultHandler, connection -> {
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
    this.executeNoResult(params, INSERT_STATEMENT, resultHandler);
    return this;
  }

  @Override
  public TicketService get(Integer id, Handler<AsyncResult<Ticket>> resultHandler) {
    this.retrieveOne(id, FETCH_STATEMENT)
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
      this.executeNoResult(params, INSERT_STATEMENT, resultHandler);
    }

    return this;
  }

  @Override
  public TicketService checkAvailability(String title, Integer quantity, Handler<AsyncResult<Boolean>> resultHandler) {
    this.retrieveAll(FETCH_ALL_STATEMENT)
            .map(rawList -> rawList.stream()
                    .map(Ticket::new).count() >= quantity
            )
            .setHandler(resultHandler);
    return this;
  }

  @Override
  public TicketService getAll(Handler<AsyncResult<List<Ticket>>> resultHandler) {
    this.retrieveAll(FETCH_ALL_STATEMENT)
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

    retrieveMany(params, FETCH_BY_TITLE_STATEMENT)
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
            executeNoResult(parameters, UPDATE_STATEMENT,r->{});
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
}
