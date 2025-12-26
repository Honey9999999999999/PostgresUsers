package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "roles")

public class Role {
    public static final Role DEFAULT_ROLE = new Role(3L, "USER");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "role", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> users;

    public Role(){}
    private Role(Long id, String name){
        this.id = id;
        this.name = name;
    }
}
