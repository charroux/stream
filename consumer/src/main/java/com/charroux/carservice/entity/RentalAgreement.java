package com.charroux.carservice.entity;

import java.util.ArrayList;
import java.util.Collection;

public class RentalAgreement {

    long id;
    long customerId;
    Collection<Car> cars = new ArrayList<Car>();

    public enum State{
        PENDING,
        CREDIT_RESERVED,
        CREDIT_REJECTED
    }

    State state;

    public RentalAgreement(long customerId, State state) {
        this.customerId = customerId;
        this.state = state;
    }

    public RentalAgreement() { }

    public void addCar(Car car){
        cars.add(car);
        car.setRentalAgreement(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Collection<Car> getCars() {
        return cars;
    }

    public void setCars(Collection<Car> cars) {
        this.cars = cars;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "RentalAgreement{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", cars=" + cars +
                ", state=" + state +
                '}';
    }
}
