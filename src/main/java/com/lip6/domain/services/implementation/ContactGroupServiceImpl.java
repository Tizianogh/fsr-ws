package com.lip6.domain.services.implementation;

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
        // List<Contact> contactByEmail =
        // this.contactServiceImpl.retrieveAllInformationsAboutContactByHisEmail(contact.getEmail());
        // Optional<ContactGroup> contactGroupById = this.getContactGroupById(idContactGroup);

        return Optional
                .ofNullable(this.daoc.addContactToGroup(contact, idContactGroup));
    }

    @Override
    public Optional<ContactGroup> getContactGroupById(Long idContactGroup) {
        Optional<ContactGroup> contactGroupById = Optional.ofNullable(this.daoc.getContactGroupWithFetch(idContactGroup));

        if (!contactGroupById.isPresent()) {
            System.out.println("Aucun groupe de contacts avec l'id " + idContactGroup + " trouvé en base de données.");
            return Optional.empty();
        }
        System.out.println("Le groupe de contact : " + contactGroupById.get().getLibelle() + ", a été crédité d'un utilisateur.");

        return contactGroupById;
    }

    @Override
    public Optional<ContactGroup> removeContactToContactGroup(Contact contact, Long idContactGroup) {
        return Optional
                .ofNullable(this.daoc.deletContactFromGroup(contact, idContactGroup));
    }

    @Override
    public boolean deletedContactGroup(Long idContactGroup) {
       boolean deletedContact = this.daoc.deleteContactGroup(idContactGroup);
       
       if(deletedContact) {
         System.out.println("Contact supprimé : " + idContactGroup);
          return deletedContact;
       } 
       
       return !deletedContact;
    }
}
