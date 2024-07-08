package ru.practicum.statmain.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.statmain.category.dto.CategoryResponse;
import ru.practicum.statmain.event.enums.State;
import ru.practicum.statmain.event.point.PointResponseRequest;
import ru.practicum.statmain.user.dto.UserShortResponse;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EventFullResponse {

    private Long id;

    private String title;

    private String annotation;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private State state;

    private CategoryResponse category;

    private UserShortResponse initiator;

    private Integer confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdOn;

    private PointResponseRequest location;

    private LocalDateTime publishedOn;

    private Integer views;
}
