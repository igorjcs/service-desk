package com.servicedesk.service_desk.models;

public enum TicketStatus {
    OPEN("OPEN"),
    CLOSED("CLOSED");

    private String status;

    TicketStatus (String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

}
