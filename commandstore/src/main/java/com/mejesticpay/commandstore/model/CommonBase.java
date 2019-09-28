package com.mejesticpay.commandstore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.Instant;


@Getter
@Setter
@MappedSuperclass
public abstract class CommonBase  implements CommandModel
{
   private Instant createdTime;
}
