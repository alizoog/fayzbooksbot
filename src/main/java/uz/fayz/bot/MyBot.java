package uz.fayz.bot;

import lombok.SneakyThrows;
import uz.fayz.model.*;
import uz.fayz.service.BookService;
import uz.fayz.service.RegionService;
import uz.fayz.service.UserService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static uz.fayz.enums.StepEnum.*;
import static uz.fayz.util.BotConstants.TOKEN;
import static uz.fayz.util.BotConstants.USERNAME;

public class MyBot extends TelegramLongPollingBot {
    private final RegionService regionService = new RegionService();
    private final UserService userService = new UserService();
    private final BookService bookService = new BookService();
    private final MyKeyboardMarkup myKeyboardMarkup = new MyKeyboardMarkup();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String chatId = update.getMessage().getChatId().toString();
            Message message = update.getMessage();
            Contact contact = update.getMessage().getContact();
            User newUser = userService.getUserByChatId(chatId);


            if (message.hasText()) {
                String text = message.getText();


                if (text.equals("/start")) {
                    sendTextMessage(chatId, "Assalomu alaykum. Botga xush kelibsiz!");
                    if (newUser == null) {
                        newUser = new User();
                        sendTextMessage(chatId, "Ism familiyangizni kiriting");
                        newUser.setChatId(chatId);
                        userService.save(newUser, FIRST_NAME);
                        return;
                    } else if (Objects.equals(newUser.getStep(), FIRST_NAME)
                               || Objects.equals(newUser.getStep(), LAST_NAME) || Objects.equals(newUser.getStep(), AGE) ||
                               Objects.equals(newUser.getStep(), SHARE_CONTACT) || Objects.equals(newUser.getStep(), STUDY_CENTER) ||
                               Objects.equals(newUser.getStep(), TEACHER_NAME) || Objects.equals(newUser.getStep(), CLASS)) {
                        sendTextMessage(chatId, "Ism familiyangizni kiriting");
                        userService.editUser(newUser, FIRST_NAME);
                        return;
                    }

                    newUser.setStep(REGISTERED);
                    userService.editUser(newUser, REGISTERED);
                }
                if (Objects.equals(newUser.getStep(), FIRST_NAME)) {
                    newUser.setFullName(text);
                    List<Region> regions = regionService.getRegions();
                    sendReplyRequest(chatId, "Viloyatingizni tanlang:", myKeyboardMarkup.createInlineKeyboardMarkupForRegion(regions));
                    return;
                }
                if (newUser.getStep().equals(LAST_NAME)) {
                    sendReplyRequest(chatId, "Raqamingizni yuboring", myKeyboardMarkup.shareContact());
                    userService.editUser(newUser, SHARE_CONTACT);
                }
                if (newUser.getStep().equals(AGE)) {
                    int age = -1;
                    try {
                        age = Integer.valueOf(text);
                        newUser.setAge(age);
                        if (age < 7 && age > 17) {

                        }
                    } catch (NumberFormatException e) {
                        sendReplyRequest(chatId, "Yoshingizni kiriting (raqam yozing)", null);
                        return;
                    }
                    newUser.setAge(age);
                    if (age < 7 || age > 17) {
                        newUser.setStep(REGISTERED);
                        userService.editUser(newUser, REGISTERED);
                    } else {
                        sendTextMessage(chatId, "Maktabingizni kiriting");
                        userService.editUser(newUser, STUDY_CENTER);
                    }
                }
                if (newUser.getStep().equals(STUDY_CENTER)) {
                    newUser.setSchool(text);
                    sendTextMessage(chatId, "Nechanchi Sinfda Uqiysiz");
                    userService.editUser(newUser, CLASS);
                }
                if (newUser.getStep().equals(CLASS)) {
                    try {
                        int classNumber = Integer.parseInt(text);
                        newUser.setClassNumber(classNumber);
                        if (classNumber < 1 || classNumber > 11) throw new NumberFormatException();
                    } catch (NumberFormatException e) {
                        sendReplyRequest(chatId, "Nechanchi Sinfda Uqiysiz (raqam yozing)", null);
                        return;
                    }
                    sendTextMessage(chatId, "Uqituvchingizning Ism Familiyasi");
                    userService.editUser(newUser, TEACHER_NAME);
                }
                if (newUser.getStep().equals(TEACHER_NAME)) {
                    newUser.setTeacherName(text);
                    newUser.setStep(REGISTERED);
                    userService.editUser(newUser, REGISTERED);
                    sendTextMessage(chatId, "muvaffaqiyatli ro'yxatdan o'tdingiz! ");
                }
                if ((Objects.equals(newUser.getStep(), REGISTERED) || Objects.equals(newUser.getStep(), SELECTED_TEST)) && text.equals("Hikoya yozish")) {
                    List<Book> books = bookService.getBooks(0);
                    sendReplyRequest(chatId, bookService.getBooksAsString(books), myKeyboardMarkup.createInlineKeyboardMarkup(books, 0));
                    userService.editUser(newUser, SELECTED_STORY);
                    return;
                }
                if ((Objects.equals(newUser.getStep(), REGISTERED) || Objects.equals(newUser.getStep(), SELECTED_STORY)) && text.equals("Test Ishlash")) {
                    List<Book> books = bookService.getBooks(0);
                    sendReplyRequest(chatId, bookService.getBooksAsString(books), myKeyboardMarkup.createInlineKeyboardMarkup(books, 0));
                    userService.editUser(newUser, SELECTED_TEST);

                }
                if ((Objects.equals(newUser.getStep(), HIKOYA_TANLOVI)) && text.equals("Menu")) {
                    sendReplyRequest(chatId, "Kategoriya tanlang", myKeyboardMarkup.createReplyKeyboardMarkup(List.of("Hikoya yozish", "Test Ishlash")));
                    userService.editUser(newUser, REGISTERED);

                } else if ((Objects.equals(newUser.getStep(), TEST_ISHLAMOQDA) || Objects.equals(newUser.getStep(), SELECTED_TEST)) && Objects.equals(text, "Testni yakunlash")) {
                    sendReplyRequest(chatId, "Test bekor qilindi Ishtirokingiz uchun rahmat", myKeyboardMarkup.createReplyKeyboardMarkup(List.of("Hikoya yozish", "Test Ishlash")));
                    newUser.setTestNumber(0);
                    userService.editUser(newUser, REGISTERED);
                } else if (Objects.equals(newUser.getStep(), HIKOYA_TANLOVI)) {
                    bookService.storyChange(chatId, text);
                    sendReplyRequest(chatId, "Hikoyangiz qabul qilindi", myKeyboardMarkup.createReplyKeyboardMarkup(List.of("Hikoya yozish", "Test Ishlash")));
                    userService.editUser(newUser, REGISTERED);
                }
                if (Objects.equals(newUser.getStep(), REGISTERED)) {
                    bookService.storyChange(chatId, text);
                    sendReplyRequest(chatId, "Kategoriyalardan birini tanlang!", myKeyboardMarkup.createReplyKeyboardMarkup(List.of("Hikoya yozish", "Test Ishlash")));
                }

            } else if (newUser.getStep().equals(SHARE_CONTACT)) {
                if (message.hasText()) newUser.setPhoneNumber(message.getText());
                else newUser.setPhoneNumber(contact.getPhoneNumber());
                sendReplyRequest(chatId, "Yoshingizni kiriting", null);
                userService.editUser(newUser, AGE);
            }
        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            User user = userService.getUserByChatId(chatId);
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            if (data.startsWith("prev")) {
                int page = Integer.parseInt(data.substring(4));
                if (page > 0) {
                    List<Book> books = bookService.getBooks(page - 1);
                    editReplyRequest(chatId, bookService.getBooksAsString(books), messageId, myKeyboardMarkup.createInlineKeyboardMarkup(books, page - 1));
                }

            } else if (data.startsWith("next")) {
                int page = Integer.parseInt(data.substring(4));
                if (bookService.isValidNextPage(page)) {
                    List<Book> books = bookService.getBooks(page + 1);
                    editReplyRequest(chatId, bookService.getBooksAsString(books), messageId, myKeyboardMarkup.createInlineKeyboardMarkup(books, page + 1));
                }
            } else if (data.startsWith("region")) {
                int regionId = Integer.parseInt(data.substring(6));
                List<Address> addresses = regionService.getAddress(regionId);
                editReplyRequest(chatId, "Tumaningizni tanlang", messageId, myKeyboardMarkup.createInlineKeyboardMarkupForAddress(addresses));

            } else if (data.startsWith("address")) {
                int addressId = Integer.parseInt(data.substring(7));
                user.setAddressId(addressId);
                userService.editUser(user, SHARE_CONTACT);
                deleteMessage(update, chatId);
                sendReplyRequest(chatId, "Raqamingizni yuboring", myKeyboardMarkup.shareContact());
            } else if (data.startsWith("prevs")) {
                int page = Integer.parseInt(data.substring(0, 1));
                int bookId = Integer.parseInt(data.substring(6));
                if (page > 0) {
                    List<Section> bookSectionList = bookService.getBookSectionList(bookId, page - 1);
                    String bookSectionAsString = bookService.getBookSectionAsString(bookSectionList);
                    editReplyRequest(chatId, bookSectionAsString, messageId, myKeyboardMarkup.createInlineKeyboardMarkupForSection(bookId, bookSectionList, page - 1));
                }

            } else if (data.contains("nexts")) {
                int page = Integer.parseInt(data.substring(0, 1));
                int bookId = Integer.parseInt(data.substring(6));
                if (bookService.isValidNextPageForSection(page, bookId)) {
                    List<Section> bookSectionList = bookService.getBookSectionList(bookId, page + 1);
                    String bookSectionAsString = bookService.getBookSectionAsString(bookSectionList);
                    editReplyRequest(chatId, bookSectionAsString, messageId, myKeyboardMarkup.createInlineKeyboardMarkupForSection(bookId, bookSectionList, page + 1));
                }
            } else if (data.startsWith("back")) {
                List<Book> books = bookService.getBooks(0);
                editReplyRequest(chatId, bookService.getBooksAsString(books), messageId, myKeyboardMarkup.createInlineKeyboardMarkup(books, 0));
                userService.editUser(user, SELECTED_STORY);
            } else if (data.startsWith("book")) {
                int bookId = Integer.parseInt(data.substring(4));
                if (Objects.equals(SELECTED_STORY, user.getStep())) {
                    if (bookService.existStory(chatId, bookId)) {
                        sendTextMessage(chatId, "Bu kitobga hikoya yozgansiz");
                        return;
                    }
                    List<Section> bookSectionList = bookService.getBookSectionList(bookId, 0);
                    String bookSectionAsString = bookService.getBookSectionAsString(bookSectionList);
                    if (bookSectionAsString.isEmpty()) {
                        sendTextMessage(chatId, "Hikoya yozishga vaqtinchalik ruxsat yuq");
//                        List<Book> books = bookService.getBooks(0);
//                        editReplyRequest(chatId, bookService.getBooksAsString(books), messageId, myKeyboardMarkup.createInlineKeyboardMarkup(books, 0));
                        return;
                    }
                    editReplyRequest(chatId, bookSectionAsString, messageId, myKeyboardMarkup.createInlineKeyboardMarkupForSection(bookId, bookSectionList, 0));
                } else if (Objects.equals(SELECTED_TEST, user.getStep())) {
                    if (bookService.isSloved(chatId, bookId)) {
                        sendReplyRequest(chatId, "Bu kitobga test Ishlagansiz", myKeyboardMarkup.createReplyKeyboardMarkup(List.of("Testni yakunlash")));
                        return;
                    }

                    bookService.createQuestionHistory(chatId, bookId);
                    Map<String, List<Answer>> questionMap = bookService.getQuestionAndAnswer(chatId, bookId, user.getTestNumber() + 1);
                    if (questionMap.isEmpty()) {
                        sendTextMessage(chatId, "Test yuq");
                        userService.editUser(user, REGISTERED);
                    }
                    deleteMessage(update, chatId);
                    sendReplyRequest(chatId, "Test boshlandi, Omad!!!", myKeyboardMarkup.createReplyKeyboardMarkup(List.of("Testni yakunlash")));
                    for (String question : questionMap.keySet()) {
                        sendReplyRequest(chatId, question, myKeyboardMarkup.createInlineKeyboardMarkupForAnswer(questionMap.get(question)));
                    }
                    user.setTestNumber(1);
                    userService.editUser(user, TEST_ISHLAMOQDA);

                }
            } else if (data.startsWith("answer")) {
                int answerId = Integer.parseInt(data.substring(6));
                int bookId = bookService.insertHistory(chatId, answerId);

                if (user.getTestNumber() == 10) {
                    deleteMessage(update, chatId);
                    Double balance = bookService.getBalance(bookId, chatId);
                    bookService.setQuestionHistory(chatId,bookId);
                    sendReplyRequest(chatId, "Test yakunlandi Ishtirokingiz uchun rahmat, Balance : " + balance, myKeyboardMarkup.createReplyKeyboardMarkup(List.of("Hikoya yozish", "Test Ishlash")));
                    user.setTestNumber(0);
                    userService.editUser(user, REGISTERED);
                } else {
                    Map<String, List<Answer>> questionMap = bookService.getQuestionAndAnswer(chatId, bookId, user.getTestNumber() + 1);
                    if (questionMap.isEmpty()) {
                        Double balance = bookService.getBalance(bookId, chatId);
                        bookService.setQuestionHistory(chatId,bookId);
                        sendReplyRequest(chatId, "Test yakunlandi Ishtirokingiz uchun rahmat, Balance : " + balance, myKeyboardMarkup.createReplyKeyboardMarkup(List.of("Hikoya yozish", "Test Ishlash")));
                        user.setTestNumber(0);
                        userService.editUser(user, REGISTERED);
                    }
                    for (String question : questionMap.keySet()) {
                        editReplyRequest(chatId, question, messageId, myKeyboardMarkup.createInlineKeyboardMarkupForAnswer(questionMap.get(question)));
                    }
                    user.setTestNumber(user.getTestNumber() + 1);
                    userService.editUser(user, TEST_ISHLAMOQDA);
                }
            } else if (data.startsWith("section")) {
                int sectionId = Integer.parseInt(data.substring(7));
                Section section = bookService.getSectionBySectionId(sectionId);
                sendReplyRequest(chatId, section.getName() + " Bo'limiga hikoya yozing", myKeyboardMarkup.createReplyKeyboardMarkup(List.of("Menu")));
                deleteMessage(update, chatId);
                userService.userChangeStatus(chatId, HIKOYA_TANLOVI);
                bookService.selectSection(chatId, sectionId);
            }
        }
    }

    private void deleteMessage(Update update, String chatId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private void sendTextMessage(String chatId, String text) {
        execute(new SendMessage(chatId, text));
    }

    @SneakyThrows
    private void sendReplyRequest(String chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        sendMessage.setReplyMarkup(replyKeyboard);
        execute(sendMessage);
    }

    @SneakyThrows
    private void editReplyRequest(String chatId, String text, Integer messageId, InlineKeyboardMarkup replyKeyboard) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setText(text);
        editMessageText.setMessageId(messageId);
        editMessageText.setReplyMarkup(replyKeyboard);

        execute(editMessageText);
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}
