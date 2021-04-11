package pl.dmcs.payment.api;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import pl.dmcs.common.RestAPIVerticle;
import pl.dmcs.payment.Order;
import pl.dmcs.payment.StripeCharge;

public class RestPaymentAPIVerticle extends RestAPIVerticle {

    private String API_KEY = "sk_test_51Ib9BBHR0fD774KgxXs8jeM1zEtHMf8CJEulWHiWtTWzjyJP3gbWnLrnFFMgt4L6hKM8ORs9XN9bp18DLABholgs00TLVYsjuo";

    private static final String SERVICE_NAME = "payment-rest-api";

    private static final String HOST = "localhost";

    private static final String MAKE_PAYMENT = "/pay";

    public RestPaymentAPIVerticle() {
    }

    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();


        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.post(MAKE_PAYMENT).handler(this::makePayment);

        String host = "localhost";
        int port =  8083;

        createHttpServer(router, host, port)
                .compose(serverCreated -> publishHttpEndpoint(SERVICE_NAME, host, port))
                .setHandler(future.completer());
    }

    private void makePayment(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        Order order = new Order(routingContext.getBodyAsJson());

        Stripe.apiKey = API_KEY;
        StripeCharge stripeCharge = new StripeCharge(1800L,"mail@test.com",order.getId(),order.getPersonIdentificationNumber());
        try {
            Charge.create(stripeCharge.getCharge());
            response.setStatusCode(200).end();
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}
