package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "contents")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_seq")
    @SequenceGenerator(name = "content_seq", sequenceName = "content_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "created_at", updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public void addUser(User user){
        this.user = user;
    }
    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.addContent(this);
    }
}
