package br.com.movieflix.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "streaming")
@Entity
public class Streaming implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;
}
