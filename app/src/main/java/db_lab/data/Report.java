package db_lab.data;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Risultato tabellare generico usato dalle operazioni statistiche OP13-OP19.
 * Mantiene l'applicazione semplice: le query restano esplicite in Queries.java,
 * mentre questa classe si occupa solo di trasformare un ResultSet in colonne e righe.
 */
public final class Report {
    public final List<String> columns;
    public final List<List<Object>> rows;

    public Report(List<String> columns, List<List<Object>> rows) {
        this.columns = List.copyOf(columns);
        this.rows = rows.stream()
                .map(row -> Collections.unmodifiableList(new ArrayList<>(row)))
                .toList();
    }

    public static final class DAO {
        private DAO() { }

        public static Report execute(Connection connection, String query, Object... parameters) {
            try (
                var statement = DAOUtils.prepare(connection, query, parameters);
                var rs = statement.executeQuery()
            ) {
                ResultSetMetaData metadata = rs.getMetaData();
                int columnCount = metadata.getColumnCount();

                var columns = new ArrayList<String>();
                for (int i = 1; i <= columnCount; i++) {
                    columns.add(metadata.getColumnLabel(i));
                }

                var rows = new ArrayList<List<Object>>();
                while (rs.next()) {
                    var row = new ArrayList<Object>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getObject(i));
                    }
                    rows.add(row);
                }

                return new Report(columns, rows);
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
