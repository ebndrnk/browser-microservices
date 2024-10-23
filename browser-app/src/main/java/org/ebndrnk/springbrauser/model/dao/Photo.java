package org.ebndrnk.springbrauser.model.dao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "t_photo")
@Data
@NoArgsConstructor
public class Photo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "url")
    private String url;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Photo(String url, User user) {
        this.url = url;
        this.user = user;
    }
}
