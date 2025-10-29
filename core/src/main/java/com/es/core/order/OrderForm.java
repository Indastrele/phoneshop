package com.es.core.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class OrderForm {
    @NotBlank(message = "This field is required")
    @Size(max = 50, message = "Maximum 50 characters")
    private String firstName;
    @NotBlank(message = "This field is required")
    @Size(max = 50, message = "Maximum 50 characters")
    private String lastName;
    @NotBlank(message = "This field is required")
    @Size(max = 255, message = "Maximum 50 characters")
    private String address;
    @NotBlank(message = "This field is required")
    @Size(max = 30, message = "Maximum 30 characters")
    private String contactPhoneNo;
    @Size(max = 255, message = "Maximum 255 characters")
    private String additionalInformation;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}
