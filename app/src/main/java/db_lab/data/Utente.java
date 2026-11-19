package db_lab.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class Utente {
    public final String username;
    public final String nome;
    public final String cognome;
    public final String email;

    public Utente(String username, String nome, String cognome, String email) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    @Override
    public String toString() {
        return username;
    }

    public static final class DAO {
        private DAO() { }

        public static List<Utente> list(Connection connection) {
            var result = new ArrayList<Utente>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_UTENTI);
                var rs = statement.executeQuery()
            ) {
                while (rs.next()) {
                    result.add(new Utente(
                        rs.getString("username"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email")
                    ));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return result;
        }

        public static void insert(Connection connection, String username, String password,
                                  String nome, String cognome, String email) {
            try (var statement = DAOUtils.prepare(
                    connection,
                    Queries.INSERT_UTENTE,
                    username, password, nome, cognome, email)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
