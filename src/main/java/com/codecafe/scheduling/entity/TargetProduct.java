package com.codecafe.scheduling.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "target_products")
public class TargetProduct {

    @Id
    private int id;

    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
