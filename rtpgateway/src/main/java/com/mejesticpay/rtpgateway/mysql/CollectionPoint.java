package com.mejesticpay.rtpgateway.mysql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="collectionpoint")
public class CollectionPoint
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String from_id;
    private String to_id;
//    private String biz_msg_idr;
    private String message_type;
    // Date and Time recrod was created by clearing. value comes from application header.
    private LocalDateTime clearing_creation_dttm;
    // Date and Time our system created record.
//    @CreationTimestamp
    private LocalDateTime creation_time;
    // Message Received from clearing
    private String message_text;

}
