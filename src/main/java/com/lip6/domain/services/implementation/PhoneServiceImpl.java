package com.lip6.domain.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lip6.dao.interfaces.IDAOPhoneNum;
import com.lip6.domain.model.PhoneNum;
import com.lip6.domain.services.interfaces.PhoneNumService;

@Service
@Qualifier("PhoneService")
@Transactional
public class PhoneServiceImpl implements PhoneNumService {

  @Autowired
  @Qualifier("daoPhone")
  private IDAOPhoneNum daoc;

  @Override
  public int updatedPhoneNum(PhoneNum phoneNum) {
    int numberOfUpdatedRows = this.daoc.updatePhoneNumber(phoneNum);

    if (numberOfUpdatedRows >= 1) {
      System.out.println(numberOfUpdatedRows + " ligne(s) modifiée(s)");
    }
    return numberOfUpdatedRows;
  }

}
