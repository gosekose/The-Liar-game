package liar.waitservice.wait.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor
public class SearchWaitRoomSliceDto extends SearchWaitRoomDto {

    @Range(min = 0, max = 1000)
    private int page = 0;

    @Range(min = 0, max = 20)
    private int limit = 10;

    public SearchWaitRoomSliceDto(@NotNull String body, @NotNull String searchType, int page, int limit) {
        super(body, searchType);
        this.page = page;
        this.limit = limit;
    }
}
