package com.mejesticpay.rtpgateway.mysql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="matchrecord")
public class MatchRecord
{
    @Id
    private Long id;

    private String message_id;
}
