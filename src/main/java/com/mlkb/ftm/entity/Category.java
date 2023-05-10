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
    @ManyToOne(targetEntity = Category.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category parentCategory;
    private Boolean isEnabled = true;
    @OneToMany(targetEntity = Category.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Set<Category> subcategories;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User owner;

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }
}
