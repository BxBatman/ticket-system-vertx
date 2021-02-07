package pl.dmcs.gateway;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;
import pl.dmcs.common.RestAPIVerticle;

import java.util.List;
import java.util.Optional;

public class APIGatewayVerticle extends RestAPIVerticle {

    private static final int DEFAULT_PORT = 8787;

    private static final Logger logger = LoggerFactory.getLogger(APIGatewayVerticle.class);

    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();

        // get HTTP host and port from configuration, or use default value
        String host = config().getString("api.gateway.http.address", "localhost");
        int port = config().getInteger("api.gateway.http.port", DEFAULT_PORT);

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());


//        router.route("/api/*").handler(this::dispatchRequests);
        router.route("/ticket-rest-api/*").handler(this::handleTicketRestApi);

        // static content
        router.route("/*").handler(StaticHandler.create());


        // create http server
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(port, host, ar -> {
                    if (ar.succeeded()) {
                        publishApiGateway(host, port);
                        future.complete();
                        logger.info("API Gateway is running on port " + port);
                        // publish log
                        publishGatewayLog("api_gateway_init_success:" + port);
                    } else {
                        future.fail(ar.cause());
                    }
                });
    }

    private void handleTicketRestApi(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        WebClient webClient = WebClient.create(vertx);

        String newPath = routingContext.request().uri();
        newPath = newPath.substring(16);
        System.out.println(newPath);

        System.out.println(routingContext.request().method());

        webClient.request(routingContext.request().method(),8081,"localhost",newPath).send( ar->{
            if (ar.succeeded()) {
                HttpResponse<Buffer> resp = ar.result();
                response.setStatusCode(200).end(resp.body());
            } else {
                ar.cause().printStackTrace();
            }
        });
    }





    // log methods

    private void publishGatewayLog(String info) {
        JsonObject message = new JsonObject()
                .put("info", info)
                .put("time", System.currentTimeMillis());
        publishLogEvent("gateway", message);
    }

    private void publishGatewayLog(JsonObject msg) {
        JsonObject message = msg.copy()
                .put("time", System.currentTimeMillis());
        publishLogEvent("gateway", message);
    }


    private String buildHostURI() {
        int port = config().getInteger("api.gateway.http.port", DEFAULT_PORT);
        final String host = config().getString("api.gateway.http.address.external", "localhost");
        return String.format("https://%s:%d", host, port);
    }

}
