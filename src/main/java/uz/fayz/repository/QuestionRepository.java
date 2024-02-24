package uz.fayz.repository;

import uz.fayz.model.Answer;
import uz.fayz.model.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository extends BaseDatabase{
    public List<Question> getQuestion(String chatId, int bookId) {
        List<Question> questions = new ArrayList<>();
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select q.* from question q where q.id not in (select h.question_id from history_item h where h.user_id  = ?) and q.book_id = ? ");
            ps.setString(1,chatId);
            ps.setInt(2,bookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               Question question = Question.builder()
                        .id(rs.getInt("id"))
                        .bookId(rs.getInt("book_id"))
                        .name(rs.getString("name"))
                        .price(rs.getDouble("price"))
                        .build();
                questions.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }

    public List<Answer> getAnswers(int questionId) {
            List<Answer> answerList = new ArrayList<>();
            try (Connection connection = connection()) {
                PreparedStatement ps = connection.prepareStatement("select * from answer where question_id = ?");
                ps.setInt(1,questionId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Answer answer = Answer.builder()
                            .id(rs.getInt("id"))
                            .correct(rs.getBoolean("correct"))
                            .name(rs.getString("name"))
                            .questionId(rs.getInt("question_id"))
                            .build();
                    answerList.add(answer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return answerList;

    }

    public Answer getAnswerbyId(int answerId) {
        Answer answer = null;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select * from answer where id = ?");
            ps.setInt(1,answerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                 answer = Answer.builder()
                        .id(rs.getInt("id"))
                        .correct(rs.getBoolean("correct"))
                        .name(rs.getString("name"))
                        .questionId(rs.getInt("question_id"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;
    }

    public Question getQuestionById(int questionId) {
        Question question = null;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select * from question where id = ? ");
            ps.setInt(1,questionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                question = Question.builder()
                        .id(rs.getInt("id"))
                        .bookId(rs.getInt("book_id"))
                        .name(rs.getString("name"))
                        .price(rs.getDouble("price"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return question;
    }

    public Answer getCorrectAnswer(int questionId) {
        Answer answer = null;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select * from answer where question_id = ? and correct");
            ps.setInt(1,questionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                answer = Answer.builder()
                        .id(rs.getInt("id"))
                        .correct(rs.getBoolean("correct"))
                        .name(rs.getString("name"))
                        .questionId(rs.getInt("question_id"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;
    }
}
