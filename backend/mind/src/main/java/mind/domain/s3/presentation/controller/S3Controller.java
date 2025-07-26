package mind.domain.s3.presentation.controller;

import mind.domain.s3.presentation.application.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
@Slf4j
public class S3Controller {
    private final S3Service s3Service;


    @PostMapping("/image")
    public String s3Image(@RequestPart("image") MultipartFile image,
                          @RequestPart("name") String name) throws IOException {

        return s3Service.storeImage(image, name);
    }

    @PostMapping("/audio")
    public String convertAndUpload(  @RequestPart("file") MultipartFile file,
                                     @RequestPart("name") String name) throws IOException {
        return s3Service.storeAudio(file, name);
    }

    @GetMapping("/audio")
    public String getFile(@RequestParam("name") String name) {
        return s3Service.getAudio(name);
    }

}
