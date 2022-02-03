package com.codecafe.scheduling.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "source_products")
public class SourceProduct {

    @Id
    private int id;

    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
