package ru.practicum.statmain.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.statmain.category.Category;
import ru.practicum.statmain.category.CategoryRepository;
import ru.practicum.statmain.event.dto.EventFullDto;
import ru.practicum.statmain.event.dto.EventPatchDto;
import ru.practicum.statmain.event.dto.EventPostDto;
import ru.practicum.statmain.event.dto.EventShortDto;
import ru.practicum.statmain.event.enums.State;
import ru.practicum.statmain.exception.NotFoundException;
import ru.practicum.statmain.user.User;
import ru.practicum.statmain.user.UserRepository;
import ru.practicum.statservice.client.StatClient;
import ru.practicum.statservice.dto.EndpointStat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private static final String EVENTS_PATH = "/events/";

    private static final String APP_NAME = "ewm-main-service";

    @Autowired
    public EventService(RestTemplateBuilder restTemplate, @Value("${stat-server.url}") String statServerUrl,
                        EventRepository eventRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.statClient = new StatClient(statServerUrl, restTemplate);
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    private final StatClient statClient;

    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    public EventFullDto addEvent(EventPostDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Event event = EventMapper.toEntity(dto, category, user);
        eventRepository.save(event);

        event.setRequests(new ArrayList<>()); //To avoid null list

        return EventMapper.toEventFullDto(event);
    }

    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Page<Event> eventPage = eventRepository.findByInitiator_Id(userId, PageRequest.of(from / size, size));

        List<Event> events = eventPage.getContent();
        getViews(events.toArray(new Event[0]));

        return EventMapper.toEventShortDto(events);
    }

    public EventFullDto getUserEvent(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found or you can't see this event"));

        getViews(event);

        return EventMapper.toEventFullDto(event);
    }

    public EventFullDto patchUserEvent(EventPatchDto dto, Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found or you can't see this event"));

        EventMapper.updateEntity(event, dto);

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            event.setCategory(category);
        }

        // TODO Понять за что отвечает
        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case CANCEL_REVIEW:
                    break;
                case SEND_TO_REVIEW:
                    break;
            }
        }

        eventRepository.save(event);
        getViews(event);

        return EventMapper.toEventFullDto(event);
    }

    public List<EventFullDto> getAdminEvents(List<Integer> users, List<Integer> categories, List<State> states,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                             Integer from, Integer size) {
        return null;
    }

    private void getViews(Event... events) {
        if (events.length == 0) {
            return;
        }

        Map<String, Event> uris = Arrays.stream(events).collect(Collectors.toMap((event -> EVENTS_PATH + event.getId()), event -> event));
        LocalDateTime now = LocalDateTime.now();

        List<EndpointStat> stats = statClient.getAll(now.minusYears(1), now, false, uris.keySet().toArray(new String[0]));

        for (EndpointStat stat : stats) {
            uris.get(stat.getUri()).setViews(stat.getHits());
        }
    }

    private void addView(Event event, String ip) {
        statClient.addHit(APP_NAME, EVENTS_PATH + event.getId(), ip);
    }

}
