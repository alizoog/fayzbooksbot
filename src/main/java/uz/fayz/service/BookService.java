package uz.fayz.service;

import uz.fayz.model.*;
import uz.fayz.repository.BookRepository;
import uz.fayz.repository.HistoryRepository;
import uz.fayz.repository.QuestionRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookService {
    private final BookRepository bookRepository = new BookRepository();
    private final QuestionRepository questionRepository = new QuestionRepository();
    private final HistoryRepository historyRepository = new HistoryRepository();

    public List<Book> getBooks(int i) {
        return bookRepository.getBookList(i);
    }



    public boolean isValidNextPage(int page) {
       int count = bookRepository.getBooksCount();
       return count >= (page+1)*10;
    }

    public List<Section> getBookSectionList(int bookId, int page) {
        return bookRepository.getBookSectionList(bookId, page);
    }

    public String getAnswerAsString(List<Answer> answerList) {
        StringBuilder sb = new StringBuilder();
        int num = 1;
        for (Answer answer : answerList) {
            sb.append(num);
            sb.append(". ");
            sb.append(answer.getName());
            sb.append("\n");
            num++;
        }
        return sb.toString();
    }

    public String getBookSectionAsString(List<Section> bookSectionList) {
        StringBuilder sb = new StringBuilder();
        int num = 1;
        for (Section section : bookSectionList) {
            sb.append(num);
            sb.append(". ");
            sb.append(section.getName());
            sb.append("\n");
            num++;
        }
        return sb.toString();
    }

    public String getBooksAsString(List<Book> bookList) {
        StringBuilder sb = new StringBuilder();
        int num = 1;
        for (Book book : bookList) {
            sb.append(num);
            sb.append(". ");
            sb.append(book.getTitle());
            sb.append("\n");
            num++;
        }
        return sb.toString();
    }
    public String getQuestionAsString(Question question, int n, Double colPrice) {
        return "(Balance : " +colPrice + ")\n" + n + ". " +
                    question.getName() +
                    " (narx : " +
                    question.getPrice() + ")" +
                    "\n";
    }

    public Section getSectionBySectionId(int sectionId) {
        return bookRepository.getSectionBySectionId(sectionId);
    }

    public void selectSection(String chatId, int sectionId) {
        Section section = bookRepository.getSectionBySectionId(sectionId);
        bookRepository.selectSection(chatId, sectionId, section.getBookId());
    }

    public void storyChange(String chatId, String text) {
        bookRepository.storyChange(chatId, text);
    }

    public Map<String, List<Answer>> getQuestionAndAnswer(String chatId, int bookId, int n) {
        Question question = questionRepository.getQuestion(chatId, bookId);
        List<Answer> answers = questionRepository.getAnswers(question.getId());
        Double colPrice = historyRepository.colPrice(chatId, bookId);
        Collections.shuffle(answers);
        String answerAsString = getAnswerAsString(answers);
        String questionAsString = getQuestionAsString(question, n,colPrice);
        Map<String, List<Answer>> map = new HashMap<>();
        map.put(questionAsString + answerAsString, answers);
        return map;
    }

    public int insertHistory(String chatId, int answerId) {
        Answer answer = questionRepository.getAnswerbyId(answerId);
        int bookId = bookRepository.getBookId(answerId);
        int historyId = bookRepository.getQuestionHistoryId(chatId, bookId);
        Question question = questionRepository.getQuestionById(answer.getQuestionId());
        Answer correctAnswer = questionRepository.getCorrectAnswer(question.getId());
        HistoryItem historyItem = HistoryItem.builder()
                .isTrue(answer.isCorrect())
                .answer(answer.getName())
                .correctAnswer(correctAnswer.getName())
                .answerId(answer.getId())
                .question(question.getName())
                .questionId(question.getId())
                .questionHistoryId(historyId)
                .userId(chatId)
                .build();
        historyRepository.saveHistoryItem(historyItem);
        return bookId;
    }

    public void createQuestionHistory(String chatId, int bookId) {
       Book book = bookRepository.getBookById(bookId);
       QuestionHistory questionHistory = QuestionHistory.builder()
               .bookId(bookId)
               .bookName(book.getTitle())
               .userId(chatId)
               .build();
       historyRepository.createQuestionHistory(questionHistory);
    }

    public boolean isSloved(String chatId, int bookId) {
        return historyRepository.isSolvedTest(chatId, bookId);
    }

    public boolean existStory(String chatId, int bookId) {
        return bookRepository.existStory(chatId, bookId);
    }

    public boolean isValidNextPageForSection(int page, int bookId) {
        int count = bookRepository.getBooksCountForSection(bookId);
        return count >= (page+1)*10;
    }

    public Double getBalance(int bookId, String chatId) {
        return historyRepository.colPrice(chatId,bookId);
    }
}
