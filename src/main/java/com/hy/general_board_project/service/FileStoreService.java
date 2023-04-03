package com.hy.general_board_project.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.hy.general_board_project.domain.profileImage.ProfileImage;
import com.hy.general_board_project.domain.profileImage.ProfileImageRepository;
import com.hy.general_board_project.domain.dto.profileImage.ProfileImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileStoreService {

    private static final String LOCAL_STORAGE_DIRECTORY = "local";
    private static final String MAIN_STORAGE_DIRECTORY = "main";

    private final ProfileImageRepository profileImageRepository;

    @Value("${application.bucket}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public ProfileImageDto storeFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            log.info("multipart is empty.");
            return null;
        }

        File fileObj = convertMultiPartFileToFile(multipartFile);
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = MAIN_STORAGE_DIRECTORY + "/" + createStoreFileName(originalFilename);
        s3Client.putObject(new PutObjectRequest(bucketName, storeFileName, fileObj));
        fileObj.delete();

        log.info("multipartFile uploaded : {}", storeFileName);

        return new ProfileImageDto(originalFilename, storeFileName);
    }

    @Transactional
    public ProfileImage save(ProfileImageDto profileImageDto) {
        return profileImageRepository.save(profileImageDto.toEntity());
    }

    public void deleteStoredFile(Long profileImageId) {
        ProfileImage profileImage = profileImageRepository.findById(profileImageId).get();
        String storeFileName = profileImage.getStoreName();
        s3Client.deleteObject(bucketName, storeFileName);
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public byte[] downloadFile(String fileName) throws IOException {
        fileName = MAIN_STORAGE_DIRECTORY + "/" + fileName;

        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
