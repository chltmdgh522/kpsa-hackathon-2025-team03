package mind.domain.s3.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class S3Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "s3_audio_id") // FK
    private S3Audio s3Audio;

    private String name;

    private String imageUrl;

    private String type;

    private Long quizNumber;

    private String quizAnswer;

    @Builder
    public S3Image(String name, String imageUrl){
        this.name=name;
        this.imageUrl= imageUrl;
    }
}