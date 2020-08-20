package com.example.mlkb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @CreationTimestamp
    private Date date;
    @OneToMany(targetEntity = Account.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Set<Account> accounts;
    @OneToMany(targetEntity = Category.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Set<Category> categories;
    @OneToMany(targetEntity = Payee.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Set<Payee> payees;
    @OneToMany(targetEntity = LoginData.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Set<LoginData> logins;

    public Profile(String name, Date date) {
        this.name = name;
        this.date = date;
    }
}
