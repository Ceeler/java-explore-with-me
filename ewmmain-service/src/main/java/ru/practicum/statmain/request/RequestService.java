package ru.practicum.statmain.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statmain.event.Event;
import ru.practicum.statmain.event.EventRepository;
import ru.practicum.statmain.event.enums.State;
import ru.practicum.statmain.exception.ConflictException;
import ru.practicum.statmain.exception.NotFoundException;
import ru.practicum.statmain.request.dto.RequestDto;
import ru.practicum.statmain.request.dto.RequestPatchDto;
import ru.practicum.statmain.request.dto.RequestPatchResultDto;
import ru.practicum.statmain.request.enums.Status;
import ru.practicum.statmain.user.User;
import ru.practicum.statmain.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    public List<RequestDto> getUserEventRequests(Long userId, Long eventId) {
        List<Request> requests = requestRepository.findAllByEvent_IdAndEvent_Initiator_Id(eventId, userId);
        return RequestMapper.toDto(requests);
    }

    public RequestPatchResultDto patchUserEventRequests(RequestPatchDto dto, Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found or you don't have access"));

        RequestPatchResultDto response = new RequestPatchResultDto();
        List<Request> requests = event.getRequests();

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            response.setConfirmedRequests(RequestMapper.toDto(requests));
            return response;
        }

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

    public List<RequestDto> getRequests(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        List<Request> requests = requestRepository.findAllByRequester_Id(userId);

        return RequestMapper.toDto(requests);
    }

    public RequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Event event = eventRepository.findByIdWithRequests(eventId).orElseThrow(() -> new NotFoundException("Event not found"));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("You can't create request for your own event");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event is not published");
        }

        if (requestRepository.findByRequester_IdAndEvent_Id(userId, eventId).isPresent()) {
            throw new ConflictException("You already have a request for this event");
        }

        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests().size() >= event.getParticipantLimit()) {
            throw new ConflictException("Event is full");
        }

        Request request = RequestMapper.toEntity(user, event);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED);
        }

        request = requestRepository.save(request);

        return RequestMapper.toDto(request);
    }

    public RequestDto cancelRequest(Long userId, Long id) {
        Request request = requestRepository.findByRequester_IdAndId(userId, id)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        request.setStatus(Status.CANCELED);
        requestRepository.save(request);

        return RequestMapper.toDto(request);
    }

}
