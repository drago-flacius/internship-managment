package com.dragomir.internship_managment.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"internships"})
@ToString(callSuper = true, exclude = {"internships"})
@Entity
@Table(name = "companies")
public class Company extends User {

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(unique = true, nullable = false, length = 9)
    private String pib;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(name = "contact_person", nullable = false, length = 200)
    private String contactPerson;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(length = 2000)
    private String description;

    @Column(length = 500)
    private String website;

    @Column(length = 500)
    private String industry;


    @JsonIgnore
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Internship> internships = new ArrayList<>();

    public void addInternship(Internship internship) {
        internships.add(internship);
        internship.setCompany(this);
    }

    public void removeInternship(Internship internship) {
        internships.remove(internship);
        internship.setCompany(null);
    }


}


