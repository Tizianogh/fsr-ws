package com.lip6.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PhoneNum {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long idPhoneNum;

  private String num;

  @ManyToOne
  private Contact contact;

  public PhoneNum() {

  }

  public PhoneNum(String num) {
    this.num = num;
  }

  //
  // public PhoneNum(String num, Contact contact) {
  // this.num = num;
  // this.contact=contact;
  // }

  public String getNum() {
    return num;
  }

  public void setNum(String num) {
    this.num = num;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }


  public long getIdPhoneNum() {
    return idPhoneNum;
  }

  public void setIdPhoneNum(long idPhoneNum) {
    this.idPhoneNum = idPhoneNum;
  }

  @Override
  public String toString() {
    return "PhoneNum [idPhoneNum=" + idPhoneNum + ", num=" + num + ", contact=" + contact + "]";
  }
}
