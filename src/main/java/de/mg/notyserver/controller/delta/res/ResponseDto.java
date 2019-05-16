package de.mg.notyserver.controller.delta.res;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ResponseDto {

    private Boolean saved;

    private List<DeltaActionDto> newDeltas;
}
