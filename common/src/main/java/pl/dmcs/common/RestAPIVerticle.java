package pl.dmcs.common;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.function.Function;

public abstract class RestAPIVerticle extends ServiceVerticle {

  protected Future<Void> createHttpServer(Router router, String host, int port) {
    Future<HttpServer> httpServerFuture = Future.future();
    vertx.createHttpServer()
      .requestHandler(router::accept)
      .listen(port, host, httpServerFuture.completer());
    return httpServerFuture.map(r -> null);
  }




  protected <T> Handler<AsyncResult<T>> resultHandler(RoutingContext context, Handler<T> handler) {
    return res -> {
      if (res.succeeded()) {
        handler.handle(res.result());
      } else {
        internalError(context, res.cause());
        res.cause().printStackTrace();
      }
    };
  }

  protected <T> Handler<AsyncResult<T>> resultHandler(RoutingContext context) {
    return ar -> {
      if (ar.succeeded()) {
        T res = ar.result();
        context.response()
          .putHeader("content-type", "application/json")
          .end(res == null ? "{}" : res.toString());
      } else {
        internalError(context, ar.cause());
        ar.cause().printStackTrace();
      }
    };
  }

  protected <T> Handler<AsyncResult<T>> resultHandler(RoutingContext context, Function<T, String> converter) {
    return ar -> {
      if (ar.succeeded()) {
        T res = ar.result();
        if (res == null) {
          serviceUnavailable(context, "invalid_result");
        } else {
          context.response()
            .putHeader("content-type", "application/json")
            .end(converter.apply(res));
        }
      } else {
        internalError(context, ar.cause());
        ar.cause().printStackTrace();
      }
    };
  }

  protected <T> Handler<AsyncResult<T>> resultHandlerWithResponse(RoutingContext context) {
    return ar -> {
      if (ar.succeeded()) {
        T res = ar.result();
        if (res == null) {
          notFound(context);
        } else {
          context.response()
            .putHeader("content-type", "application/json")
            .end(res.toString());
        }
      } else {
        internalError(context, ar.cause());
        ar.cause().printStackTrace();
      }
    };
  }

  protected Handler<AsyncResult<Void>> resultHandlerWithoutResponse(RoutingContext context, int status) {
    return ar -> {
      if (ar.succeeded()) {
        context.response()
          .setStatusCode(status == 0 ? 200 : status)
          .putHeader("content-type", "application/json")
          .end();
      } else {
        internalError(context, ar.cause());
        ar.cause().printStackTrace();
      }
    };
  }


  protected void notFound(RoutingContext context) {
    context.response().setStatusCode(404)
      .putHeader("content-type", "application/json")
      .end(new JsonObject().put("message", "not_found").encodePrettily());
  }

  protected void internalError(RoutingContext context, Throwable ex) {
    context.response().setStatusCode(500)
      .putHeader("content-type", "application/json")
      .end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
  }

  protected void serviceUnavailable(RoutingContext context, String cause) {
    context.response().setStatusCode(503)
      .putHeader("content-type", "application/json")
      .end(new JsonObject().put("error", cause).encodePrettily());
  }
}
