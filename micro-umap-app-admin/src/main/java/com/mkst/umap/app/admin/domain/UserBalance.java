package com.mkst.umap.app.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author yqh
 * @date 2023/2/22 17:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UserBalance {
    /** 余额 */
    private BigDecimal balance;

    /** 卡券额度 */
    private BigDecimal couponBalance;

    /** 奖励券数量 */
    private int awardTicketNum;
}
