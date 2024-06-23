package ru.practicum.statmain.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statmain.event.Event;
import ru.practicum.statmain.event.EventRepository;
import ru.practicum.statmain.exception.BadRequestException;
import ru.practicum.statmain.exception.ConflictException;
import ru.practicum.statmain.exception.NotFoundException;
import ru.practicum.statmain.request.dto.RequestDto;
import ru.practicum.statmain.request.dto.RequestPatchDto;
import ru.practicum.statmain.request.dto.RequestPatchResultDto;
import ru.practicum.statmain.request.enums.Status;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;

    public List<RequestDto> getUserEventRequests(Long userId, Long eventId) {
        List<Request> requests = requestRepository.findAllByEvent_IdAndEvent_Initiator_Id(userId, eventId);
        return RequestMapper.toDto(requests);
    }

    public RequestPatchResultDto patchUserEventRequests(RequestPatchDto dto, Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found or you don't have access"));

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new BadRequestException("Requests is not to be moderated");
        }

        RequestPatchResultDto response = new RequestPatchResultDto();
        List<Request> requests = event.getRequests();
        Status status = dto.getStatus();
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> pendingRequests = new ArrayList<>();

        event.getRequests().forEach(request -> {
            if (request.getStatus().equals(Status.PENDING)) {
                pendingRequests.add(request);
            } else if (request.getStatus().equals(Status.CONFIRMED)) {
                confirmedRequests.add(request);
            }
        });

        if (status.equals(Status.CONFIRMED)) {
            if (dto.getRequestIds().size() + confirmedRequests.size() > event.getParticipantLimit()) {
                throw new ConflictException("Accepted requests count is more than participant limit");
            }
            response.setConfirmedRequests(new ArrayList<>());
        } else {
            response.setRejectedRequests(new ArrayList<>());
        }

        for (Long id : dto.getRequestIds()) {
            // Находим нужный запрос
            Request request = requests.stream().filter(r -> r.getId().equals(id)).findFirst()
                    .orElseThrow(() -> new NotFoundException("Request with id " + id + "not found"));

            // Проверяем статус
            if (!request.getStatus().equals(Status.PENDING)) {
                throw new ConflictException("Request" + id + " is not pending");
            }

            // Обновляем статус
            request.setStatus(status);

            // Обновляем к-во подтвержденных запросов или отклоненных
            if (status.equals(Status.CONFIRMED)) {
                confirmedRequests.add(request);
                response.getConfirmedRequests().add(RequestMapper.toDto(request));
            } else {
                response.getRejectedRequests().add(RequestMapper.toDto(request));
            }
        }

        if (confirmedRequests.size() == event.getParticipantLimit()) {
            for (Request request : pendingRequests) {
                // Пропускаем новые подтвержденные запросы
                if (request.getStatus().equals(Status.PENDING)) {
                    request.setStatus(Status.REJECTED);
                }
            }
        }

        // Обновляем запросы
        eventRepository.save(event);

        return response;
    }
}
