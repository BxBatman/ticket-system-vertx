package pl.dmcs.order.api;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import pl.dmcs.common.RestAPIVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import pl.dmcs.order.Order;
import pl.dmcs.order.OrderService;
import pl.dmcs.order.dto.OrderDto;
import pl.dmcs.order.dto.ReservationTicketDtoResult;
import pl.dmcs.order.dto.TicketDto;

public class RestOrderAPIVerticle extends RestAPIVerticle {

  private static final String SERVICE_NAME = "order-rest-api";

  private static final int TICKET_PORT = 8081;
  private static final int PAYMENT_PORT = 8083;
  private static final String HOST = "localhost";

  private static final String API_GET = "/order/:id";
  private static final String MAKE_ORDER = "/order/make";
  private static final String MAKE_ORDER_AND_PAY = "/order/makeAndPay";
  private static final String API_SAVE = "/order";

  private final OrderService service;

  public RestOrderAPIVerticle(OrderService service) {
    this.service = service;
  }

  @Override
  public void start(Future<Void> future) throws Exception {
    super.start();


    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.get(API_GET).handler(this::getOrder);
    router.post(API_SAVE).handler(this::saveOrder);
    router.post(MAKE_ORDER).handler(this::makeOrder);
    router.post(MAKE_ORDER_AND_PAY).handler(this::makeOrderAndPay);

    String host = "localhost";
    int port =  8090;

    createHttpServer(router, host, port)
      .compose(serverCreated -> publishHttpEndpoint(SERVICE_NAME, host, port))
      .setHandler(future.completer());
  }

  private void getOrder(RoutingContext context) {
    Integer id = Integer.valueOf(context.request().getParam("id"));
    service.getOrder(id, resultHandlerWithResponse(context));
  }


  private void saveOrder(RoutingContext context) {
    Order order = new Order(context.getBodyAsJson());
    service.save(order, resultHandlerWithoutResponse(context,201));
  }

  private void makeOrder(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();
    OrderDto orderDto = new OrderDto(routingContext.getBodyAsJson());
    TicketDto ticketDto = orderDto.getTicketDto();

      WebClient client = WebClient.create(vertx);
      client.get(TICKET_PORT, HOST, "/ticket/availability/" + ticketDto.getTitle() + "/" + ticketDto.getQuantity())
              .send(ar -> {
                if (ar.succeeded() && ar.result().bodyAsString().equals("true")) {
                      client.post(TICKET_PORT,HOST,"/ticket/reserve").sendJsonObject(
                              new JsonObject()
                              .put("title",ticketDto.getTitle())
                              .put("quantity",ticketDto.getQuantity()),res ->{
                                if (res.succeeded()) {
                                  ReservationTicketDtoResult reservationTicketDtoResult = res.result().bodyAsJson(ReservationTicketDtoResult.class);
                                  Order order = new Order();
                                  order.setTicketNumbers(reservationTicketDtoResult.getTicketNumbers());
                                  order.setPersonIdentificationNumber(orderDto.getPersonIdentificationNumber());
                                  service.save(order, resultHandlerWithoutResponse(routingContext,201));
                                } else {
                                  ar.cause().printStackTrace();
                                }
                              });

                } else if (ar.succeeded() && ar.result().bodyAsString().equals("false")) {
                  HttpResponse<Buffer> resp = ar.result();
                  response.setStatusCode(200).end(resp.body());
                } else {
                  ar.cause().printStackTrace();
                }
              });

  }

  private void makeOrderAndPay(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();
    OrderDto orderDto = new OrderDto(routingContext.getBodyAsJson());
    TicketDto ticketDto = orderDto.getTicketDto();

    WebClient client = WebClient.create(vertx);
    client.get(TICKET_PORT, HOST, "/ticket/availability/" + ticketDto.getTitle() + "/" + ticketDto.getQuantity())
            .send(ar -> {
              if (ar.succeeded() && ar.result().bodyAsString().equals("true")) {
                client.post(TICKET_PORT,HOST,"/ticket/reserve").sendJsonObject(
                        new JsonObject()
                                .put("title",ticketDto.getTitle())
                                .put("quantity",ticketDto.getQuantity()),res ->{
                          if (res.succeeded()) {
                            ReservationTicketDtoResult reservationTicketDtoResult = res.result().bodyAsJson(ReservationTicketDtoResult.class);
                            Order order = new Order();
                            order.setTicketNumbers(reservationTicketDtoResult.getTicketNumbers());
                            order.setPersonIdentificationNumber(orderDto.getPersonIdentificationNumber());

                            client.post(PAYMENT_PORT,HOST,"/pay").sendJsonObject(
                                    new JsonObject()
                                    .put("person_identification_number",order.getPersonIdentificationNumber()), ress->{
                                      if (ress.succeeded()) {
                                        service.save(order, resultHandlerWithoutResponse(routingContext,201));
                                      } else {
                                        ress.cause().printStackTrace();
                                      }
                                    }
                            );
                          } else {
                            ar.cause().printStackTrace();
                          }
                        });

              } else if (ar.succeeded() && ar.result().bodyAsString().equals("false")) {
                HttpResponse<Buffer> resp = ar.result();
                response.setStatusCode(200).end(resp.body());
              } else {
                ar.cause().printStackTrace();
              }
            });
  }

}
