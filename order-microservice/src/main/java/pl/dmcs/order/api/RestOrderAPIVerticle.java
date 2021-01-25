package pl.dmcs.order.api;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import pl.dmcs.common.RestAPIVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import pl.dmcs.order.Order;
import pl.dmcs.order.OrderService;

public class RestOrderAPIVerticle extends RestAPIVerticle {

  private static final Logger logger = LoggerFactory.getLogger(RestOrderAPIVerticle.class);

  private static final String SERVICE_NAME = "order-rest-api";

  private static final String API_GET = "/order/:id";
  private static final String API_SAVE = "/order";
  private static final String API_GET_TICKET="/order/ticket/:id";

  private final OrderService service;

  public RestOrderAPIVerticle(OrderService service) {
    this.service = service;
  }

  @Override
  public void start(Future<Void> future) throws Exception {
    super.start();


    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.get(API_GET).handler(this::apiGetOrder);
    router.post(API_SAVE).handler(this::apiSaveOrder);
    router.get(API_GET_TICKET).handler(this::apiGetTicket);

    String host = config().getString("order.http.address", "0.0.0.0");
    int port = config().getInteger("order.http.port", 8090);

    createHttpServer(router, host, port)
      .compose(serverCreated -> publishHttpEndpoint(SERVICE_NAME, host, port))
      .setHandler(future.completer());
  }

  private void apiGetOrder(RoutingContext context) {
    Integer id = Integer.valueOf(context.request().getParam("id"));
    service.getOrder(id,resultHandlerNonEmpty(context));
  }


  private void apiSaveOrder(RoutingContext context) {
    Order order = new Order(context.getBodyAsJson());
    service.save(order,resultVoidHandler(context,201));
  }

  private void apiGetTicket(RoutingContext context) {
    HttpServerResponse response = context.response();
    Integer id = Integer.valueOf(context.request().getParam("id"));
    WebClient client = WebClient.create(vertx);
    client.get(8081,"localhost","/ticket/" + id)
            .send(ar-> {
              if (ar.succeeded()) {
                HttpResponse<Buffer> resp = ar.result();
                response.setStatusCode(200).end(resp.body());
              } else {
                ar.cause().printStackTrace();
              }
            });
  }

}
