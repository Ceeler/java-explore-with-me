package ru.practicum.statmain.compilation;

import ru.practicum.statmain.compilation.dto.CompilationDto;
import ru.practicum.statmain.event.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {

    private CompilationMapper() {
    }

    public static CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getName())
                .pinned(compilation.getPinned())
                .events(EventMapper.toEventShortDto(compilation.getEvents())) //TODO views
                .build();
    }

    public static List<CompilationDto> toDto(List<Compilation> compilation) {
        return compilation.stream().map(CompilationMapper::toDto).collect(Collectors.toList());
    }

    public static Compilation toEntity(CompilationDto dto) {
        return null;
    }

}
