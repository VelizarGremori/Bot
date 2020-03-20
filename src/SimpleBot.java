import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import commands.CommandFactory;
import commands.Command;
import commands.CommandState;
import models.Session;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import shared.Bot;

public class SimpleBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            var response = Bot.handleRequest(chat_id, message_text);

            SendMessage message = new SendMessage().setChatId(chat_id).setText(response.getMessage()).enableMarkdown(true);

            if (response.getState() == CommandState.SUCCESS || response.getState() == CommandState.FAIL){
                setButtons(message, true, true);
            }else if(response.getState() == CommandState.REQUEST_AUTHORIZATION) {
                setButtons(message, true, false);
            }else {
                setButtons(message, false, false);
            }
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setButtons(SendMessage sendMessage, boolean showFirstRow ,boolean showSecondRow) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        if(showFirstRow) {
            KeyboardRow firstKeyboardRow = new KeyboardRow();
            firstKeyboardRow.add(new KeyboardButton("/help"));
            firstKeyboardRow.add(new KeyboardButton("/login"));
            keyboard.add(firstKeyboardRow);
        }
        if(showSecondRow) {
            KeyboardRow secondKeyboardRow = new KeyboardRow();
            secondKeyboardRow.add(new KeyboardButton("/accounts"));
            secondKeyboardRow.add(new KeyboardButton("/transfer"));
            secondKeyboardRow.add(new KeyboardButton("/history"));
            keyboard.add(secondKeyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
    }


    public String getBotUsername() {
        return "Shadow Terminal";
    }

    public String getBotToken() {
        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("src/telegram.properties"))){
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props.getProperty("apiKey");
    }
}
