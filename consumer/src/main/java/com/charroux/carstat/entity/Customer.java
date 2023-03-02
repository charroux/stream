package com.charroux.carstat.entity;


public class Customer {

    long id;
    int credit;

    public Customer() {}

    public Customer(int credit) {
        this.credit = credit;
    }

    public void reserveCredit(int amount) throws CreditException {
        if(credit >= amount){
            credit = credit - amount;
        } else {
            throw new CreditException();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", credit=" + credit +
                '}';
    }
}
