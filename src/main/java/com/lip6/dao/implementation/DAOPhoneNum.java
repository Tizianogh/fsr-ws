package com.lip6.dao.implementation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.lip6.dao.interfaces.IDAOPhoneNum;
import com.lip6.domain.model.PhoneNum;

@Service
@Qualifier("daoPhone")
public class DAOPhoneNum implements IDAOPhoneNum {
  @Autowired
  private EntityManagerFactory emf;

  @Override
  public int updatePhoneNumber(PhoneNum phoneNum) {
    EntityManager entityManager = this.emf.createEntityManager();
    EntityTransaction tx = entityManager.getTransaction();
    tx.begin();
    String request =
        "UPDATE PhoneNum c SET c.num=:num WHERE c.idPhoneNum=:idPhoneNum";

    Query query = entityManager.createQuery(request)
        .setParameter("num", phoneNum.getNum())
        .setParameter("idPhoneNum", phoneNum.getIdPhoneNum());

    int numberOfRowsUpdated = query.executeUpdate();
    tx.commit();
    entityManager.close();
    return numberOfRowsUpdated;
  }
}
