package com.lip6.domain.model.DTO;

public class ContactGroupDTO {
    private Long idGroupContact;
    private String libelle;

    public ContactGroupDTO(Long idContactGroup, String libelle) {
        this.idGroupContact = idContactGroup;
        this.libelle = libelle;
    }

    public Long getIdGroupContact() {
        return idGroupContact;
    }

    public void setIdGroupContact(Long idGroupContact) {
        this.idGroupContact = idGroupContact;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return "ContactGroupDTO [idGroupContact=" + idGroupContact + ", libelle=" + libelle + "]";
    }
}
