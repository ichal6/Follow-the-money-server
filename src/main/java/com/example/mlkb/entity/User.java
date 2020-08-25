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
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    @CreationTimestamp
    private Date date;
    @OneToMany(targetEntity = Account.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<Account> accounts;
    @OneToMany(targetEntity = Category.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<Category> categories;
    @OneToMany(targetEntity = Payee.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<Payee> payees;

    public User(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public User(Long id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getDate(), user.getDate());
    }
}
