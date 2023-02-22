package com.mkst.umap.app.admin.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author yqh
 * @date 2023/2/22 17:19
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class UserBalance {
    /** 余额 */
    private BigDecimal balance;

    /** 卡券额度 */
    private BigDecimal couponBalance;
}
