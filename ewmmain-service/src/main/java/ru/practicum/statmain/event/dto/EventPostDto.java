package ru.practicum.statmain.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.statmain.event.point.PointDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventPostDto {

    @Size(min = 3, max = 120)
    @NotBlank
    private String title;

    @Size(min = 20, max = 7000)
    @NotBlank
    private String description;

    @Size(min = 20, max = 2000)
    @NotBlank
    private String annotation;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private PointDto location;

    @NotNull
    private Integer category;


}
