package Test;

import Models.Account;
import Models.Person;
import Models.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        try {
            var uid = UUID.randomUUID().toString();
            var user = User.insertUser(uid, uid, uid);
            var person = Person.insertPerson(uid, user.getId());
            var pers = Person.getPersonsByUser(user.getId());
            var account1 = Account.insertAccount(pers.get(0).getId(), 100);
            var account2 = Account.insertAccount(pers.get(0).getId(), 0);
            Account.transfer(account1.getId(), account2.getId(), 50);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
