package org.ebndrnk.springbrauser.service.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, byte[]> redisTemplate;

    public void savePhoto(Long userId, byte[] photoData) {
        redisTemplate.opsForValue().set(userId.toString(), photoData);
    }

    public byte[] getPhoto(Long userId) {
        return redisTemplate.opsForValue().get(userId.toString());
    }

    public void deletePhoto(Long userId) {
        redisTemplate.delete(userId.toString());
    }
}
