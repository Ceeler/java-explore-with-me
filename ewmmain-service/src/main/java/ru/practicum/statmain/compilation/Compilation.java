package ru.practicum.statmain.compilation;

import lombok.*;
import ru.practicum.statmain.event.Event;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "compilations", schema = "ewm")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean pinned;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "compilations_events",
            schema = "ewm",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Event> events;

}
