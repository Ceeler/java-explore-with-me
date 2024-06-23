package ru.practicum.statmain.user;

import lombok.*;
import ru.practicum.statmain.request.Request;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "users", schema = "ewm")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "requester", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Request> requests;
}