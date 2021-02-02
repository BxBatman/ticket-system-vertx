package pl.dmcs.order.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

@DataObject(generateConverter = true)
public class ReservationTicketDtoResult {
    private List<Integer> ticketNumbers;

    public ReservationTicketDtoResult() {
    }

    public ReservationTicketDtoResult(JsonObject json) {
        ReservationTicketDtoResultConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ReservationTicketDtoResultConverter.toJson(this, json);
        return json;
    }

    public List<Integer> getTicketNumbers() {
        return ticketNumbers;
    }

    public void setTicketNumbers(List<Integer> ticketNumbers) {
        this.ticketNumbers = ticketNumbers;
    }

    @Override
    public String toString() {
        return toJson().encodePrettily();
    }
}
