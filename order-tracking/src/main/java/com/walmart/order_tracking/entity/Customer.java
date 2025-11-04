package com.walmart.order_tracking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
/* The @Entity annotation is used in Java applications to mark a class as a
        persistent entity, meaning it represents a table in a relational database.
This annotation is part of the Java Persistence API (JPA) and tells the framework to map
    this class to a database table, with each instance of the class corresponding to a row
        in that table. */
@Entity
/* customer emails are usually unique so we are setting it as a key
 * and @UniqueConstraint will make sure that the email entries would be unique
 */
//to connect to the db the schema or db (here its orderdb) must be present in the connections
@Table(name="customers",
        uniqueConstraints = @UniqueConstraint(name = "uk_customers_email", columnNames = "email"))
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //MySQL auto increment
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Email
    @NotBlank
    @Column(nullable = false, length = 320)
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
