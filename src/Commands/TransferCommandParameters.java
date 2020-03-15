package Commands;

import org.apache.commons.cli.*;

public class TransferCommandParameters extends CommandParameters{

    private String owner_id;
    private String target_id;
    private String amount;

    public String getOwner_id() {
        return owner_id;
    }

    public String getTarget_id() {
        return target_id;
    }

    public String getAmount() {
        return amount;
    }

    public TransferCommandParameters(long chat_id) {
        super(chat_id);
    }

    @Override
    void parse(String[] params) {
        Options options = new Options();

        Option owner_id = new Option("o", "ownerId", true, "ownerId");
        options.addOption(owner_id);

        Option target_id = new Option("t", "targetId", true, "targetId");
        options.addOption(target_id);

        Option amount = new Option("a", "Amount", true, "Amount");
        options.addOption(amount);


        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, params);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.owner_id = cmd.getOptionValue("o");
        this.target_id = cmd.getOptionValue("t");
        this.amount = cmd.getOptionValue("a");
    }
}
