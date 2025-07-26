package mind.domain.s3.presentation.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {
    String storeImage(MultipartFile multipartFile, String name) throws IOException;
    String storeAudio(MultipartFile file, String name) throws IOException;

    String getAudio(String filename);



}
