package com.anhpt.res_app.common.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class StartTimeDurationId implements Serializable {
    @Column(name = "start_time_id")
    private Long startTimeId;

    @Column(name = "duration_id")
    private Long durationId;

}
