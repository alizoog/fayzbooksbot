package uz.fayz.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class HistoryItem {
  private Integer id;
  private Integer questionHistoryId;
  private String userId;
  private Integer questionId;
  private Integer answerId;
  private String question;
  private String answer;
  private String correctAnswer;
  private boolean isTrue;

}