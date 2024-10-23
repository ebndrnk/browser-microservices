package org.ebndrnk.springbrauser.model.dao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_request")
@Data
@NoArgsConstructor
public class SearchRequest {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request")
    private String Request;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
