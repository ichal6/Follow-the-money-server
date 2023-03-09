package com.mlkb.ftm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private GeneralType generalType;
    @OneToMany(targetEntity = Subcategory.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Set<Subcategory> subcategories;
}
