package pl.dmcs.catalog.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class ReservationTicketDto {
    private String title;
    private int quantity;

    public ReservationTicketDto() {
    }

    public ReservationTicketDto(String title, int quantity) {
        this.title = title;
        this.quantity = quantity;
    }

    public ReservationTicketDto(JsonObject json) {
        ReservationTicketDtoConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ReservationTicketDtoConverter.toJson(this, json);
        return json;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return toJson().encodePrettily();
    }
}
