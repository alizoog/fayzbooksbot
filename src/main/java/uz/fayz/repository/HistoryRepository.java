package uz.fayz.repository;

import uz.fayz.model.HistoryItem;
import uz.fayz.model.QuestionHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HistoryRepository extends BaseDatabase {

    public void saveHistoryItem(HistoryItem historyItem) {
        try (Connection connection = connection()) {
           PreparedStatement ps = connection.prepareStatement("INSERT INTO history_item (correct_answer, answer,user_id, question_history_id,answer_id, question, question_id,istrue) values (?,?,?,?,?,?,?,?);");
            ps.setString(1,historyItem.getCorrectAnswer());
            ps.setString(2,historyItem.getAnswer());
            ps.setString(3,historyItem.getUserId());
            ps.setInt(4,historyItem.getQuestionHistoryId());
            ps.setInt(5,historyItem.getAnswerId());
            ps.setString(6,historyItem.getQuestion());
            ps.setInt(7,historyItem.getQuestionId());
            ps.setBoolean(8,historyItem.isTrue());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createQuestionHistory(QuestionHistory history) {
        try (Connection connection = connection()) {
           PreparedStatement ps = connection.prepareStatement("INSERT INTO question_history (book_name, user_id, book_id) values (?,?,?)");
            ps.setString(1,history.getBookName());
            ps.setString(2,history.getUserId());
            ps.setInt(3,history.getBookId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isSolvedTest(String chatId, int bookId){
        boolean t = false;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM question_history WHERE book_id = ? AND user_id = ? ");
            ps.setInt(1,bookId);
            ps.setString(2,chatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                t = ( rs.getInt("count") != 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public Double colPrice(String chatId, int bookId) {
        Double t = 0.0;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT sum(q.price) from history_item hi left join question q on hi.question_id = q.id where q.book_id = ? and hi.user_id =? and hi.isTrue");
            ps.setInt(1,bookId);
            ps.setString(2,chatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                t=rs.getDouble("sum");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
