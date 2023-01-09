package com.lip6.domain.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lip6.dao.interfaces.IDAOContactGroup;
import com.lip6.domain.model.Contact;
import com.lip6.domain.model.ContactGroup;
import com.lip6.domain.services.interfaces.ContactGroupService;

@Service
@Qualifier("contactGroupService")
@Transactional
public class ContactGroupServiceImpl implements ContactGroupService {
    @Autowired
    @Qualifier("daoContactGroup")
    private IDAOContactGroup daoc;

    @Autowired
    @Qualifier("contactService")
    private ContactServiceImpl contactServiceImpl;

    @Override
    public Optional<ContactGroup> createContactGroup(ContactGroup contactGroup) {
        return Optional.of(this.daoc.createContactGroup(contactGroup));
    }

    @Override
    public Optional<ContactGroup> addContactToContactGroup(Contact contact, Long idContactGroup) {
        List<Contact> contactByEmail = this.contactServiceImpl.retrieveAllInformationsAboutContactByHisEmail(contact.getEmail());
        Optional<ContactGroup> contactGroupById = this.getContactGroupById(idContactGroup);

        if (!contactGroupById.get().getContacts().add(contactByEmail.get(0))) {
            System.out.println("Le contact n'a pas été ajouté au group de contact");
            return Optional.empty();
        }

        return contactGroupById;
    }

    @Override
    public Optional<ContactGroup> getContactGroupById(Long idContactGroup) {
        Optional<ContactGroup> contactGroupById = Optional.ofNullable(this.daoc.getContactGroupProxyById(idContactGroup));

        if (!contactGroupById.isPresent()) {
            System.out.println("Aucun groupe de contacts avec l'id " + idContactGroup + " trouvé en base de données.");
            return Optional.empty();
        }
        System.out.println("Le groupe de contact : " + contactGroupById.get().getLibelle() + ", a été crédité d'un utilisateur.");

        return contactGroupById;
    }
}