package pl.dmcs.common;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class RestAPIVerticle extends BaseMicroserviceVerticle {

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

  protected <T> Handler<AsyncResult<T>> resultHandlerNonEmpty(RoutingContext context) {
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

  protected <T> Handler<AsyncResult<T>> rawResultHandler(RoutingContext context) {
    return ar -> {
      if (ar.succeeded()) {
        T res = ar.result();
        context.response()
          .end(res == null ? "" : res.toString());
      } else {
        internalError(context, ar.cause());
        ar.cause().printStackTrace();
      }
    };
  }

  protected Handler<AsyncResult<Void>> resultVoidHandler(RoutingContext context, JsonObject result) {
    return resultVoidHandler(context, result, 200);
  }

  protected Handler<AsyncResult<Void>> resultVoidHandler(RoutingContext context, JsonObject result, int status) {
    return ar -> {
      if (ar.succeeded()) {
        context.response()
          .setStatusCode(status == 0 ? 200 : status)
          .putHeader("content-type", "application/json")
          .end(result.encodePrettily());
      } else {
        internalError(context, ar.cause());
        ar.cause().printStackTrace();
      }
    };
  }

  protected Handler<AsyncResult<Void>> resultVoidHandler(RoutingContext context, int status) {
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

  protected Handler<AsyncResult<Void>> deleteResultHandler(RoutingContext context) {
    return res -> {
      if (res.succeeded()) {
        context.response().setStatusCode(204)
          .putHeader("content-type", "application/json")
          .end(new JsonObject().put("message", "delete_success").encodePrettily());
      } else {
        internalError(context, res.cause());
        res.cause().printStackTrace();
      }
    };
  }

  // helper method dealing with failure

  protected void badRequest(RoutingContext context, Throwable ex) {
    context.response().setStatusCode(400)
      .putHeader("content-type", "application/json")
      .end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
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

  protected void notImplemented(RoutingContext context) {
    context.response().setStatusCode(501)
      .putHeader("content-type", "application/json")
      .end(new JsonObject().put("message", "not_implemented").encodePrettily());
  }

  protected void badGateway(Throwable ex, RoutingContext context) {
    ex.printStackTrace();
    context.response()
      .setStatusCode(502)
      .putHeader("content-type", "application/json")
      .end(new JsonObject().put("error", "bad_gateway")
        //.put("message", ex.getMessage())
        .encodePrettily());
  }

  protected void serviceUnavailable(RoutingContext context) {
    context.fail(503);
  }

  protected void serviceUnavailable(RoutingContext context, Throwable ex) {
    context.response().setStatusCode(503)
      .putHeader("content-type", "application/json")
      .end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
  }

  protected void serviceUnavailable(RoutingContext context, String cause) {
    context.response().setStatusCode(503)
      .putHeader("content-type", "application/json")
      .end(new JsonObject().put("error", cause).encodePrettily());
  }
}
