package pl.dmcs.common.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.Optional;

public class PostgresRepository {

  protected final JDBCClient client;

  public PostgresRepository(Vertx vertx, JsonObject config) {
    this.client = JDBCClient.createNonShared(vertx, config);
  }

  protected void executeWithoutResult(JsonArray params, String sql, Handler<AsyncResult<Void>> resultHandler) {
    client.getConnection(connectionHandler(resultHandler, connection -> {
      connection.updateWithParams(sql, params, r -> {
        if (r.succeeded()) {
          resultHandler.handle(Future.succeededFuture());
        } else {
          resultHandler.handle(Future.failedFuture(r.cause()));
        }
        connection.close();
      });
    }));
  }

  protected <K> Future<Optional<JsonObject>> getOne(K param, String sql) {
    return getConnection()
      .compose(connection -> {
        Future<Optional<JsonObject>> future = Future.future();
        connection.queryWithParams(sql, new JsonArray().add(param), r -> {
          if (r.succeeded()) {
            List<JsonObject> resList = r.result().getRows();
            if (resList == null || resList.isEmpty()) {
              future.complete(Optional.empty());
            } else {
              future.complete(Optional.of(resList.get(0)));
            }
          } else {
            future.fail(r.cause());
          }
          connection.close();
        });
        return future;
      });
  }

  protected Future<List<JsonObject>> getMany(JsonArray param, String sql) {
    return getConnection().compose(connection -> {
      Future<List<JsonObject>> future = Future.future();
      connection.queryWithParams(sql, param, r -> {
        if (r.succeeded()) {
          future.complete(r.result().getRows());
        } else {
          future.fail(r.cause());
        }
        connection.close();
      });
      return future;
    });
  }

  protected Future<List<JsonObject>> getAll(String sql) {
    return getConnection().compose(connection -> {
      Future<List<JsonObject>> future = Future.future();
      connection.query(sql, r -> {
        if (r.succeeded()) {
          future.complete(r.result().getRows());
        } else {
          future.fail(r.cause());
        }
        connection.close();
      });
      return future;
    });
  }


  protected void deleteAll(String sql, Handler<AsyncResult<Void>> resultHandler) {
    client.getConnection(connectionHandler(resultHandler, connection -> {
      connection.update(sql, r -> {
        if (r.succeeded()) {
          resultHandler.handle(Future.succeededFuture());
        } else {
          resultHandler.handle(Future.failedFuture(r.cause()));
        }
        connection.close();
      });
    }));
  }

  protected <R> Handler<AsyncResult<SQLConnection>> connectionHandler(Handler<AsyncResult<R>> h1, Handler<SQLConnection> h2) {
    return conn -> {
      if (conn.succeeded()) {
        final SQLConnection connection = conn.result();
        h2.handle(connection);
      } else {
        h1.handle(Future.failedFuture(conn.cause()));
      }
    };
  }

  protected Future<SQLConnection> getConnection() {
    Future<SQLConnection> future = Future.future();
    client.getConnection(future.completer());
    return future;
  }

}
