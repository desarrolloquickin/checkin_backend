package com.fjrotgerl.checkinonline.entities;


import java.util.List;

public class PDF {

    private String id;

    private List<String> pdfList;

    private int personaId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPdfList() {
        return pdfList;
    }

    public void setPdfList(List<String> pdfList) {
        this.pdfList = pdfList;
    }

    public int getPersonaId() {
        return personaId;
    }

    public void setPersonaId(int personaId) {
        this.personaId = personaId;
    }
}
