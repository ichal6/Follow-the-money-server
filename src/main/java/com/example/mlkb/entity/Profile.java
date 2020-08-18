package com.example.mlkb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @CreatedDate
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
}
