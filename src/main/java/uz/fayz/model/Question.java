package uz.fayz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
