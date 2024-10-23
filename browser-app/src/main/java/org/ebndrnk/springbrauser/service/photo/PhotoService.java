package org.ebndrnk.springbrauser.service.photo;

import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.springbrauser.model.dao.Photo;
import org.ebndrnk.springbrauser.model.dao.User;
import org.ebndrnk.springbrauser.reposiroty.photo.PhotoRepository;
import org.ebndrnk.springbrauser.service.user_actions.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final AmazonS3Service amazonS3Service;
    private final RedisService redisService;
    private final PhotoRepository photoRepository;
    private final UserService userService;
    private final PostgresService postgresService;

    @Transactional
    public String addPhoto(MultipartFile multipartFile) throws IOException {
        User currentUser = userService.findUserById(userService.getCurrentUserId());

        if(getPhoto() != null){
            deletePhoto();
        }

        try {
            // Загрузка в S3
            String photoUrl = amazonS3Service.uploadFile(multipartFile);

            // Сохранение URL в PostgreSQL
            Photo photo = new Photo(photoUrl, currentUser);
            postgresService.save(photo);

            // Сохранение изображения в Redis
            byte[] photoData = IOUtils.toByteArray(multipartFile.getInputStream());
            redisService.savePhoto(currentUser.getId(), photoData);

            return photoUrl;
        } catch (Exception e) {
            try {
                deletePhoto();
                addPhoto(multipartFile);
            } catch (Exception ex){
                throw new RuntimeException("Failed to add photo", ex);
            }
        }

        throw new RuntimeException("Exception with adding photo");
    }

    public byte[] getPhoto() {
        User currentUser = userService.findUserById(userService.getCurrentUserId());
        Long userId = currentUser.getId();

        // Сначала проверяем Redis
        byte[] photoData = redisService.getPhoto(userId);
        System.out.println("cheking redis");
        if (photoData != null) {
            System.out.println("photo came from redis");
            return photoData;
        }

        // Если нет в Redis, ищем в PostgreSQL
        Optional<Photo> optionalPhoto = photoRepository.findByUser(currentUser);

        if (optionalPhoto.isPresent()) {
            Photo photo = optionalPhoto.get();

            byte[] dataFromS3 = null;
            try {
                dataFromS3 = amazonS3Service.downloadFile(photo.getUrl());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Сохранение в Redis для будущего использования
            redisService.savePhoto(userId, dataFromS3);

            return dataFromS3;
        }

        return null;
    }

    @Transactional
    public void deletePhoto() {
        User currentUser = userService.findUserById(userService.getCurrentUserId());
        Long userId = currentUser.getId();
        String url = currentUser.getPhoto().getUrl();


        System.out.println(userId);

        try {
            redisService.deletePhoto(userId);
            // Удаление из S3
            amazonS3Service.deleteFile(url);


            // Удаление из базы данных
            postgresService.deleteByUserId(userId);
            System.out.println("deleted file from db and s3");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }



    }

}
