package com.example.dishpatch.infra.db.statistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
@Embeddable
public class OrderStatId {

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private LocalDate date;
}
