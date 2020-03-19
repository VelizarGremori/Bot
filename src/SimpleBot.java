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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SimpleBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            List<String> command_args = new LinkedList<String>(Arrays.asList(message_text.split(" ")));

            //TODO Проверка на новую команду, нужно что-то нормально придумать будет
            if(message_text.charAt(0) !='/') {
                var session = Session.getSession(chat_id);
                if (session != null){
                    command_args.add(0, session.getMode());
                }
                else{
                    Session.insertSession(chat_id);

                }
            }

            Command command = CommandFactory.getCommand(command_args.get(0));

            var commandString = String.join(" ",command_args);

            System.out.println(commandString);

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
            else if(response.getState() == CommandState.REQUEST_PARAMETER){
                Session.updateSession(chat_id,  commandString);
            }else if( response.getState() == CommandState.REQUEST_AUTHORIZATION){
                message = (new SendMessage()).setChatId(chat_id).setText(response.getMessage());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
    }

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
