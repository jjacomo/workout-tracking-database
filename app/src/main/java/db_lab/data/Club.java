package db_lab.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class Club {
    public final int id;
    public final String nome;
    public final String descrizione;

    public Club(int id, String nome, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        return id + " - " + nome;
    }

    public static final class DAO {
        private DAO() { }

        public static List<Club> list(Connection connection) {
            var result = new ArrayList<Club>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_CLUB);
                var rs = statement.executeQuery()
            ) {
                while (rs.next()) {
                    result.add(new Club(
                            rs.getInt("IDClub"),
                            rs.getString("nome"),
                            rs.getString("descrizione")));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return result;
        }

        public static void insert(Connection connection, int idClub, String nome, String descrizione) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.INSERT_CLUB, idClub, nome, descrizione)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
