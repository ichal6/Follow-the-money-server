package com.mlkb.ftm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Authorities {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private AuthorityType name;

    public Authorities(AuthorityType name) {
        this.name = name;
    }
}
