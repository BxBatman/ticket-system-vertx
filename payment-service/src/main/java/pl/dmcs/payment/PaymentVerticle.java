package pl.dmcs.payment;

import io.vertx.core.Future;
import pl.dmcs.common.ServiceVerticle;
import pl.dmcs.payment.api.RestPaymentAPIVerticle;

public class PaymentVerticle extends ServiceVerticle {

    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();
        vertx.deployVerticle(new RestPaymentAPIVerticle());
    }




}
