package pl.dmcs.order;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;
import pl.dmcs.order.dto.OrderDto;
import pl.dmcs.order.dto.TicketDto;

import java.nio.Buffer;

@VertxGen
@ProxyGen
public interface OrderService {

  String SERVICE_NAME = "order-eb-service";

  String SERVICE_ADDRESS = "service.order";

  @Fluent
  OrderService initializePersistence(Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  OrderService save(Order order,Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  OrderService deleteOrder(Integer id, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  OrderService checkAvailability(OrderDto orderDto, Handler<AsyncResult<Boolean>> resultHandler);

  @Fluent
  OrderService getOrder(Integer id, Handler<AsyncResult<Order>> resultHandler);

  @Fluent
  OrderService createOrder(OrderDto orderDto,Handler<AsyncResult<Void>> resultHandler);

}
