package pl.dmcs.order;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DataObject(generateConverter = true)
public class Order {

  private Integer id;
  private List<Integer> ticketNumbers = new ArrayList<>();
  private String personIdentificationNumber;


  public Order() {
  }


  public Order(JsonObject json) {
    OrderConverter.fromJson(json, this);
    if (json.getValue("ticket_numbers") instanceof String) {
      this.ticketNumbers = new JsonArray(json.getString("ticket_numbers"))
        .stream()
        .map(e -> (Integer) e)
        .collect(Collectors.toList());
    }
    this.personIdentificationNumber = json.getString("person_identification_number");
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    OrderConverter.toJson(this, json);
    return json;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<Integer> getTicketNumbers() {
    return ticketNumbers;
  }

  public void setTicketNumbers(List<Integer> ticketNumbers) {
    this.ticketNumbers = ticketNumbers;
  }

  public String getPersonIdentificationNumber() {
    return personIdentificationNumber;
  }

  public void setPersonIdentificationNumber(String personIdentificationNumber) {
    this.personIdentificationNumber = personIdentificationNumber;
  }

  @Override
  public String toString() {
    return this.toJson().encodePrettily();
  }
}
