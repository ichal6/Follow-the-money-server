package com.example.mlkb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
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

    public Profile(Long id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(getId(), profile.getId()) &&
                Objects.equals(getName(), profile.getName()) &&
                Objects.equals(getDate(), profile.getDate());
    }
}
