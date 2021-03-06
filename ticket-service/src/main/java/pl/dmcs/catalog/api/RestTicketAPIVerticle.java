package pl.dmcs.catalog.api;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.dmcs.catalog.Ticket;
import pl.dmcs.catalog.TicketService;
import pl.dmcs.catalog.dto.ReservationTicketDtoResult;
import pl.dmcs.common.RestAPIVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import pl.dmcs.catalog.dto.ReservationTicketDto;
import pl.dmcs.catalog.dto.TicketDto;

public class RestTicketAPIVerticle extends RestAPIVerticle {

  private static final String SERVICE_NAME = "ticket-rest-api";

  private static final Logger logger = LoggerFactory.getLogger(RestTicketAPIVerticle.class);

  private final TicketService ticketService;

  private static final String API_SAVE = "/ticket";
  private static final String API_GET = "/ticket/:id";
  private static final String API_MULTIPLE_SAVE="/ticket/multiple";
  private static final String API_AVAILABILITY_CHECK="/ticket/availability/:title/:quantity";
  private static final String API_GET_ALL="/ticket/all/list";
  private static final String API_RESERVE="/ticket/reserve";
  private static final String API_GET_SPECIFIC_TICKETS="/ticket/specific/all";
  private static final String API_DELETE_TICKET = "/ticket/delete/:title";

  public RestTicketAPIVerticle(TicketService ticketService) {
    this.ticketService = ticketService;
  }

  @Override
  public void start(Future<Void> future) throws Exception {
    long startTime = System.nanoTime();
    super.start();
    final Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.post(API_SAVE).handler(this::apiAddTicket);
    router.post(API_MULTIPLE_SAVE).handler(this::apiMultipleAdd);
    router.get(API_GET).handler(this::apiGetTicket);
    router.get(API_AVAILABILITY_CHECK).handler(this::apiAvailabilityCheck);
    router.get(API_GET_ALL).handler(this::apiGetAll);
    router.post(API_RESERVE).handler(this::apiCheckAndReserveTickets);
    router.post(API_GET_SPECIFIC_TICKETS).handler(this::apiGetSpecificTickets);
    router.delete(API_DELETE_TICKET).handler(this::apiDeleteTicket);

    String host = config().getString("ticket.http.address", "localhost");
    int port = config().getInteger("ticket.http.port", 8081);

    createHttpServer(router, host, port)
      .compose(serverCreated -> publishHttpEndpoint(SERVICE_NAME, host, port))
      .setHandler(future.completer());
    long endTime = System.nanoTime();
    long duration = (endTime - startTime);
    logger.info("Startup time " + (double)duration/1000000000);
  }

  private void apiAddTicket(RoutingContext context) {
    Ticket ticket = new Ticket(context.getBodyAsJson());
    ticketService.save(ticket, resultHandlerWithoutResponse(context, 201));
  }

  private void apiMultipleAdd(RoutingContext context) {
    TicketDto ticketDto = new TicketDto(context.getBodyAsJson());
    ticketService.saveMultiplesSameTickets(ticketDto, resultHandlerWithResponse(context));
  }

  private void apiGetTicket(RoutingContext context) {
    Integer id = Integer.valueOf(context.request().getParam("id"));
    ticketService.get(id, resultHandlerWithResponse(context));
  }

  private void apiAvailabilityCheck(RoutingContext context) {
    String title = context.request().getParam("title");
    Integer quantity = Integer.valueOf(context.request().getParam("quantity"));
    ticketService.checkAvailability(title,quantity, resultHandlerWithResponse(context));
  }

  private void apiGetAll(RoutingContext context) {
    ticketService.getAll(resultHandlerWithResponse(context));
  }

  private void apiCheckAndReserveTickets(RoutingContext context) {
    ReservationTicketDto reservationTicketDto = new ReservationTicketDto(context.getBodyAsJson());
    ticketService.reserveTickets(reservationTicketDto, resultHandlerWithResponse(context));
  }

  private void apiGetSpecificTickets(RoutingContext context) {
    ReservationTicketDtoResult reservationTicketDtoResult = new ReservationTicketDtoResult(context.getBodyAsJson());
    ticketService.getSpecificTickets(reservationTicketDtoResult.getTicketNumbers(), resultHandlerWithResponse(context));
  }

  private void apiDeleteTicket(RoutingContext context) {
    String title = context.request().getParam("title");
    ticketService.deleteTicket(title,resultHandlerWithoutResponse(context, 200));
  }

}
