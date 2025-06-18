package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.enums.type.SettingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_setting")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "setting_key")
    private String settingKey;

    @Column(name = "setting_value")
    private String settingValue;

    @Column(name = "setting_type")
    @Enumerated(EnumType.STRING)
    private SettingType settingType;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
