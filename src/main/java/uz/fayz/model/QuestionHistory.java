package uz.fayz.model;

import lombok.*;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionHistory {
    private Integer id;
    private List<HistoryItem> answers;
    private String userId;
    private Integer bookId;
    private String bookName;
    private Integer numberOfQuestion;
    private Integer correctAnswer;

}