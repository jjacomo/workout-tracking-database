package db_lab.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class TrattoGara {
    public final int codiceGara;
    public final String nomeGara;
    public final int ordine;
    public final int idSegmento;
    public final String nomeSegmento;

    public TrattoGara(int codiceGara, String nomeGara, int ordine,
                      int idSegmento, String nomeSegmento) {
        this.codiceGara = codiceGara;
        this.nomeGara = nomeGara;
        this.ordine = ordine;
        this.idSegmento = idSegmento;
        this.nomeSegmento = nomeSegmento;
    }

    public static final class DAO {
        private DAO() { }

        public static List<TrattoGara> list(Connection connection) {
            var result = new ArrayList<TrattoGara>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_TRATTI_GARA);
                var rs = statement.executeQuery()
            ) {
                while (rs.next()) {
                    result.add(new TrattoGara(
                            rs.getInt("codiceGara"),
                            rs.getString("nomeGara"),
                            rs.getInt("ordine"),
                            rs.getInt("IDSegmento"),
                            rs.getString("nomeSegmento")));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return result;
        }

        public static void insert(Connection connection, int codiceGara,
                                  int ordine, int idSegmento) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.INSERT_TRATTO_GARA,
                    codiceGara, ordine, idSegmento)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
