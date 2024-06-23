package ru.practicum.statmain.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.statmain.event.enums.StateAction;
import ru.practicum.statmain.event.point.PointDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventPatchDto {

    private String title;

    private String annotation;

    private String description;

    private Integer categoryId;

    private LocalDateTime eventDate;

    private PointDto location;

    private Boolean paid;

    private StateAction stateAction;

    private Integer participantLimit;

    private Boolean requestModeration;
}
