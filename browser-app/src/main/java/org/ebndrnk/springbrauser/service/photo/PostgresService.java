package org.ebndrnk.springbrauser.service.photo;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.springbrauser.model.dao.Photo;
import org.ebndrnk.springbrauser.reposiroty.photo.PhotoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostgresService {
    private final PhotoRepository photoRepository;

    public void save(Photo photo) {
        photoRepository.save(photo);
    }

    public void deleteByUserId(Long userId) {
        photoRepository.deleteByUserId(userId);
    }
}
