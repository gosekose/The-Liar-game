package liar.waitservice.wait.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EnumValueDto {
    private String key;
    private String value;
    private String comment;
}