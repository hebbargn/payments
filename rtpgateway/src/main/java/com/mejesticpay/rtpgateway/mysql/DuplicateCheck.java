package com.mejesticpay.rtpgateway.mysql;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "duplicatecheck", uniqueConstraints={@UniqueConstraint(columnNames ={"instruction_id","key1"})})
public class DuplicateCheck
{
    @Id
    @GeneratedValue
    private Long id;

    // CdtTrfTxInf/PmtId/InstrId
    @NotNull
    private String instruction_id;

    private String key1;

}
