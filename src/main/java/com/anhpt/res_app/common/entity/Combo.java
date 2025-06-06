package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.enums.status.ComboStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_combo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Combo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "media_id")
    private Long mediaId;

    private String introduce;
    private String description;

    @Enumerated(EnumType.STRING)
    private ComboStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "combo")
    private List<ComboVersion> comboVersions;
}
