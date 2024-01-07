package org.unioulu.jpa.manytomany.example.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "many_to_many_example_hobbies")
public class Hobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude 
    @ManyToMany(mappedBy = "hobbies")
    private Set<Person> persons = new HashSet<>();

    public void addPerson(Person person) {
        this.persons.add(person);
        person.getHobbies().add(this);
    }

}
