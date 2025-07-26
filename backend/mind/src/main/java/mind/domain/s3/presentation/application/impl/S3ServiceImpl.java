package game3.domain.s3.presentation.application.impl;

import game3.domain.s3.domain.entity.S3Audio;
import game3.domain.s3.domain.entity.S3Image;
import game3.domain.s3.domain.repository.S3AudioRepository;
import game3.domain.s3.domain.repository.S3ImageRepository;
import game3.domain.s3.presentation.application.S3Service;
import game3.global.infra.exception.error.MindException;
import game3.global.infra.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class S3ServiceImpl implements S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName; // 버킷 이름 설정

    @Value("${cloud.aws.region.static}")
    private String bucketRegion; // 지역 설정
    private static final String TEMP_DIR = "/tmp";


    private final S3Client s3Client; // AmazonS3Client 대신 S3Client 사용

    private final S3AudioRepository s3AudioRepository;
    private final S3ImageRepository s3ImageRepository;

    @Override
    public String storeImage(MultipartFile multipartFile, String name) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        log.info("크하하하하하");

        // 원본 파일 이름 가져오기
        String originalFilename = multipartFile.getOriginalFilename(); // 예: image.png

        // 저장할 파일 이름 생성
        String storeFileName = createStoreFileName(originalFilename); // 고유 파일 이름 생성

        // S3에 파일 업로드 (AWS SDK v2 방식)
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(storeFileName)
                .contentType(multipartFile.getContentType())
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

        // 업로드된 파일의 URL 생성
        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName,               // S3 버킷 이름
                bucketRegion,             // S3 리전
                storeFileName             // 저장된 파일 이름
        );

        S3Image s3Image = S3Image.builder()
                .name(name)
                .imageUrl(fileUrl)
                .build();

        s3ImageRepository.save(s3Image);

        return fileUrl;
    }


    @Override
    public String storeAudio(MultipartFile file, String name) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        String key = name + ".mp3"; // 또는 .mp3로 저장해도 무방

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType()) // 예: audio/wav
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(
                file.getInputStream(),
                file.getSize()
        ));

        String audioUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, bucketRegion, key);
        S3Audio s3Audio = S3Audio.builder()
                .name(name)
                .audioUrl(audioUrl)
                .build();

        s3AudioRepository.save(s3Audio);


        return audioUrl;
    }


    @Override
    public String getAudio(String name) {

        List<S3Audio> byS3Audio = s3AudioRepository.findByName(name);
        if (byS3Audio.size() != 1) {
            throw new MindException(ErrorCode.JWT_ERROR_TOKEN);
        }
        S3Audio s3Audio = byS3Audio.get(0);


        return s3Audio.getAudioUrl();
    }


    private static String createStoreFileName(String originalFilename) {
        String ext = extractedExt(originalFilename);
        // 서버에 저장하는 파일명
        String uuid = UUID.randomUUID().toString();
        //asd3f143as5d4f5.png

        String storeFileName = uuid + "." + ext;

        return storeFileName;
    }

    private static String extractedExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1); //png반환
        return ext;
    }
}
