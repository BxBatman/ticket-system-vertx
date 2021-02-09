package pl.dmcs.catalog;

import pl.dmcs.catalog.api.RestTicketAPIVerticle;
import pl.dmcs.catalog.impl.TicketServiceImpl;
import pl.dmcs.common.ServiceVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ProxyHelper;

import static pl.dmcs.catalog.TicketService.SERVICE_ADDRESS;
import static pl.dmcs.catalog.TicketService.SERVICE_NAME;


public class TicketVerticle extends ServiceVerticle {

  private TicketService ticketService;

  @Override
  public void start(Future<Void> future) throws Exception {
    super.start();

    ticketService = new TicketServiceImpl(vertx, config());
    ProxyHelper.registerService(TicketService.class, vertx, ticketService, SERVICE_ADDRESS);
    publishEventBusService(SERVICE_NAME, SERVICE_ADDRESS, TicketService.class)
      .compose(servicePublished -> deployRestVerticle())
      .setHandler(future.completer());
  }

  private Future<Void> deployRestVerticle() {
    Future<String> future = Future.future();
    vertx.deployVerticle(new RestTicketAPIVerticle(ticketService),
      new DeploymentOptions().setConfig(config()),
      future.completer());
    return future.map(r -> null);
  }
}
