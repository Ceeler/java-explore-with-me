package ru.practicum.statmain.event.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PointDto {

    @NotNull
    private Double lat;

    @NotNull
    private Double lon;
}
