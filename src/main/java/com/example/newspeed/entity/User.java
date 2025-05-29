package com.example.newspeed.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends TimeStampEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Email
    @Column(unique = true)
    private String email;
    private String password;
    private String userName;
    private String intro;
    private String profileImageUrl;

    @OneToMany(mappedBy = "users")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    public User(String email, String password, String userName, String intro, String profileImageUrl){
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.intro = intro;
        this.profileImageUrl = profileImageUrl;
    }
}
