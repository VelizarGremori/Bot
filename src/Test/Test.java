package test;

import models.Account;
import models.Person;
import models.Transaction;
import models.User;
import org.junit.Assert;

import java.sql.SQLException;
import java.util.UUID;

public class Test {

    @org.junit.Test
    public void ModelTest() {
        try {
            var uid = UUID.randomUUID().toString();
            var user = User.insertUser(uid, uid, uid);
            Person.insertPerson(uid, user.getId());
            var pers = Person.getPersonsByUser(user.getId());
            var sourceAccount = Account.insertAccount(pers.get(0).getId(), 100);
            var targetAccount = Account.insertAccount(pers.get(0).getId(), 0);
            Account.transfer(sourceAccount.getId(), targetAccount.getId(), 50);

            sourceAccount = Account.getAccount(sourceAccount.getId());
            Assert.assertEquals(50, sourceAccount.getSum());

            targetAccount = Account.getAccount(targetAccount.getId());
            Assert.assertEquals(50, targetAccount.getSum());

            var SourceTransactions = Transaction.getTransactionByAccount(sourceAccount.getId());
            var targetTransactions = Transaction.getTransactionByAccount(sourceAccount.getId());

            Assert.assertEquals(1, SourceTransactions.size());
            Assert.assertEquals(sourceAccount.getId(), SourceTransactions.get(0).getSourceAccountId());
            Assert.assertEquals(targetAccount.getId(), SourceTransactions.get(0).getTargetAccountId());
            Assert.assertEquals(50, SourceTransactions.get(0).getAmount());

            Assert.assertEquals(1, targetTransactions.size());
            Assert.assertEquals(sourceAccount.getId(), SourceTransactions.get(0).getSourceAccountId());
            Assert.assertEquals(targetAccount.getId(), SourceTransactions.get(0).getTargetAccountId());
            Assert.assertEquals(50, SourceTransactions.get(0).getAmount());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
