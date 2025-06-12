package com.anhpt.res_app.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_media")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "origin_name")
    private String originName;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "file_size")
    private Long fileSize;

    private Integer width;
    private Integer height;
    private Long duration;
    private String url;
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "media")
    private List<DeskMedia> deskMedias = new ArrayList<>();

    @OneToMany(mappedBy = "media")
    private List<DishMedia> dishMedias = new ArrayList<>();

    @OneToOne(mappedBy = "media", fetch = FetchType.LAZY)
    private Combo combo;

    @OneToOne(mappedBy = "media")
    private Collection collection;

    @OneToMany(mappedBy = "media")
    private List<Post> posts = new ArrayList<>();
}
