package ru.practicum.statmain.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.statmain.category.Category;
import ru.practicum.statmain.category.CategoryRepository;
import ru.practicum.statmain.event.dto.*;
import ru.practicum.statmain.event.enums.Sorting;
import ru.practicum.statmain.event.enums.State;
import ru.practicum.statmain.exception.ConflictException;
import ru.practicum.statmain.exception.NotFoundException;
import ru.practicum.statmain.user.User;
import ru.practicum.statmain.user.UserRepository;
import ru.practicum.statservice.EndpointStat;
import ru.practicum.statservice.StatClient;

import java.time.LocalDateTime;
import java.util.*;
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
        event = eventRepository.save(event);

        event.setConfirmedRequests(new ArrayList<>());
        event.setRequests(new ArrayList<>()); //To avoid null list

        return EventMapper.toEventFullDto(event);
    }

    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Page<Event> eventPage = eventRepository.findByInitiator_Id(userId, PageRequest.of(from / size, size));

        List<Event> events = eventPage.getContent();
        getViews(events);

        return EventMapper.toEventShortDto(events);
    }

    public EventFullDto getUserEvent(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found or you can't see this event"));

        getViews(event);

        return EventMapper.toEventFullDto(event);
    }

    public EventFullDto patchUserEvent(EventUserPatchDto dto, Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found or you can't see this event"));

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event is already published");
        }

        EventMapper.updateEntity(event, dto);

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            event.setCategory(category);
        }

        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
            }
        }

        eventRepository.save(event);

        getViews(event);

        return EventMapper.toEventFullDto(event);
    }

    public List<EventFullDto> getAdminEvents(List<Long> users, List<Integer> categories, List<State> states,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                             Integer from, Integer size) {

        BooleanExpression resultQuery = null;

        if (users != null && !users.isEmpty()) {
            resultQuery = appendPredicate(resultQuery, QEvent.event.initiator.id.in(users));
        }

        if (categories != null && !categories.isEmpty()) {
            resultQuery = appendPredicate(resultQuery, QEvent.event.category.id.in(categories));
        }

        if (states != null && !states.isEmpty()) {
            resultQuery = appendPredicate(resultQuery, QEvent.event.state.in(states));
        }

        if (rangeStart != null) {
            resultQuery = appendPredicate(resultQuery, QEvent.event.eventDate.goe(rangeStart));
        }

        if (rangeEnd != null) {
            resultQuery = appendPredicate(resultQuery, QEvent.event.eventDate.loe(rangeEnd));
        }

        List<Event> events;
        if (resultQuery == null) {
            events = eventRepository.findAll(PageRequest.of(from / size, size)).getContent();
        } else {
            events = eventRepository.findAll(resultQuery, PageRequest.of(from / size, size))
                    .getContent();
        }

        getViews(events);

        return EventMapper.toEventFullDto(events);
    }

    public EventFullDto patchAdminEvent(Long id, EventAdminPatchDto dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found or you can't see this event"));

        if (event.getPublishedOn() != null && event.getPublishedOn().isBefore(event.getEventDate().plusHours(1))) {
            throw new ConflictException("Event can't be changed");
        }

        EventMapper.updateEntity(event, dto);

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            event.setCategory(category);
        }

        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case PUBLISH_EVENT:
                    if (!event.getState().equals(State.PENDING)) {
                        throw new ConflictException("Event can't be changed");
                    }
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    if (event.getState().equals(State.PUBLISHED)) {
                        throw new ConflictException("Event can't be changed");
                    }
                    event.setState(State.CANCELED);
                    break;
            }
        }

        eventRepository.save(event);
        getViews(event);

        return EventMapper.toEventFullDto(event);
    }

    public List<EventShortDto> getEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, Sorting sort,
                                         Integer from, Integer size, String ip) {

        BooleanExpression resultQuery = QEvent.event.state.eq(State.PUBLISHED);

        if (text != null) {
            String search = "%" + text + "%";
            resultQuery = resultQuery.and(QEvent.event.annotation.likeIgnoreCase(search)
                    .or(QEvent.event.description.likeIgnoreCase(search)));
        }

        if (categories != null && !categories.isEmpty()) {
            resultQuery = resultQuery.and(QEvent.event.category.id.in(categories));
        }

        if (paid != null) {
            resultQuery = resultQuery.and(QEvent.event.paid.eq(paid));
        }

        if (rangeStart != null || rangeEnd != null) {
            if (rangeStart != null) {
                resultQuery = resultQuery.and(QEvent.event.eventDate.goe(rangeStart));
            }
            if (rangeEnd != null) {
                resultQuery = resultQuery.and(QEvent.event.eventDate.loe(rangeEnd));
            }
        } else {
            resultQuery = resultQuery.and(QEvent.event.eventDate.goe(LocalDateTime.now()));
        }

        if (onlyAvailable) {
            resultQuery = resultQuery.and(QEvent.event.confirmedRequests.size().loe(QEvent.event.participantLimit));
        }

        List<Event> events = null;

        if (sort != null) {
            switch (sort) {
                case EVENT_DATE:
                    Pageable page = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "eventDate"));
                    events = eventRepository.findAll(resultQuery, page).getContent();
                    break;
                case VIEWS:
                    List<Long> ids = getTopViewsIds(rangeStart, rangeEnd, from, size);
                    resultQuery = resultQuery.and(QEvent.event.id.in(ids));
                    events = eventRepository.findAll(resultQuery, PageRequest.of(from / size, size)).getContent();
                    getViews(events);
                    events.sort(Comparator.comparing(Event::getViews).reversed());
                    return EventMapper.toEventShortDto(events);
            }
        } else {
            events = eventRepository.findAll(resultQuery, PageRequest.of(from / size, size)).getContent();
        }

        getViews(events);

        for (Event event : events) {
            addView(event, ip);
        }

        return EventMapper.toEventShortDto(events);
    }

    public EventFullDto getEvent(Long id, String ip) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Event not found");
        }

        getViews(event);

        addView(event, ip);

        return EventMapper.toEventFullDto(event);
    }

    private BooleanExpression appendPredicate(BooleanExpression resultQuery, BooleanExpression predicate) {
        if (resultQuery == null) {
            return predicate;
        } else {
            return resultQuery.and(predicate);
        }
    }

    private void getViews(Event... events) {
        if (events.length == 0) {
            return;
        }

        Map<String, Event> uris = Arrays.stream(events).collect(Collectors.toMap((event -> EVENTS_PATH + event.getId()), event -> event));
        LocalDateTime now = LocalDateTime.now();

        List<EndpointStat> stats = statClient.getAll(now.minusYears(1), now.plusYears(1), true, uris.keySet().toArray(new String[0]));

        for (EndpointStat stat : stats) {
            uris.get(stat.getUri()).setViews(stat.getHits());
        }
    }

    private void getViews(List<Event> events) {
        if (events.size() == 0) {
            return;
        }

        Map<String, Event> uris = events.stream().collect(Collectors.toMap((event -> EVENTS_PATH + event.getId()), event -> event));
        LocalDateTime now = LocalDateTime.now();

        List<EndpointStat> stats = statClient.getAll(now.minusYears(1), now.plusYears(1), true, uris.keySet().toArray(new String[0]));

        for (EndpointStat stat : stats) {
            uris.get(stat.getUri()).setViews(stat.getHits());
        }
    }

    private List<Long> getTopViewsIds(LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        LocalDateTime now = LocalDateTime.now();
        List<EndpointStat> stats = statClient.getAll(
                rangeStart == null ? now.minusYears(1) : rangeStart,
                rangeEnd == null ? now.plusYears(1) : rangeEnd,
                true);
        stats.sort(Comparator.comparing(EndpointStat::getHits).reversed());
        List<Long> ids = new ArrayList<>();

        for (int i = from; i < size; i++) {
            String[] uri = stats.get(i).getUri().split("/");
            ids.add(Long.parseLong(uri[uri.length - 1]));
        }

        return ids;
    }

    private void addView(Event event, String ip) {
        statClient.addHit(APP_NAME, EVENTS_PATH + event.getId(), ip);
    }

}
