package com.model;

import java.time.LocalDateTime;
import java.io.Serializable;

public class Bug implements Serializable{

    private Integer id;
    private String denumire;
    private String descriere;
    private Gravitate gravitate;
    private Status status;

    private String timestamp;

    public Bug(Integer id,String denumire, String descriere, Gravitate gravitate, Status status, String timestamp) {
        this.id = id;
        this.denumire = denumire;
        this.descriere = descriere;
        this.gravitate = gravitate;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Gravitate getGravitate() {
        return gravitate;
    }

    public void setGravitate(Gravitate gravitate) {
        this.gravitate = gravitate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
