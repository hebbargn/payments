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

    // Unique and unambiguous identification of a financial institution sending message.
    private String from_id;
    // // Unique and unambiguous identification of a financial institution receiving message.
    private String to_id;
    // Business Message Identifier. Assigned by the sender of the message
    private String biz_msg_idr;
    // Message Identifier that defines the Business Message. For example: pacs.008.001.06
    private String message_type;
    // Date and Time recrod was created by clearing. value comes from application header.
    private LocalDateTime clearing_creation_dttm;
    // Date and Time our system created record.
//    @CreationTimestamp
    private LocalDateTime creation_time;
    // Message Received from clearing
    private String message_text;

    // Indicates whether the message is a Copy, a Duplicate, or a copy of a duplicate of a previously sent ISO 20022 Message.
    private String copy_duplicate;

}
