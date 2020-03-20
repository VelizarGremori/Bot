import commands.*;
import models.Session;
import shared.Bot;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ConsoleMain {
    public static void main(String[] args) {
        var scanner =  new Scanner(System.in);
        var bot = new FakeBot();
        while (true){
            bot.onUpdate(-1 , scanner.next());
        }
    }

    static class FakeBot
    {
        public void onUpdate(long chat_id, String message_text)
        {
            var response = Bot.handleRequest(chat_id, message_text);
            System.out.println(response.getMessage());
        }
    }
}
