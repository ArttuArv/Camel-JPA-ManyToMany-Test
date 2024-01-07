package org.unioulu.jpa.manytomany.example.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Entity
@Table(name="many_to_many_example_persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int age;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "persons", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pet> pets;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinTable(
        name = "many_to_many_example_persons_person_hobby",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "hobby_id")
    )
    private Set<Hobby> hobbies = new HashSet<>();

    public void setPets(List<Pet> pets) {
        this.pets = pets;
        if (pets != null) {
            for (Pet pet : pets) {
                pet.setPersons(this);
            }
        }
    }

   public void addHobby(Hobby hobby) {
        this.hobbies.add(hobby);
        hobby.getPersons().add(this);
    }
    
}
