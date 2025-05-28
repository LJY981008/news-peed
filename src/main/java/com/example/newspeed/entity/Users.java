package com.example.newspeed.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Users extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String password;
    private String userName;
    private String intro;
    private String profileImageUrl;

    public Users(String email, String password, String userName, String intro, String profileImageUrl){
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.intro = intro;
        this.profileImageUrl = profileImageUrl;
    }
}
