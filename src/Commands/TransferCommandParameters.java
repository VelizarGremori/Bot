package commands;

import org.apache.commons.cli.*;

public class TransferCommandParameters extends CommandParameters{

    private String source_id;
    private String target_id;
    private String amount;

    public String getSource_id() {
        return source_id;
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

        Option source_id = new Option("s", "sourceId", true, "sourceId");
        options.addOption(source_id);

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
        var args = cmd.getArgList().iterator();
        args.next();
        this.source_id = cmd.getOptionValue("o");
        if(this.source_id == null && args.hasNext())
            this.source_id = args.next();
        this.target_id = cmd.getOptionValue("t");
        if(this.target_id == null && args.hasNext())
            this.target_id = args.next();
        this.amount = cmd.getOptionValue("a");;
        if(this.amount == null && args.hasNext())
            this.amount = args.next();
    }
}
