package uz.fayz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.fayz.model.Answer;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    private int id;
    private int bookId;
    private Double price;
    private String name;
    private List<Answer> answerList;
}
