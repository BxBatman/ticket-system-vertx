package pl.dmcs.gateway;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import pl.dmcs.common.RestAPIVerticle;

public class APIGatewayVerticle extends RestAPIVerticle {

    private static final int DEFAULT_PORT = 8787;
    private static final int TICKET_PORT = 8081;
    private static final int ORDER_PORT = 8089;


    private static final Logger logger = LoggerFactory.getLogger(APIGatewayVerticle.class);

    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();

        String host = "localhost";
        int port = DEFAULT_PORT;

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.route("/ticket-rest-api/*").handler(this::handleTicketRestApi);
        router.route("/order-rest-api/*").handler(this::handleOrderRestApi);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(port, host, ar -> {
                    if (ar.succeeded()) {
                        publishApiGateway(host, port);
                        future.complete();
                        logger.info("API Gateway is running on port " + port);
                    } else {
                        future.fail(ar.cause());
                    }
                });
    }

    private void handleTicketRestApi(RoutingContext routingContext) {
        send(TICKET_PORT,routingContext);
    }

    private void handleOrderRestApi(RoutingContext routingContext) {
        send(ORDER_PORT,routingContext);
    }


    private void send(int port,RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        WebClient webClient = WebClient.create(vertx);

        String newPath = routingContext.request().uri();
        newPath = newPath.substring(16);
        System.out.println(newPath);

        System.out.println(routingContext.request().method());

        if (routingContext.request().method().toString() == "GET") {

            webClient.get(port, "localhost", newPath).send(ar -> {
                if (ar.succeeded()) {
                    HttpResponse<Buffer> resp = ar.result();
                    response.setStatusCode(200).end(resp.body());
                } else {
                    ar.cause().printStackTrace();
                }
            });
        } else if (routingContext.request().method().toString() == "POST") {
            webClient.post(port, "localhost", newPath).sendJsonObject(routingContext.getBodyAsJson(), res -> {
                if (res.succeeded()) {
                    HttpResponse<Buffer> resp = res.result();
                    response.setStatusCode(200).end(resp.body());
                } else {
                    res.cause().printStackTrace();
                }
            });
        } else if (routingContext.request().method().toString() == "PUT") {
            webClient.put(port, "localhost", newPath).sendJsonObject(routingContext.getBodyAsJson(), res -> {
                if (res.succeeded()) {
                    HttpResponse<Buffer> resp = res.result();
                    response.setStatusCode(200).end(resp.body());
                } else {
                    res.cause().printStackTrace();
                }
            });
        } else if (routingContext.request().method().toString() == "DELETE") {
            webClient.delete(port, "localhost", newPath).send(ar -> {
                if (ar.succeeded()) {
                    HttpResponse<Buffer> resp = ar.result();
                    response.setStatusCode(200).end(resp.body());
                } else {
                    ar.cause().printStackTrace();
                }
            });
        }
    }

}
