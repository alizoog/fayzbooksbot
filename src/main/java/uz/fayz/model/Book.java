package uz.fayz.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Integer id;

    private String title;

    private Boolean isActive;

    private List<Section> sectionList;
}

