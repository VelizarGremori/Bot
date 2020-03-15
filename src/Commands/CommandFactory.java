package Commands;

import org.reflections.Reflections;

import java.util.Set;

public class CommandFactory {
    public static Command getCommand(String command)
    {
        Reflections reflections = new Reflections("Commands");

        Set<Class<? extends Command>> allClasses =
                reflections.getSubTypesOf(Command.class);

        for (Class<? extends Command> commandExecuterClass:
             allClasses ) {
            if (commandExecuterClass.getAnnotation(CommandName.class).value() == command) {
                try {
                    return commandExecuterClass.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return new HelpCommand();
    }

}
