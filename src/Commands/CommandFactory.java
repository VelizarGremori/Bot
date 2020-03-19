package commands;

import org.apache.commons.logging.Log;
import org.reflections.Reflections;

import java.util.Set;

public class CommandFactory {
    public static Command getCommand(String command)
    {
        Reflections reflections = new Reflections("commands");

        Set<Class<? extends Command>> allClasses =
                reflections.getSubTypesOf(Command.class);

        for (Class<? extends Command> commandClass:
             allClasses ) {
            if (command.equals(commandClass.getAnnotation(CommandName.class).value())) {
                try {
                    return commandClass.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return new HelpCommand();
    }

}
