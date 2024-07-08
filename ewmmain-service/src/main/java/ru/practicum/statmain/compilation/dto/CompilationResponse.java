package ru.practicum.statmain.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.statmain.event.dto.EventShortResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationResponse {

    private Long id;

    private List<EventShortResponse> events;

    private boolean pinned;

    private String title;

}
