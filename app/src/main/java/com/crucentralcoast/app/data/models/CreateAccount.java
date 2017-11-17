package com.crucentralcoast.app.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.ParcelConstructor;

/**
 * Created by Dylan on 11/4/17.
 */

public class CreateAccount {

   public static final String sGender = "gender";
   public static final String sPhoneNumber = "phoneNumber";
   public static final String sPassword = "password";
   public static final String sName = "name";
   public static final String sEmail = "email";
   public static final String sPhone = "phone";



   @SerializedName(sGender) public int gender;
   @SerializedName(sPhoneNumber) public String phoneNumber;
   @SerializedName(sPassword) public String password;
   @SerializedName(sName) public CruName name;
   @SerializedName(sEmail) public String email;
   @SerializedName(sPhone) public String phone;

   @ParcelConstructor
   public CreateAccount (CruName inputName, int inputGender,
                  String inputPhoneNumber, String inputEmail, String inputPassword) {

      this.name = inputName;
      this.gender = inputGender;
      this.phoneNumber = inputPhoneNumber;
      this.email = inputEmail;
      this.password = inputPassword;
   }


}
