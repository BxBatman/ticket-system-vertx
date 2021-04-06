package pl.dmcs.payment;

import java.util.HashMap;
import java.util.Map;

public class StripeCharge {
    private final long amount;
    private final String receiptEmail;
    private final String source;
    private final String currency;
    private final Integer orderId;
    private final String personIdentificationNumber;

    public StripeCharge(long amount, String receiptEmail,Integer orderId, String personIdentificationNumber) {
        this.amount = amount;
        this.receiptEmail = receiptEmail;
        this.source = "tok_visa";
        this.currency = "pln";
        this.orderId = orderId;
        this.personIdentificationNumber = personIdentificationNumber;
    }

    public Map<String, Object> getCharge() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("amount", this.amount);
        params.put("currency", this.currency);
        // source should obtained with Stripe.js
        params.put("source", this.source);
        params.put(
                "description",
                "Vertx Person ID:" + personIdentificationNumber
        );
        params.put("receipt_email",this.receiptEmail);
        return params;
    }
}
