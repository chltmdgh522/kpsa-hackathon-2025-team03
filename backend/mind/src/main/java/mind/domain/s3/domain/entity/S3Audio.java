package mind.domain.s3.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class S3Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "s3Audio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<S3Image> s3Image =new ArrayList<>();

    private String name;

    private String audioUrl;

    private String type;

    @Builder
    public S3Audio(String name, String audioUrl) {
        this.name = name;
        this.audioUrl = audioUrl;
    }
}
