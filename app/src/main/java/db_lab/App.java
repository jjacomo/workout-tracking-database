package db_lab;

import db_lab.data.DAOException;
import db_lab.data.DAOUtils;
import db_lab.model.Model;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public final class App {
    private static final String DATABASE = "workout_tracking_db";
    // private static final String DEFAULT_USER = "root";

    public static void main(String[] args) {
        // String user = JOptionPane.showInputDialog(
        // null,
        // "Utente MariaDB/MySQL:",
        // DEFAULT_USER);
        //
        // if (user == null || user.isBlank()) {
        // return;
        // }
        String user = "root";

        // var passwordField = new JPasswordField();
        // int result = JOptionPane.showConfirmDialog(
        // null,
        // passwordField,
        // "Password MariaDB/MySQL (utente " + user + ")",
        // JOptionPane.OK_CANCEL_OPTION,
        // JOptionPane.PLAIN_MESSAGE);
        //
        // if (result != JOptionPane.OK_OPTION) {
        // return;
        // }

        // String password = new String(passwordField.getPassword());
        String password = "root";

        try {
            var connection = DAOUtils.localMySQLConnection(DATABASE, user, password);
            var model = Model.fromConnection(connection);
            var view = new View(() -> {
                try {
                    connection.close();
                } catch (Exception ignored) {
                }
            });
            var controller = new Controller(model, view);
            view.setController(controller);
            controller.userRequestedInitialPage();
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Impossibile connettersi al database '" + DATABASE + "'.\n\n" +
                            "Controlla che MariaDB sia avviato e che gli script SQL richiesti siano stati eseguiti " +
                            "e che la password sia corretta.\n\nDettaglio: " + e.getMessage(),
                    "Errore di connessione",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
