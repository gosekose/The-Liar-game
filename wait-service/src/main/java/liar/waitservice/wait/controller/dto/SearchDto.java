package liar.waitservice.wait.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto<T>{

    @NotNull
    private T request;

    @NotNull
    private String searchType;

    public String upperSearchType() {
        return searchType.toUpperCase();
    }

}
