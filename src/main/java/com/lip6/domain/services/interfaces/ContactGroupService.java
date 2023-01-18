package com.lip6.domain.services.interfaces;

import java.util.Optional;

import com.lip6.domain.model.Contact;
import com.lip6.domain.model.ContactGroup;

public interface ContactGroupService {
    public Optional<ContactGroup> createContactGroup(ContactGroup contactGroup);

    public Optional<ContactGroup> addContactToContactGroup(Contact contact, Long idContactGroup);

    public Optional<ContactGroup> removeContactToContactGroup(Contact contact, Long idContactGroup);

    public Optional<ContactGroup> getContactGroupById(Long idContactGroup);
}
