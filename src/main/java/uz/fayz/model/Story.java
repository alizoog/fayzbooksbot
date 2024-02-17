package uz.fayz.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Story {
    private Integer id;
    private String body;
    private String score;
    private Double price;
    private Integer sectionId;
}
