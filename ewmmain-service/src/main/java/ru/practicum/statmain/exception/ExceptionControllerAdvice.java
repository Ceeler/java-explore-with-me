package ru.practicum.statmain.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleBadRequestException(final RuntimeException e) {
        log.warn("500 {}", e.getMessage());
        return ExceptionDto.builder()
                .message(e.getMessage())
                .status("BAD_REQUEST")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleConflictException(final RuntimeException e) {
        log.warn("500 {}", e.getMessage());
        return ExceptionDto.builder()
                .message(e.getMessage())
                .status("CONFLICT")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleNotFoundException(final RuntimeException e) {
        log.warn("500 {}", e.getMessage());
        return ExceptionDto.builder()
                .message(e.getMessage())
                .status("NOT_FOUND")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto onIllegalArgumentException(final RuntimeException e) {
        log.warn("400 ", e);
        return ExceptionDto.builder()
                .message(e.getMessage())
                .status("BAD_REQUEST")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto handleThrowable(final RuntimeException e) {
        log.warn("500 ", e);
        return ExceptionDto.builder()
                .message(e.getMessage())
                .status("INTERNAL_SERVER_ERROR")
                .timestamp(LocalDateTime.now())
                .build();
    }


}
