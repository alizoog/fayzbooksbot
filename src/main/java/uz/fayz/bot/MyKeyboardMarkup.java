package uz.fayz.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.fayz.model.*;

import java.util.ArrayList;
import java.util.List;

public class MyKeyboardMarkup {
    public ReplyKeyboard createReplyKeyboardMarkup(List<String> keyboard) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        KeyboardRow row = new KeyboardRow();
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 1; i <= keyboard.size(); i++) {
            row.add(new KeyboardButton(keyboard.get(i - 1)));
            if (i % 2 == 0) {
                rows.add(row);
                row = new KeyboardRow();
            }
        }
        if (keyboard.size() % 2 != 0) {
            rows.add(row);
        }

        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public InlineKeyboardMarkup createInlineKeyboardMarkup(List<Book> book, int page) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        int num = 1;
        for (Book book1 : book) {
            InlineKeyboardButton button = new InlineKeyboardButton(String.valueOf(num));
            button.setCallbackData("book" + book1.getId());
            row.add(button);
            if (num % 5 == 0) {
                rows.add(row);
                row = new ArrayList<>();
            }
            num++;
        }
        if (book.size() % 5 != 0) {
            rows.add(row);
        }
        InlineKeyboardButton prev = new InlineKeyboardButton("⬅\uFE0F");
        prev.setCallbackData("prev" + page);
        InlineKeyboardButton next = new InlineKeyboardButton("➡\uFE0F");
        next.setCallbackData("next" + page);
        row = new ArrayList<>();
        row.add(prev);
        row.add(next);


        rows.add(row);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup createInlineKeyboardMarkupForRegion(List<Region> regions) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        int num = 1;
        for (Region region : regions) {
            InlineKeyboardButton button = new InlineKeyboardButton(region.getName());
            button.setCallbackData("region" + region.getId());
            row.add(button);
            if (num % 2 == 0) {
                rows.add(row);
                row = new ArrayList<>();
            }
            num++;
        }
        if (regions.size() % 2 != 0) {
            rows.add(row);
        }


        rows.add(row);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup createInlineKeyboardMarkupForAddress(List<Address> addresses) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        int num = 1;
        for (Address address : addresses) {
            InlineKeyboardButton button = new InlineKeyboardButton(address.getCity());
            button.setCallbackData("address" + address.getId());
            row.add(button);
            if (num % 2 == 0) {
                rows.add(row);
                row = new ArrayList<>();
            }
            num++;
        }
        if (addresses.size() % 2 != 0) {
            rows.add(row);
        }
        rows.add(row);
        InlineKeyboardButton back = new InlineKeyboardButton("Back");
        back.setCallbackData("backReg");
        row = new ArrayList<>();
        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard shareContact() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardButton phoneButton = new KeyboardButton();
        phoneButton.setText("share contact !!!");
        phoneButton.setRequestContact(true);
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(phoneButton);
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = List.of(keyboardRow);
        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup createInlineKeyboardMarkupForSection(int bookId, List<Section> bookSectionList, int page) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        int num = 1;
        for (Section section : bookSectionList) {
            InlineKeyboardButton button = new InlineKeyboardButton(String.valueOf(num));
            button.setCallbackData("section" + section.getId());
            row.add(button);
            if (num % 5 == 0) {
                rows.add(row);
                row = new ArrayList<>();
            }
            num++;
        }
        if (bookSectionList.size() % 5 != 0) {
            rows.add(row);
        }
        InlineKeyboardButton prev = new InlineKeyboardButton("⬅\uFE0F");
        prev.setCallbackData(page + "prevs" + bookId);
        InlineKeyboardButton next = new InlineKeyboardButton("➡\uFE0F");
        next.setCallbackData(page + "nexts" + bookId);
        InlineKeyboardButton back = new InlineKeyboardButton("Back");
        back.setCallbackData("back");
        row = new ArrayList<>();
        row.add(prev);
        row.add(back);
        row.add(next);
        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup createInlineKeyboardMarkupForAnswer(List<Answer> answers) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        int num = 1;
        for (Answer answer1 : answers) {
            InlineKeyboardButton button = new InlineKeyboardButton(String.valueOf(num));
            button.setCallbackData("answer" + answer1.getId());
            row.add(button);
            if (num % 5 == 0) {
                rows.add(row);
                row = new ArrayList<>();
            }
            num++;
        }
        if (answers.size() % 5 != 0) {
            rows.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;

    }
}
