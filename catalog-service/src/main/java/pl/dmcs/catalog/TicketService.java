package pl.dmcs.catalog;

import pl.dmcs.catalog.dto.ReservationTicketDto;
import pl.dmcs.catalog.dto.ReservationTicketDtoResult;
import pl.dmcs.catalog.dto.TicketDto;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;

@VertxGen
@ProxyGen
public interface TicketService {

  String SERVICE_NAME = "ticket-eb-service";

  String SERVICE_ADDRESS = "service.ticket";

  @Fluent
  TicketService initializePersistence(Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  TicketService save(Ticket ticket, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  TicketService get(Integer id, Handler<AsyncResult<Ticket>> resultHandler);

  @Fluent
  TicketService saveMultiplesSameTickets(TicketDto ticketDto, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  TicketService checkAvailability(String title, Integer quantity,Handler<AsyncResult<Boolean>> resultHandler);

  @Fluent
  TicketService getAll(Handler<AsyncResult<List<Ticket>>> resultHandler);

  @Fluent
  TicketService reserveTickets(ReservationTicketDto reservationTicketDto, Handler<AsyncResult<ReservationTicketDtoResult>> resultHandler);


}
