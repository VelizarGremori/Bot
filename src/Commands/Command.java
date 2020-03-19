package commands;

import java.sql.SQLException;

public abstract class Command<P extends CommandParameters> {

   protected CommandResponse response = new CommandResponse();

   public CommandResponse execute() {
      if(check()){
         try {
            executeCommand(getParameters());
         } catch (SQLException e) {
            throw new RuntimeException(e);
         }
      }
      return response;
   }

   public abstract void setParameters(String[] params, long chatId);
   public abstract void setParameters(P p);

   public abstract P getParameters();

   abstract protected boolean check();

   abstract protected void executeCommand(P params) throws SQLException;
}
