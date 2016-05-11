package com.kronsys.orders;

public class Order {
  private OrderStatus status;
  private int number;
 

  public Order(int number, OrderStatus status) {
    this.number = number;
    this.status = status;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  @Override
  public String toString() {
    return "Order [status=" + status + ", number=" + number + "]";
  }

}
