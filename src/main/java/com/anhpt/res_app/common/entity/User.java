package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.enums.status.UserStatus;
import com.anhpt.res_app.common.enums.type.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(name = "type_id")
    private String typeId;

    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    @Column(name = "phone_verified")
    private Boolean phoneVerified = false;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRoles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserVoucher> userVouchers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Media> medias = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Collection> collections = new ArrayList<>();
}
