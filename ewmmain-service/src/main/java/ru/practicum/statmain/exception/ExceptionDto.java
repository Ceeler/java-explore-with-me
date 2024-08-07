package ru.practicum.statmain.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDto {

    private String errors;

    private String message;

    private String reason;

    private String status;

    private LocalDateTime timestamp;
}
