package com.ictak.expensetrackerbe.dbmodels;

import javax.persistence.*;

@Entity
@Table(name="paymentType")
public class PaymentTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String type;

    public PaymentTypeEntity() {
    }

    public PaymentTypeEntity(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
