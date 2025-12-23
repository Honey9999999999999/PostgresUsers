package org.example.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Friendship {
    @Embeddable
    @Getter @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class FriendshipId implements Serializable {
        private Long userId;
        private Long friendId;

        public FriendshipId(Long userId, Long friendId){
            this.userId = userId;
            this.friendId = friendId;
        }
    }

    @EmbeddedId
    private FriendshipId id = new FriendshipId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("friendId")
    @JoinColumn(name = "friend_id")
    private User friend;

    @Column(name = "status")
    private String status = "PENDING";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Friendship(User user, User friend){
        id = new FriendshipId(user.getId(), friend.getId());
        this.user = user;
        this.friend = friend;
    }
}
