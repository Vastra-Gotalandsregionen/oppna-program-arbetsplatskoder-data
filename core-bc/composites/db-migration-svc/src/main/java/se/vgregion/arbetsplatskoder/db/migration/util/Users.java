package se.vgregion.arbetsplatskoder.db.migration.util;

import se.vgregion.arbetsplatskoder.db.migration.AbstractJob;
import se.vgregion.arbetsplatskoder.db.migration.sql.ConnectionExt;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static se.vgregion.arbetsplatskoder.db.migration.AbstractJob.getDatabaseCacheDirectory;

/**
 * Use this utility class to temporarily remove _user- and _user_prodn1- items from the db.
 * Good to have when running the migration scripts.
 */
public class Users {

    public static void main(String[] args) {
        // saveUsersFromDatabaseToFileThenDeleteAll();
        insertUsersAndProdLinksFromFile();
    }

    public static void insertUsersAndProdLinksFromFile() {
        ConnectionExt connection = AbstractJob.getMainConnectionExt();
        UsersAndProdLink items = load();
        for (Map<String, Object> user : items.getUsers()) {
            String bytea = (String) user.get("thumbnailphoto");
            if (bytea != null)
                user.put("thumbnailphoto", bytea.getBytes());
            connection.insert("_user", user);
        }
        connection.commit();
        for (Map<String, Object> prod : items.getProdn1link()) {
            connection.insert("_user_prodn1", prod);
        }
        connection.commit();
    }

    public static void saveUsersFromDatabaseToFileThenDeleteAll() {
        saveUsersFromDatabaseToFile();
        ConnectionExt connection = AbstractJob.getMainConnectionExt();
        connection.execute("delete from _user_prodn1");
        connection.execute("delete from _user");
        connection.commit();
    }

    public static void saveUsersFromDatabaseToFile() {
        ConnectionExt connection = AbstractJob.getMainConnectionExt();
        List<Map<String, Object>> users = connection.query("select * from _user", 0, 100_000);
        List<Map<String, Object>> prodn1link = connection.query("select * from _user_prodn1", 0, 100_000);

        store(new UsersAndProdLink(users, prodn1link));
    }

    public static void store(UsersAndProdLink usersAndProdLink) {
        Path file = Paths.get(getDatabaseCacheDirectory(), "new.users.prodlink.json");
        Zerial.toJsonFile(usersAndProdLink, file);
    }

    public static UsersAndProdLink load() {
        Path file = Paths.get(getDatabaseCacheDirectory(), "new.users.prodlink.json");
        return Zerial.fromJsonFile(UsersAndProdLink.class, file);
    }

    private static class UsersAndProdLink {

        public UsersAndProdLink(List<Map<String, Object>> users, List<Map<String, Object>> prodn1link) {
            this.users = users;
            this.prodn1link = prodn1link;
        }

        public UsersAndProdLink() {

        }

        private List<Map<String, Object>> users;
        private List<Map<String, Object>> prodn1link;

        public List<Map<String, Object>> getUsers() {
            return users;
        }

        public void setUsers(List<Map<String, Object>> users) {
            this.users = users;
        }

        public List<Map<String, Object>> getProdn1link() {
            return prodn1link;
        }

        public void setProdn1link(List<Map<String, Object>> prodn1link) {
            this.prodn1link = prodn1link;
        }

    }

}
