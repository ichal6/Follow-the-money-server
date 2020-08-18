package com.example.mlkb.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private GeneralType generalType;
    @OneToMany(targetEntity = Subcategory.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Set<Subcategory> subcategories;
}
