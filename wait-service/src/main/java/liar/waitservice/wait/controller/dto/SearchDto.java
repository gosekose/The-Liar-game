package liar.waitservice.wait.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto<T>{

    @NotNull
    private T body;

    @NotNull
    private String searchType;

    public String upperSearchType() {
        return searchType.toUpperCase();
    }

}
