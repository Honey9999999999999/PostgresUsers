package org.example.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Message {
    @Embeddable
    @Getter @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class MessageId{
        private Long senderId;
        private Long recipientId;
        private LocalDateTime createdAt;

        public MessageId(Long senderId, Long recipientId){
            this.senderId = senderId;
            this.recipientId = recipientId;
            createdAt = LocalDateTime.now();
        }
    }

    @EmbeddedId
    private MessageId id = new MessageId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("senderId")
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipientId")
    @JoinColumn(name = "recipient_id")
    private User recipient;

//    @MapsId("createdAt")
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;

    @Column(name = "text")
    private String text;

    public Message(User sender, User recipient){
        id = new MessageId(sender.getId(), recipient.getId());
        this.sender = sender;
        this.recipient = recipient;
    }
}
