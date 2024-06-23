package ru.practicum.statmain.event;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.practicum.statmain.category.Category;
import ru.practicum.statmain.event.enums.State;
import ru.practicum.statmain.request.Request;
import ru.practicum.statmain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "events", schema = "ewm")
@Getter
@Setter
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String annotation;

    private String description;

    @Column(name = "date")
    private LocalDateTime eventDate;

    private Boolean paid;

    @Column(name = "user_limit")
    private Integer participantLimit;

    @Column(name = "moderated")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User initiator;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_events",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "event_lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "event_lon")),
    })
    private Point point;

    @CreationTimestamp
    private LocalDateTime createdOn;

    private LocalDateTime publishedOn;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private List<Request> requests;

    @Transient
    private int views;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Point {
        private Double lat;

        private Double lon;
    }
}
