import java.util.*;

import Commands.CommandFactory;
import Commands.Command;
import Commands.CommandState;
import Models.Session;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SimpleBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            List<String> command_args = Arrays.asList(message_text.split(" "));

            //TODO Проверка на новую команду, нужно что-то нормально придумать будет
            if(message_text.charAt(0) !='/') {
                var session = Session.getSession(chat_id);
                if (session != null)
                    command_args.add(0, session.getMode());
            }

            Command command = CommandFactory.getCommand(command_args.get(0));

            var commandString = String.join(" ",command_args);

            command.setParameters(command_args.toArray(new String[0]), chat_id);
            var response = command.execute();

            SendMessage message = (new SendMessage()).setChatId(chat_id).setText(response.getMessage());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            if(response.getState() == CommandState.SUCCESS){
                Session.updateSession(chat_id, null);
            }
            else{
                Session.updateSession(chat_id,  commandString);
            }
    }

}


    public String getBotUsername() {
        return "Bot Buterbrod";
    }

    public String getBotToken() {
        return "979329461:AAHt7Ii2unaGXsDLTPjBZFXhjrbxXYbiNYA";
    }
}
