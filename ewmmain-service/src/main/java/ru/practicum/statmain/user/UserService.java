package ru.practicum.statmain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.statmain.event.Event;
import ru.practicum.statmain.event.EventMapper;
import ru.practicum.statmain.event.EventRepository;
import ru.practicum.statmain.event.dto.EventShortResponse;
import ru.practicum.statmain.exception.ConflictException;
import ru.practicum.statmain.exception.NotFoundException;
import ru.practicum.statmain.user.dto.UserCreateRequest;
import ru.practicum.statmain.user.dto.UserResponse;
import ru.practicum.statmain.user.dto.UserShortResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public UserResponse addUser(UserCreateRequest request) {
        checkEmail(request.getEmail());
        User user = UserMapper.toEntity(request);
        userRepository.save(user);
        return UserMapper.toResponse(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(user);
    }

    public List<UserResponse> getUsers(List<Long> ids, Integer from, Integer size) {
        List<User> users;
        if (ids != null && !ids.isEmpty()) {
            users = userRepository.findAllByIdIn(ids, PageRequest.of(from / size, size)).getContent();
        } else {
            users = userRepository.findAll(PageRequest.of(from / size, size)).getContent();
        }

        return UserMapper.toResponse(users);
    }

    public List<UserShortResponse> subscribe(Long userId, Long subscriberId) {
        if (userId.equals(subscriberId)) {
            throw new ConflictException("You can't subscribe to yourself");
        }

        User user = userRepository.getUserWithSubscriptionsById(userId).orElseThrow(() -> new NotFoundException("User 1 not found"));

        User subscriber = userRepository.findById(subscriberId).orElseThrow(() -> new NotFoundException("User 2 not found"));

        user.getSubscriptions().add(subscriber);
        userRepository.save(user);

        return UserMapper.toUserShortDto(user.getSubscriptions());
    }

    public List<UserShortResponse> getUserSubscriptions(Long userId) {
        return UserMapper.toUserShortDto(userRepository.getUserWithSubscriptionsById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"))
                .getSubscriptions());
    }

    public List<UserShortResponse> getUserSubscribers(Long userId) {
        return UserMapper.toUserShortDto(userRepository.getUserWithSubscribersById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"))
                .getSubscribers());
    }

    public void deleteSubscriber(Long userId, Long subscriberId) {
        User user = userRepository.getUserWithSubscribersById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        User subscriber = user.getSubscribers().stream().filter(u -> u.getId().equals(subscriberId)).findFirst()
                .orElseThrow(() -> new NotFoundException("User not found or not subscribed"));

        user.getSubscribers().remove(subscriber);

        userRepository.save(user);
    }

    public void unsubscribe(Long userId, Long subscribedOnId) {
        User user = userRepository.getUserWithSubscriptionsById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        User subscriber = user.getSubscriptions().stream().filter(u -> u.getId().equals(subscribedOnId)).findFirst()
                .orElseThrow(() -> new NotFoundException("User not found or not subscribed"));

        user.getSubscriptions().remove(subscriber);

        userRepository.save(user);
    }

    public List<EventShortResponse> getSubscriberEvent(Long userId, Long subscribedOnId) {
        userRepository.getUserWithSubscriptionsById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!userRepository.isSubscribedTo(subscribedOnId, userId)) {
            throw new ConflictException("You don't subscribed");
        }

        List<Event> events = eventRepository.getUserActualEvents(subscribedOnId, LocalDateTime.now());

        return EventMapper.toEventShortDto(events);
    }

    private void checkEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("Email already exists");
        }
    }
}
