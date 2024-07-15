package manito.server.entity;

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

    private String theme;

    private String content;

    private LocalDateTime regDateTime;

    private LocalDateTime modDateTime;

    private String font;

    private String fontColor;

    @ColumnDefault("'Y'")
    private String isPublic;
}
