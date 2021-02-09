package pl.dmcs.order.impl;

import pl.dmcs.common.service.PostgresRepository;
import pl.dmcs.order.Order;
import pl.dmcs.order.OrderService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import pl.dmcs.order.dto.OrderDto;

public class OrderServiceImpl extends PostgresRepository implements OrderService {



  public OrderServiceImpl(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }


  @Override
  public OrderService initializePersistence(Handler<AsyncResult<Void>> resultHandler) {
    client.getConnection(connectionHandler(resultHandler, connection -> {
      connection.execute(CREATE_STATEMENT, r -> {
        resultHandler.handle(r);
        connection.close();
      });
    }));
    return this;
  }

  @Override
  public OrderService save(Order order, Handler<AsyncResult<Void>> resultHandler) {
    JsonArray params = new JsonArray()
            .add(order.getPersonIdentificationNumber())
            .add(new JsonArray(order.getTicketNumbers()).encode());

    executeWithoutResult(params, INSERT_STATEMENT, resultHandler);

    return this;
  }

  @Override
  public OrderService deleteOrder(Integer id, Handler<AsyncResult<Void>> resultHandler) {
    JsonArray params = new JsonArray()
            .add(id);
    executeWithoutResult(params,DELETE_STATEMENT,resultHandler);
    return this;
  }

  @Override
  public OrderService checkAvailability(OrderDto orderDto, Handler<AsyncResult<Boolean>> resultHandler) {
    return null;
  }

  @Override
  public OrderService getOrder(Integer id, Handler<AsyncResult<Order>> resultHandler) {
    this.getOne(id, GET_STATEMENT)
            .map(option -> option.map(Order::new).orElse(null))
            .setHandler(resultHandler);

    return this;
  }




  @Override
  public OrderService createOrder(OrderDto orderDto, Handler<AsyncResult<Void>> resultHandler) {
    return null;
  }


  private static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS orders (\n" +
          "  id serial NOT NULL,\n" +
          "  person_identification_number character varying(255) NOT NULL,\n" +
          "  ticket_numbers character varying(512) NOT NULL,\n" +
          "   CONSTRAINT orders_pkey PRIMARY KEY (id) )";
  private static final String INSERT_STATEMENT = "INSERT INTO orders (person_identification_number, ticket_numbers) VALUES (?, ?)";
  private static final String GET_STATEMENT = "SELECT * FROM orders WHERE id = ?";
  private static final String DELETE_STATEMENT = "DELETE FROM orders where id = ?";
}
