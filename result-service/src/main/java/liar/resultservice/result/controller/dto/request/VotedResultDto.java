package liar.resultservice.result.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VotedResultDto {

    private String liarId;
    private List<String> userIds = new ArrayList<>();
    private int cnt;
}