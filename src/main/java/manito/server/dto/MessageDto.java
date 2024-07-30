package manito.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import manito.server.entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MessageDto {
    private Long id;

    private Long paperId;

    private User user;

    private String theme;

    private String content;

    private LocalDateTime regDateTime;

    @JsonInclude(Include.NON_NULL)
    private LocalDateTime modDateTime;

    private String font;

    private String fontColor;

    private String isPublic;

    private String anonymous;

    private Integer position;
}
