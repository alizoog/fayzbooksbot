package uz.fayz.model;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    private int id;
    private int bookId;
    private Double price;
    private String name;
}
