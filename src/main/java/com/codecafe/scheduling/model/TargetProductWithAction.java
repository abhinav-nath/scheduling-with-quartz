package com.codecafe.scheduling.model;

import com.codecafe.scheduling.entity.TargetProduct;
import lombok.*;

@Getter
@Setter
public class TargetProductWithAction {

    private TargetProduct targetProduct;
    private boolean isDeleted;

}
