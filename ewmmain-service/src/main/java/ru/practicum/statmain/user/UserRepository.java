package ru.practicum.statmain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByIdIn(List<Long> ids, Pageable page);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.subscribers " +
            "WHERE u.id = :id")
    Optional<User> getUserWithSubscribersById(Long id);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.subscriptions " +
            "WHERE u.id = :id")
    Optional<User> getUserWithSubscriptionsById(Long id);

    @Query(value = "SELECT COUNT(s) > 0 FROM ewm.subscriptions s WHERE s.user_id = :id AND s.subscriber_id = :subscriberId",
            nativeQuery = true)
    Boolean isSubscribedTo(Long id, Long subscriberId);
}
