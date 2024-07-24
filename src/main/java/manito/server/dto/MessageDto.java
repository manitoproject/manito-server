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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(Include.NON_NULL)
public class MessageDto {
    private Long id;

    private Long paperId;

    private Long userId;

    private String theme;

    private String content;

    private LocalDateTime regDateTime;

    private LocalDateTime modDateTime;

    private String font;

    private String fontColor;

    private String isPublic;

    private Integer position;
}
