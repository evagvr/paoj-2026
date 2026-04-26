package com.pao.project.bankingApp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Customer implements Comparable<Customer>{
    private final String userId;
    private final String cnp;
    private final String firstName;
    private final String lastName;
    private String email;
    private String phone;
    private final LocalDate dateOfBirth;
    private final LocalDateTime createdAt;
    private final CreditHistory creditHistory;
    private final List<Insurance> insurances;
    public Customer(String cnp, String firstName, String lastName, LocalDate dateOfBirth, String email, String phone){
        this.userId = java.util.UUID.randomUUID().toString();
        this.cnp = cnp;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = LocalDateTime.now();
        this.email = email;
        this.phone = phone;
        this.insurances = new ArrayList<>();
        this.creditHistory = new CreditHistory();
    }

    public String getUserId() {
        return userId;
    }

    public String getCnp() {
        return cnp;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getFullName(){
        return lastName + " " +firstName;
    }
    public String getEmail(){ return email;}
    public String getPhone(){ return phone;}
    public LocalDate getDateOfBirth(){ return dateOfBirth;}
    public LocalDateTime getCreatedAt(){ return createdAt;}
    public CreditHistory getCreditHistory(){return creditHistory;}
    public List<Insurance> getInsurances(){
        return insurances;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void addInsurance(Insurance insurance) {
        insurances.add(insurance);
    }
    @Override
    public int compareTo(Customer other){
        return Comparator.comparing(Customer::getLastName)
                .thenComparing(Customer::getFirstName)
                .compare(this, other);
    }
    @Override
    public String toString() {
        return "Customer{" +
                "userId='" + userId + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", cnp='" + cnp + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return cnp.equals(customer.cnp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnp);
    }
}
