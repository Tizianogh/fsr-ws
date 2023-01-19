package com.lip6.dao.interfaces;

import com.lip6.domain.model.Contact;
import com.lip6.domain.model.ContactGroup;
import com.lip6.domain.model.DTO.ContactGroupDTO;

public interface IDAOContactGroup {
    public ContactGroup createContactGroup(ContactGroup contactGroup);

    public ContactGroupDTO getContactGroupById(Long idGroupContact);

    public ContactGroup getContactGroupWithFetch(Long idGroupContact);

    public ContactGroup addContactToGroup(Contact emailContact, Long idContactGroup);

    public ContactGroup getGroupContactById(Long idGroupContact);

    public ContactGroup deletContactFromGroup(Contact emailContact, Long idContactGroup);
    
    public boolean deleteContactGroup(Long idContactGroup);
}
