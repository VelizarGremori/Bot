package commands;

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
            var args = cmd.getArgList().iterator();
            if(args.hasNext())
                args.next();
            this.login = cmd.getOptionValue("l");
            if(this.login == null && args.hasNext())
                this.login = args.next();
            this.password = cmd.getOptionValue("p");
            if(this.password == null && args.hasNext())
                this.password = args.next();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
