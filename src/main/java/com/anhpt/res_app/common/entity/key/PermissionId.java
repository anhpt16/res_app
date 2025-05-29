package com.anhpt.res_app.common.entity.key;

import com.anhpt.res_app.common.enums.FeatureMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PermissionId implements Serializable {
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "feature_uri")
    private String featureUri;

    @Column(name = "feature_med")
    @Enumerated(EnumType.STRING)
    private FeatureMethod featureMethod;
}
