package liar.waitservice.wait.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchWaitRoomDto {

    @NotNull
    private String body;

    @NotNull
    private String searchType;

    public String upperSearchType() {
        return searchType.toUpperCase();
    }

}
