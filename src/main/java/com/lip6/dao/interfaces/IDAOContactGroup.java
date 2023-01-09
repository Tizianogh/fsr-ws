package com.lip6.dao.interfaces;

import com.lip6.domain.model.ContactGroup;
import com.lip6.domain.model.DTO.ContactGroupDTO;

public interface IDAOContactGroup {
    public ContactGroup createContactGroup(ContactGroup contactGroup);

    public ContactGroupDTO getContactGroupById(Long idGroupContact);

    public ContactGroup getContactGroupProxyById(Long idGroupContact);
}
