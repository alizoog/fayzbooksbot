package uz.fayz.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Section {
    private Integer id;
    private String name;
    private Integer bookId;
}
