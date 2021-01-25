package pl.dmcs.order.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

@DataObject(generateConverter = true)
public class OrderDto {
    private List<Integer> tickets;
    private String personIdentificationNumber;

    public OrderDto() {
    }

    public OrderDto(JsonObject json) {
        OrderDtoConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        OrderDtoConverter.toJson(this, json);
        return json;
    }


    public List<Integer> getTickets() {
        return tickets;
    }

    public void setTickets(List<Integer> tickets) {
        this.tickets = tickets;
    }

    public String getPersonIdentificationNumber() {
        return personIdentificationNumber;
    }

    public void setPersonIdentificationNumber(String personIdentificationNumber) {
        this.personIdentificationNumber = personIdentificationNumber;
    }

    @Override
    public String toString() {
        return toJson().encodePrettily();
    }
}
