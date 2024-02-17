package uz.fayz.repository;

import uz.fayz.model.Book;
import uz.fayz.model.Section;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookRepository extends BaseDatabase {

    public List<Book> getBookList(int page) {
        List<Book> bookList = new ArrayList<>();
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select * from book where is_active order by book.id limit 10 offset ?");
            ps.setInt(1,page*10);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Book book = Book.builder()
                        .title(rs.getString("title"))
                        .id(rs.getInt("id"))
                        .build();
                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public int getBooksCount() {
        int count = -1;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select count(*) from book where is_active");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<Section> getBookSectionList(int bookId, int page) {
        List<Section> bookSectionList = new ArrayList<>();
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select * from section where book_id = ? order by id limit 10 offset ?");
            ps.setInt(1,bookId);
            ps.setInt(2,page*10);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Section section = Section.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id"))
                        .build();
                bookSectionList.add(section);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookSectionList;
    }

    public Section getSectionBySectionId(int sectionId) {
        Section section = null;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select * from section where id = ? ");
            ps.setInt(1,sectionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                 section = Section.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id"))
                         .bookId(rs.getInt("book_id"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return section;
    }



    public void selectSection(String chatId, int sectionId, Integer bookId) {
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO story (section_id, user_chat_id,book_id) values (?,?,?)");
            ps.setInt(1,sectionId);
            ps.setString(2,chatId);
            ps.setInt(3,bookId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storyChange(String chatId, String text) {
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE story set  body = ? where user_chat_id = ? and body is null");
            ps.setString(1,text);
            ps.setString(2,chatId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getBookId(int answerId) {
        int bookId = -1;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select q.book_id from question q join answer a on q.id = a.question_id where a.id = ?");
            ps.setInt(1,answerId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                bookId = rs.getInt("book_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookId;
    }

    public int getQuestionHistoryId(String chatId, int bookId) {
        int historyId = -1;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select h.id from question_history h where h.user_id = ? and h.book_id = ?");
            ps.setString(1,chatId);
            ps.setInt(2,bookId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                historyId = rs.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return historyId;
    }

    public Book getBookById(int bookId) {
        Book book = null;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select * from book where id = ? ");
            ps.setInt(1,bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                book = Book.builder()
                        .title(rs.getString("title"))
                        .id(rs.getInt("id"))
                        .isActive(rs.getBoolean("is_active"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

    public boolean existStory(String chatId, int bookId) {
            boolean exist = false;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select * from story where book_id = ? and user_chat_id = ? ");
            ps.setInt(1,bookId);
            ps.setString(2,chatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
              exist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exist;
    }

    public int getBooksCountForSection(int bookId) {
        int count = -1;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select count(*) from section where book_id = ?");
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
