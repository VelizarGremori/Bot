package Commands;

import org.apache.commons.cli.*;
public class LoginCommandParameters extends CommandParameters
{
    private String login;
    private String password;

    public LoginCommandParameters(long chat_id) {
        super(chat_id);
    }

    @Override
    void parse(String[] params) {
            Options options = new Options();

            Option login = new Option("l", "Login", true, "Login");
            options.addOption(login);

            Option password = new Option("p", "Password", true, "Password");
            options.addOption(password);

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = null;
            try {
                cmd = parser.parse(options, params);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            this.login = cmd.getOptionValue("l");
            this.password = cmd.getOptionValue("p");
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
