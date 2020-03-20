package shared;

import commands.Command;
import commands.CommandFactory;
import commands.CommandResponse;
import commands.CommandState;
import models.Session;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Bot {
    public static CommandResponse handleRequest(long chat_id, String message_text)
    {
        List<String> command_args = new LinkedList<String>(Arrays.asList(message_text.split(" ")));
        if(message_text.charAt(0) !='/') {
            var session = Session.getSession(chat_id);
            if (session != null){
                command_args.addAll(0, Arrays.asList(session.getMode().split(" ")));
            }
            else{
                Session.insertSession(chat_id);
            }
        }

        Command command = CommandFactory.getCommand(command_args.get(0));

        var commandString = String.join(" ",command_args);

        System.out.println(commandString);

        command.setParameters(command_args.toArray(new String[0]), chat_id);
        var response = command.execute();;

        if(response.getState() == CommandState.REQUEST_PARAMETER){
            Session.updateSession(chat_id, commandString);
        }
        else{
            Session.updateSession(chat_id, null);
        }

        return response;
    }
}
