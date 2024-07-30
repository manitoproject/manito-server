package manito.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Paper paper;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String theme;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime regDateTime;

    private LocalDateTime modDateTime;

    private String font;

    private String fontColor;

    @Column(nullable = false)
    @ColumnDefault("'Y'")
    private String isPublic;

    private String anonymous;

    @Column(nullable = false)
    private Integer position;

    public void update(String content, LocalDateTime modDateTime, String font, String fontColor) {
        this.content = content;
        this.modDateTime = modDateTime;
        this.font = font;
        this.fontColor = fontColor;
    }
}
