package ru.practicum.statmain.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationPatchRequest {

    List<Long> events;

    private Boolean pinned;

    @Size(min = 2, max = 50)
    private String title;

}
