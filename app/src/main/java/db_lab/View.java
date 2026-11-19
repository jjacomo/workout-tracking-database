package db_lab;

import db_lab.data.Attivita;
import db_lab.data.Attraversamento;
import db_lab.data.Attrezzatura;
import db_lab.data.TipoAttivita;
import db_lab.data.TipoAttrezzatura;
import db_lab.data.Segmento;
import db_lab.data.Club;
import db_lab.data.Iscrizione;
import db_lab.data.PosizioneAmministratore;
import db_lab.data.Utente;
import db_lab.data.Partecipazione;
import db_lab.data.TrattoGara;
import db_lab.data.Gara;
import db_lab.data.Report;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;

public final class View {
    private static final String HOME = "home";
    private static final String USERS = "users";
    private static final String ACTIVITIES = "activities";
    private static final String SEGMENTS = "segments";
    private static final String CLUBS = "clubs";
    private static final String STATISTICS = "statistics";
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final JFrame frame = new JFrame("Activity Tracker - Database");
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    private final DefaultTableModel usersModel = new DefaultTableModel(
            new Object[]{"Username", "Name", "Last name", "Email"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel activitiesModel = new DefaultTableModel(
            new Object[]{"User", "Date/time", "Type", "Duration (min)", "Distance (km)", "Elevation gain (m)"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel activityTypesModel = new DefaultTableModel(
            new Object[]{"Code", "Type", "Activities", "Total distance (km)", "Total duration (min)"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel equipmentModel = new DefaultTableModel(
            new Object[]{"Code", "Owner", "Type", "Brand", "Model", "Mileage (km)"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel equipmentTypesModel = new DefaultTableModel(
            new Object[]{"Code", "Equipment type"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel segmentsModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Distance (km)", "Elevation gain (m)", "N. attraversamenti"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel crossingsModel = new DefaultTableModel(
            new Object[]{"User", "Activity date/time", "Order", "Segment", "Time (s)"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel clubsModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Description"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel membershipsModel = new DefaultTableModel(
            new Object[]{"User", "Club", "Registration date", "Cancellation date", "Status"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel adminPositionsModel = new DefaultTableModel(
            new Object[]{"Position ID", "User", "Club", "Start date", "End date", "Status"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel racesModel = new DefaultTableModel(
            new Object[]{"Code", "Name", "Club", "Creator", "Type", "Created", "Start", "End"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel raceSegmentsModel = new DefaultTableModel(
            new Object[]{"Race", "Order", "Segment"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel participationsModel = new DefaultTableModel(
            new Object[]{"User", "Race", "Registration date", "Time (s)"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel reportModel = new DefaultTableModel() {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JLabel reportTitle = new JLabel("Select one of the OP13-OP19 operations");

    private Controller controller;

    public View(Runnable onClose) {
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                onClose.run();
                frame.dispose();
            }
        });

        root.add(buildHomePage(), HOME);
        root.add(buildUsersPage(), USERS);
        root.add(buildActivitiesPage(), ACTIVITIES);
        root.add(buildSegmentsPage(), SEGMENTS);
        root.add(buildClubsPage(), CLUBS);
        root.add(buildStatisticsPage(), STATISTICS);
        frame.setContentPane(root);
        frame.setMinimumSize(new Dimension(950, 650));
        frame.setSize(1120, 760);
        frame.setLocationRelativeTo(null);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private JPanel buildHomePage() {
        var panel = new JPanel(new BorderLayout(16, 16));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        var title = new JLabel("Activity Tracker", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
        panel.add(title, BorderLayout.NORTH);

        var buttons = new JPanel(new GridLayout(5, 1, 10, 10));

        var users = new JButton("Users");
        users.addActionListener(e -> controller.userRequestedUsersPage());
        buttons.add(users);

        var activities = new JButton("Activities and equipment");
        activities.addActionListener(e -> controller.userRequestedActivitiesPage());
        buttons.add(activities);

        var segments = new JButton("Segments");
        segments.addActionListener(e -> controller.userRequestedSegmentsPage());
        buttons.add(segments);
        var clubs = new JButton("Clubs and races");
        clubs.addActionListener(e -> controller.userRequestedClubsPage());
        buttons.add(clubs);

        var statistics = new JButton("Statistics and rankings");
        statistics.addActionListener(e -> controller.userRequestedStatisticsPage());
        buttons.add(statistics);

        var wrapper = new JPanel(new GridBagLayout());
        wrapper.add(buttons);
        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    private JButton disabledButton(String text) {
        var button = new JButton(text);
        button.setEnabled(false);
        return button;
    }

    private JPanel buildUsersPage() {
        var panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        var title = new JLabel("Users");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        panel.add(title, BorderLayout.NORTH);

        var table = new JTable(usersModel);
        table.setFillsViewportHeight(true);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        var controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        var back = new JButton("Back");
        back.addActionListener(e -> controller.userRequestedInitialPage());
        var add = new JButton("Register new user");
        add.addActionListener(e -> controller.userRequestedAddUser());
        controls.add(back);
        controls.add(add);
        panel.add(controls, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildActivitiesPage() {
        var panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        var title = new JLabel("Activities and equipment");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        panel.add(title, BorderLayout.NORTH);

        var tabs = new JTabbedPane();
        tabs.addTab("Activities", buildActivitiesTab());
        tabs.addTab("Equipment", buildEquipmentTab());
        tabs.addTab("Tipi attrezzatura", buildEquipmentTypesTab());
        panel.add(tabs, BorderLayout.CENTER);

        var controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        var back = new JButton("Back");
        back.addActionListener(e -> controller.userRequestedInitialPage());

        var addActivity = new JButton("New activity");
        addActivity.addActionListener(e -> controller.userRequestedAddActivity());

        var addEquipment = new JButton("New equipment");
        addEquipment.addActionListener(e -> controller.userRequestedAddEquipment());

        var addEquipmentType = new JButton("New equipment type");
        addEquipmentType.addActionListener(e -> controller.userRequestedAddEquipmentType());

        var associate = new JButton("Associate equipment with activity");
        associate.addActionListener(e -> controller.userRequestedAssociateEquipment());

        controls.add(back);
        controls.add(addActivity);
        controls.add(addEquipment);
        controls.add(addEquipmentType);
        controls.add(associate);
        panel.add(controls, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildSegmentsPage() {
        var panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        var title = new JLabel("Segments and crossings");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        panel.add(title, BorderLayout.NORTH);

        var segmentsTable = new JTable(segmentsModel);
        segmentsTable.setFillsViewportHeight(true);
        var segmentsScroll = new JScrollPane(segmentsTable);
        segmentsScroll.setBorder(BorderFactory.createTitledBorder(
                "Segments - numberOfCrossings is a redundant attribute"));

        var crossingsTable = new JTable(crossingsModel);
        crossingsTable.setFillsViewportHeight(true);
        var crossingsScroll = new JScrollPane(crossingsTable);
        crossingsScroll.setBorder(BorderFactory.createTitledBorder("Attraversamenti registrati"));

        var split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, segmentsScroll, crossingsScroll);
        split.setResizeWeight(0.48);
        panel.add(split, BorderLayout.CENTER);

        var controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        var back = new JButton("Back");
        back.addActionListener(e -> controller.userRequestedInitialPage());
        var addSegment = new JButton("New segment");
        addSegment.addActionListener(e -> controller.userRequestedAddSegment());
        var addCrossing = new JButton("Record crossing");
        addCrossing.addActionListener(e -> controller.userRequestedAddCrossing());
        controls.add(back);
        controls.add(addSegment);
        controls.add(addCrossing);
        panel.add(controls, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildClubsPage() {
        var panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        var title = new JLabel("Clubs and races");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        panel.add(title, BorderLayout.NORTH);

        var tabs = new JTabbedPane();
        tabs.addTab("Clubs and registrations", buildClubManagementTab());
        tabs.addTab("Races", buildRacesTab());
        panel.add(tabs, BorderLayout.CENTER);

        var controls = new JPanel(new GridLayout(2, 0, 6, 6));
        var back = new JButton("Back");
        back.addActionListener(e -> controller.userRequestedInitialPage());
        var addClub = new JButton("Create new club");
        addClub.addActionListener(e -> controller.userRequestedAddClub());
        var join = new JButton("Register user with club");
        join.addActionListener(e -> controller.userRequestedJoinClub());
        var leave = new JButton("Leave club");
        leave.addActionListener(e -> controller.userRequestedLeaveClub());
        var addRace = new JButton("Create new race");
        addRace.addActionListener(e -> controller.userRequestedAddRace());
        var joinRace = new JButton("Register user for race");
        joinRace.addActionListener(e -> controller.userRequestedJoinRace());
        var resultRace = new JButton("Record result");
        resultRace.addActionListener(e -> controller.userRequestedRecordRaceResult());

        controls.add(back);
        controls.add(addClub);
        controls.add(join);
        controls.add(leave);
        controls.add(addRace);
        controls.add(joinRace);
        controls.add(resultRace);
        panel.add(controls, BorderLayout.SOUTH);

        return panel;
    }

    private JComponent buildClubManagementTab() {
        var clubsTable = new JTable(clubsModel);
        clubsTable.setFillsViewportHeight(true);
        var clubsScroll = new JScrollPane(clubsTable);
        clubsScroll.setBorder(BorderFactory.createTitledBorder("Club"));

        var membershipsTable = new JTable(membershipsModel);
        membershipsTable.setFillsViewportHeight(true);
        var membershipsScroll = new JScrollPane(membershipsTable);
        membershipsScroll.setBorder(BorderFactory.createTitledBorder("Membership history"));

        var adminTable = new JTable(adminPositionsModel);
        adminTable.setFillsViewportHeight(true);
        var adminScroll = new JScrollPane(adminTable);
        adminScroll.setBorder(BorderFactory.createTitledBorder("Administrator position history"));

        var lower = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, membershipsScroll, adminScroll);
        lower.setResizeWeight(0.58);
        var split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, clubsScroll, lower);
        split.setResizeWeight(0.32);
        return split;
    }

    private JComponent buildRacesTab() {
        var racesTable = new JTable(racesModel);
        racesTable.setFillsViewportHeight(true);
        var racesScroll = new JScrollPane(racesTable);
        racesScroll.setBorder(BorderFactory.createTitledBorder("Races"));

        var segmentsTable = new JTable(raceSegmentsModel);
        segmentsTable.setFillsViewportHeight(true);
        var segmentsScroll = new JScrollPane(segmentsTable);
        segmentsScroll.setBorder(BorderFactory.createTitledBorder("Ordered routes - RACE_SEGMENTS"));

        var participationsTable = new JTable(participationsModel);
        participationsTable.setFillsViewportHeight(true);
        var participationsScroll = new JScrollPane(participationsTable);
        participationsScroll.setBorder(BorderFactory.createTitledBorder("Participations"));

        var lower = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, segmentsScroll, participationsScroll);
        lower.setResizeWeight(0.50);
        var split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, racesScroll, lower);
        split.setResizeWeight(0.43);
        return split;
    }

    private JPanel buildStatisticsPage() {
        var panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        var title = new JLabel("Statistics and rankings");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        panel.add(title, BorderLayout.NORTH);

        var operations = new JPanel(new GridLayout(7, 1, 8, 8));
        operations.setBorder(BorderFactory.createTitledBorder("Operations"));

        var op13 = new JButton("OP13 - User ranking by distance");
        op13.addActionListener(e -> controller.userRequestedOp13());
        var op14 = new JButton("OP14 - Best times on a segment");
        op14.addActionListener(e -> controller.userRequestedOp14());
        var op15 = new JButton("OP15 - Equipment by mileage");
        op15.addActionListener(e -> controller.userRequestedOp15());
        var op16 = new JButton("OP16 - Averages by activity type");
        op16.addActionListener(e -> controller.userRequestedOp16());
        var op17 = new JButton("OP17 - Most frequently traveled segments");
        op17.addActionListener(e -> controller.userRequestedOp17());
        var op18 = new JButton("OP18 - Clubs by distance");
        op18.addActionListener(e -> controller.userRequestedOp18());
        var op19 = new JButton("OP19 - Race ranking");
        op19.addActionListener(e -> controller.userRequestedOp19());

        operations.add(op13);
        operations.add(op14);
        operations.add(op15);
        operations.add(op16);
        operations.add(op17);
        operations.add(op18);
        operations.add(op19);

        var resultPanel = new JPanel(new BorderLayout(8, 8));
        reportTitle.setFont(reportTitle.getFont().deriveFont(Font.BOLD, 16f));
        resultPanel.add(reportTitle, BorderLayout.NORTH);
        var reportTable = new JTable(reportModel);
        reportTable.setFillsViewportHeight(true);
        reportTable.setAutoCreateRowSorter(true);
        resultPanel.add(new JScrollPane(reportTable), BorderLayout.CENTER);

        var split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, operations, resultPanel);
        split.setResizeWeight(0.30);
        panel.add(split, BorderLayout.CENTER);

        var controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        var back = new JButton("Back");
        back.addActionListener(e -> controller.userRequestedInitialPage());
        controls.add(back);
        panel.add(controls, BorderLayout.SOUTH);

        return panel;
    }

    private JComponent buildActivitiesTab() {
        var activitiesTable = new JTable(activitiesModel);
        activitiesTable.setFillsViewportHeight(true);
        var activityScroll = new JScrollPane(activitiesTable);
        activityScroll.setBorder(BorderFactory.createTitledBorder("Registered activities"));

        var typesTable = new JTable(activityTypesModel);
        typesTable.setFillsViewportHeight(true);
        var typesScroll = new JScrollPane(typesTable);
        typesScroll.setBorder(BorderFactory.createTitledBorder("TIPI_ATTIVITA - attributi ridondanti"));

        var split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, activityScroll, typesScroll);
        split.setResizeWeight(0.68);
        return split;
    }

    private JComponent buildEquipmentTab() {
        var table = new JTable(equipmentModel);
        table.setFillsViewportHeight(true);
        var scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Equipment - mileage increases when equipment is associated with activities"));
        return scroll;
    }

    private JComponent buildEquipmentTypesTab() {
        var table = new JTable(equipmentTypesModel);
        table.setFillsViewportHeight(true);
        var scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Equipment types - compatible activity types are selected at creation"));
        return scroll;
    }

    public void showInitialPage() {
        cards.show(root, HOME);
        frame.setVisible(true);
    }

    public void showUsersPage() {
        cards.show(root, USERS);
    }

    public void showActivitiesPage() {
        cards.show(root, ACTIVITIES);
    }

    public void showSegmentsPage() {
        cards.show(root, SEGMENTS);
    }

    public void showClubsPage() {
        cards.show(root, CLUBS);
    }

    public void showStatisticsPage() {
        cards.show(root, STATISTICS);
    }

    public void clearReport() {
        reportTitle.setText("Select one of the OP13-OP19 operations");
        reportModel.setDataVector(new Object[0][0], new Object[0]);
    }

    public void setReport(String title, Report report) {
        reportTitle.setText(title + " - " + report.rows.size() + " risultati");
        Object[] columns = report.columns.toArray();
        Object[][] rows = new Object[report.rows.size()][];
        for (int i = 0; i < report.rows.size(); i++) {
            rows[i] = report.rows.get(i).toArray();
        }
        reportModel.setDataVector(rows, columns);
    }

    public void setUsers(List<Utente> users) {
        usersModel.setRowCount(0);
        for (var user : users) {
            usersModel.addRow(new Object[]{user.username, user.nome, user.cognome, user.email});
        }
    }

    public void setActivities(List<Attivita> activities) {
        activitiesModel.setRowCount(0);
        for (var activity : activities) {
            activitiesModel.addRow(new Object[]{
                    activity.username,
                    activity.dataOraInizio.format(DATE_TIME_FORMAT),
                    activity.tipoAttivita,
                    activity.durata,
                    activity.distanza,
                    activity.dislivello
            });
        }
    }

    public void setActivityTypes(List<TipoAttivita> types) {
        activityTypesModel.setRowCount(0);
        for (var type : types) {
            activityTypesModel.addRow(new Object[]{
                    type.codice,
                    type.nome,
                    type.numeroAttivita,
                    type.distanzaTotale,
                    type.durataTotale
            });
        }
    }

    public void setEquipment(List<Attrezzatura> equipment) {
        equipmentModel.setRowCount(0);
        for (var item : equipment) {
            equipmentModel.addRow(new Object[]{
                    item.codice,
                    item.username,
                    item.tipoAttrezzatura,
                    item.marca,
                    item.modello,
                    item.chilometraggio
            });
        }
    }

    public void setEquipmentTypes(List<TipoAttrezzatura> types) {
        equipmentTypesModel.setRowCount(0);
        for (var type : types) {
            equipmentTypesModel.addRow(new Object[]{type.codice, type.nome});
        }
    }

    public void setSegments(List<Segmento> segments) {
        segmentsModel.setRowCount(0);
        for (var segment : segments) {
            segmentsModel.addRow(new Object[]{
                    segment.id, segment.nome, segment.distanza, segment.dislivello,
                    segment.numeroAttraversamenti
            });
        }
    }

    public void setCrossings(List<Attraversamento> crossings) {
        crossingsModel.setRowCount(0);
        for (var crossing : crossings) {
            crossingsModel.addRow(new Object[]{
                    crossing.username,
                    crossing.dataOraInizio.format(DATE_TIME_FORMAT),
                    crossing.ordine,
                    crossing.idSegmento + " - " + crossing.nomeSegmento,
                    crossing.tempoImpiegato
            });
        }
    }

    public void setClubs(List<Club> clubs) {
        clubsModel.setRowCount(0);
        for (var club : clubs) {
            clubsModel.addRow(new Object[]{club.id, club.nome, club.descrizione});
        }
    }

    public void setMemberships(List<Iscrizione> memberships) {
        membershipsModel.setRowCount(0);
        for (var membership : memberships) {
            membershipsModel.addRow(new Object[]{
                    membership.username,
                    membership.idClub + " - " + membership.nomeClub,
                    membership.dataIscrizione.format(DATE_TIME_FORMAT),
                    membership.dataDisiscrizione == null ? "" : membership.dataDisiscrizione.format(DATE_TIME_FORMAT),
                    membership.dataDisiscrizione == null ? "Active" : "Completed"
            });
        }
    }

    public void setAdminPositions(List<PosizioneAmministratore> positions) {
        adminPositionsModel.setRowCount(0);
        for (var position : positions) {
            adminPositionsModel.addRow(new Object[]{
                    position.idPosizione,
                    position.username,
                    position.idClub + " - " + position.nomeClub,
                    position.dataInizio.format(DATE_TIME_FORMAT),
                    position.dataFine == null ? "" : position.dataFine.format(DATE_TIME_FORMAT),
                    position.dataFine == null ? "Active" : "Completed"
            });
        }
    }

    public void setRaces(List<Gara> races) {
        racesModel.setRowCount(0);
        for (var race : races) {
            racesModel.addRow(new Object[]{
                    race.codice, race.nome, race.idClub + " - " + race.nomeClub,
                    race.creatore, race.tipoAttivita,
                    race.dataCreazione.format(DATE_TIME_FORMAT),
                    race.dataInizio.format(DATE_TIME_FORMAT),
                    race.dataFine.format(DATE_TIME_FORMAT)
            });
        }
    }

    public void setRaceSegments(List<TrattoGara> segments) {
        raceSegmentsModel.setRowCount(0);
        for (var segment : segments) {
            raceSegmentsModel.addRow(new Object[]{
                    segment.codiceGara + " - " + segment.nomeGara,
                    segment.ordine,
                    segment.idSegmento + " - " + segment.nomeSegmento
            });
        }
    }

    public void setParticipations(List<Partecipazione> participations) {
        participationsModel.setRowCount(0);
        for (var participation : participations) {
            participationsModel.addRow(new Object[]{
                    participation.username,
                    participation.codiceGara + " - " + participation.nomeGara,
                    participation.dataIscrizione.format(DATE_TIME_FORMAT),
                    participation.tempoImpiegato == null ? "" : participation.tempoImpiegato
            });
        }
    }

    public NewUserData askNewUser() {
        var username = new JTextField(20);
        var password = new JPasswordField(20);
        var nome = new JTextField(20);
        var cognome = new JTextField(20);
        var email = new JTextField(20);

        var panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Username:")); panel.add(username);
        panel.add(new JLabel("Password:")); panel.add(password);
        panel.add(new JLabel("First name:")); panel.add(nome);
        panel.add(new JLabel("Last name:")); panel.add(cognome);
        panel.add(new JLabel("Email:")); panel.add(email);

        int result = JOptionPane.showConfirmDialog(frame, panel, "New user",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        var data = new NewUserData(
                username.getText().trim(),
                new String(password.getPassword()),
                nome.getText().trim(),
                cognome.getText().trim(),
                email.getText().trim());

        if (data.username().isBlank() || data.password().isBlank() || data.nome().isBlank()
                || data.cognome().isBlank() || data.email().isBlank()) {
            showError("All fields are required.");
            return null;
        }
        return data;
    }

    public NewActivityData askNewActivity(List<Utente> users, List<TipoAttivita> types) {
        if (users.isEmpty()) {
            showError("At least one user must be created before registering an activity.");
            return null;
        }
        if (types.isEmpty()) {
            showError("No activity types are available in the database.");
            return null;
        }

        var user = new JComboBox<>(users.toArray(Utente[]::new));
        var type = new JComboBox<>(types.toArray(TipoAttivita[]::new));
        var dateTime = new JTextField(LocalDateTime.now().withSecond(0).withNano(0).format(DATE_TIME_FORMAT), 20);
        var duration = new JTextField(20);
        var distance = new JTextField(20);
        var elevation = new JTextField(20);

        var panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("User:")); panel.add(user);
        panel.add(new JLabel("Date/time (yyyy-MM-dd HH:mm):")); panel.add(dateTime);
        panel.add(new JLabel("Activity type:")); panel.add(type);
        panel.add(new JLabel("Duration (minutes):")); panel.add(duration);
        panel.add(new JLabel("Distance (km):")); panel.add(distance);
        panel.add(new JLabel("Elevation gain (m):")); panel.add(elevation);

        int result = JOptionPane.showConfirmDialog(frame, panel, "New activity",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            var parsedDateTime = LocalDateTime.parse(dateTime.getText().trim(), DATE_TIME_FORMAT);
            int parsedDuration = Integer.parseInt(duration.getText().trim());
            double parsedDistance = Double.parseDouble(distance.getText().trim().replace(',', '.'));
            int parsedElevation = Integer.parseInt(elevation.getText().trim());

            if (parsedDuration <= 0 || parsedDistance < 0 || parsedElevation < 0) {
                showError("Duration must be positive; distance and elevation gain cannot be negative.");
                return null;
            }

            return new NewActivityData(
                    (Utente) user.getSelectedItem(),
                    parsedDateTime,
                    parsedDuration,
                    parsedDistance,
                    parsedElevation,
                    (TipoAttivita) type.getSelectedItem());
        } catch (DateTimeParseException e) {
            showError("Invalid date/time. Use the yyyy-MM-dd HH:mm format.");
            return null;
        } catch (NumberFormatException e) {
            showError("Duration, distance, and elevation gain must be valid numeric values.");
            return null;
        }
    }

    public NewEquipmentData askNewEquipment(List<Utente> users, List<TipoAttrezzatura> types) {
        if (users.isEmpty()) {
            showError("At least one user must be created before registering equipment.");
            return null;
        }
        if (types.isEmpty()) {
            showError("Prima di registrare un'attrezzatura devi creare almeno un tipo di attrezzatura.");
            return null;
        }

        var code = new JTextField(20);
        var brand = new JTextField(20);
        var model = new JTextField(20);
        var user = new JComboBox<>(users.toArray(Utente[]::new));
        var type = new JComboBox<>(types.toArray(TipoAttrezzatura[]::new));

        var panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Codice attrezzatura:")); panel.add(code);
        panel.add(new JLabel("Brand:")); panel.add(brand);
        panel.add(new JLabel("Model:")); panel.add(model);
        panel.add(new JLabel("Owner:")); panel.add(user);
        panel.add(new JLabel("Tipo attrezzatura:")); panel.add(type);

        int result = JOptionPane.showConfirmDialog(frame, panel, "New equipment",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            int parsedCode = Integer.parseInt(code.getText().trim());
            if (parsedCode <= 0 || brand.getText().trim().isBlank() || model.getText().trim().isBlank()) {
                showError("Il codice deve essere positivo e marca/modello sono obbligatori.");
                return null;
            }
            return new NewEquipmentData(
                    parsedCode,
                    brand.getText().trim(),
                    model.getText().trim(),
                    (Utente) user.getSelectedItem(),
                    (TipoAttrezzatura) type.getSelectedItem());
        } catch (NumberFormatException e) {
            showError("Il codice attrezzatura deve essere un numero intero.");
            return null;
        }
    }

    public NewEquipmentTypeData askNewEquipmentType(List<TipoAttivita> activityTypes) {
        if (activityTypes.isEmpty()) {
            showError("No activity types are available.");
            return null;
        }

        var code = new JTextField(20);
        var name = new JTextField(20);
        var compatible = new JList<>(activityTypes.toArray(TipoAttivita[]::new));
        compatible.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        compatible.setVisibleRowCount(Math.min(6, activityTypes.size()));

        var panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Codice tipo:")); panel.add(code);
        panel.add(new JLabel("First name:")); panel.add(name);
        panel.add(new JLabel("Compatibile con:")); panel.add(new JScrollPane(compatible));

        int result = JOptionPane.showConfirmDialog(frame, panel, "New equipment type",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            int parsedCode = Integer.parseInt(code.getText().trim());
            var selected = compatible.getSelectedValuesList();
            if (parsedCode <= 0 || name.getText().trim().isBlank() || selected.isEmpty()) {
                showError("Code and name are required, and at least one compatible activity type must be selected.");
                return null;
            }
            return new NewEquipmentTypeData(parsedCode, name.getText().trim(), selected);
        } catch (NumberFormatException e) {
            showError("Il codice del tipo deve essere un numero intero.");
            return null;
        }
    }

    public EquipmentUseData askEquipmentUse(List<Attivita> activities, List<Attrezzatura> equipment) {
        if (activities.isEmpty()) {
            showError("There are no activities to associate with equipment.");
            return null;
        }
        if (equipment.isEmpty()) {
            showError("Non ci sono attrezzature registrate.");
            return null;
        }

        var activity = new JComboBox<>(activities.toArray(Attivita[]::new));
        var item = new JComboBox<>(equipment.toArray(Attrezzatura[]::new));

        var panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.add(new JLabel("Activity:"));
        panel.add(activity);
        panel.add(new JLabel("Equipment:"));
        panel.add(item);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Associate equipment with activity",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        return new EquipmentUseData(
                (Attivita) activity.getSelectedItem(),
                (Attrezzatura) item.getSelectedItem());
    }

    public NewSegmentData askNewSegment() {
        var id = new JTextField(20);
        var name = new JTextField(20);
        var distance = new JTextField(20);
        var elevation = new JTextField(20);

        var panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("ID segmento:")); panel.add(id);
        panel.add(new JLabel("First name:")); panel.add(name);
        panel.add(new JLabel("Distance (km):")); panel.add(distance);
        panel.add(new JLabel("Elevation gain (m):")); panel.add(elevation);

        int result = JOptionPane.showConfirmDialog(frame, panel, "New segment",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            int parsedId = Integer.parseInt(id.getText().trim());
            double parsedDistance = Double.parseDouble(distance.getText().trim().replace(',', '.'));
            int parsedElevation = Integer.parseInt(elevation.getText().trim());
            String parsedName = name.getText().trim();

            if (parsedId <= 0 || parsedName.isBlank() || parsedDistance <= 0 || parsedElevation < 0) {
                showError("ID and distance must be positive, name is required, and elevation gain cannot be negative.");
                return null;
            }
            return new NewSegmentData(parsedId, parsedName, parsedDistance, parsedElevation);
        } catch (NumberFormatException e) {
            showError("ID, distanza e dislivello devono essere valori numerici validi.");
            return null;
        }
    }

    public NewCrossingData askNewCrossing(List<Attivita> activities, List<Segmento> segments) {
        if (activities.isEmpty()) {
            showError("At least one activity is required before recording a crossing.");
            return null;
        }
        if (segments.isEmpty()) {
            showError("At least one segment must be created before recording a crossing.");
            return null;
        }

        var activity = new JComboBox<>(activities.toArray(Attivita[]::new));
        var segment = new JComboBox<>(segments.toArray(Segmento[]::new));
        var order = new JTextField(20);
        var time = new JTextField(20);

        var panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Activity:")); panel.add(activity);
        panel.add(new JLabel("Segment:")); panel.add(segment);
        panel.add(new JLabel("Order within the activity:")); panel.add(order);
        panel.add(new JLabel("Elapsed time (seconds):")); panel.add(time);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Record crossing",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            int parsedOrder = Integer.parseInt(order.getText().trim());
            int parsedTime = Integer.parseInt(time.getText().trim());
            if (parsedOrder <= 0 || parsedTime <= 0) {
                showError("Ordine e tempo impiegato devono essere numeri interi positivi.");
                return null;
            }
            return new NewCrossingData(
                    (Attivita) activity.getSelectedItem(),
                    (Segmento) segment.getSelectedItem(),
                    parsedOrder, parsedTime);
        } catch (NumberFormatException e) {
            showError("Ordine e tempo impiegato devono essere numeri interi.");
            return null;
        }
    }

    public NewClubData askNewClub(List<Utente> users) {
        if (users.isEmpty()) {
            showError("At least one user must be registered before creating a club.");
            return null;
        }

        var idClub = new JTextField(20);
        var name = new JTextField(20);
        var description = new JTextField(20);
        var creator = new JComboBox<>(users.toArray(Utente[]::new));
        var idPosition = new JTextField(20);
        var dateTime = new JTextField(
                LocalDateTime.now().withSecond(0).withNano(0).format(DATE_TIME_FORMAT), 20);

        var panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Club ID:")); panel.add(idClub);
        panel.add(new JLabel("First name:")); panel.add(name);
        panel.add(new JLabel("Description:")); panel.add(description);
        panel.add(new JLabel("Creator:")); panel.add(creator);
        panel.add(new JLabel("Administrator position ID:")); panel.add(idPosition);
        panel.add(new JLabel("Creation date/time:")); panel.add(dateTime);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Create new club",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            int parsedClubId = Integer.parseInt(idClub.getText().trim());
            int parsedPositionId = Integer.parseInt(idPosition.getText().trim());
            var parsedDateTime = LocalDateTime.parse(dateTime.getText().trim(), DATE_TIME_FORMAT);
            var parsedName = name.getText().trim();
            var parsedDescription = description.getText().trim();

            if (parsedClubId <= 0 || parsedPositionId <= 0
                    || parsedName.isBlank() || parsedDescription.isBlank()) {
                showError("Identifiers must be positive and name/description are required.");
                return null;
            }

            return new NewClubData(
                    parsedClubId, parsedName, parsedDescription,
                    (Utente) creator.getSelectedItem(), parsedPositionId, parsedDateTime);
        } catch (NumberFormatException e) {
            showError("Club ID and position ID must be integers.");
            return null;
        } catch (DateTimeParseException e) {
            showError("Invalid date/time. Use the yyyy-MM-dd HH:mm format.");
            return null;
        }
    }

    public JoinClubData askJoinClub(List<Utente> users, List<Club> clubs) {
        if (users.isEmpty() || clubs.isEmpty()) {
            showError("At least one user and one club must exist to register a membership.");
            return null;
        }

        var user = new JComboBox<>(users.toArray(Utente[]::new));
        var club = new JComboBox<>(clubs.toArray(Club[]::new));
        var dateTime = new JTextField(
                LocalDateTime.now().withSecond(0).withNano(0).format(DATE_TIME_FORMAT), 20);

        var panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("User:")); panel.add(user);
        panel.add(new JLabel("Club:")); panel.add(club);
        panel.add(new JLabel("Registration date/time:")); panel.add(dateTime);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Register user with club",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            return new JoinClubData(
                    (Utente) user.getSelectedItem(),
                    (Club) club.getSelectedItem(),
                    LocalDateTime.parse(dateTime.getText().trim(), DATE_TIME_FORMAT));
        } catch (DateTimeParseException e) {
            showError("Invalid date/time. Use the yyyy-MM-dd HH:mm format.");
            return null;
        }
    }

    public LeaveClubData askLeaveClub(List<Utente> users, List<Club> clubs) {
        if (users.isEmpty() || clubs.isEmpty()) {
            showError("At least one user and one club must exist to record a departure.");
            return null;
        }

        var user = new JComboBox<>(users.toArray(Utente[]::new));
        var club = new JComboBox<>(clubs.toArray(Club[]::new));
        var dateTime = new JTextField(
                LocalDateTime.now().withSecond(0).withNano(0).format(DATE_TIME_FORMAT), 20);

        var panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("User:")); panel.add(user);
        panel.add(new JLabel("Club:")); panel.add(club);
        panel.add(new JLabel("Departure date/time:")); panel.add(dateTime);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Leave club",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            return new LeaveClubData(
                    (Utente) user.getSelectedItem(),
                    (Club) club.getSelectedItem(),
                    LocalDateTime.parse(dateTime.getText().trim(), DATE_TIME_FORMAT));
        } catch (DateTimeParseException e) {
            showError("Invalid date/time. Use the yyyy-MM-dd HH:mm format.");
            return null;
        }
    }

    public NewRaceData askNewRace(List<Utente> users, List<Club> clubs,
                                   List<TipoAttivita> types, List<Segmento> segments) {
        if (users.isEmpty() || clubs.isEmpty() || types.isEmpty() || segments.isEmpty()) {
            showError("Creating a race requires at least one user, one club, one activity type, and one segment.");
            return null;
        }

        var code = new JTextField(20);
        var name = new JTextField(20);
        var creator = new JComboBox<>(users.toArray(Utente[]::new));
        var club = new JComboBox<>(clubs.toArray(Club[]::new));
        var type = new JComboBox<>(types.toArray(TipoAttivita[]::new));
        var creation = new JTextField(
                LocalDateTime.now().withSecond(0).withNano(0).format(DATE_TIME_FORMAT), 20);
        var start = new JTextField(
                LocalDateTime.now().plusDays(7).withSecond(0).withNano(0).format(DATE_TIME_FORMAT), 20);
        var end = new JTextField(
                LocalDateTime.now().plusDays(7).plusHours(2).withSecond(0).withNano(0).format(DATE_TIME_FORMAT), 20);
        var path = new JTextField(20);

        StringBuilder available = new StringBuilder();
        for (var segment : segments) {
            if (!available.isEmpty()) {
                available.append("; ");
            }
            available.append(segment.id).append("=").append(segment.nome);
        }
        var segmentInfo = new JTextArea(available.toString(), 3, 28);
        segmentInfo.setEditable(false);
        segmentInfo.setLineWrap(true);
        segmentInfo.setWrapStyleWord(true);

        var panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Race code:")); panel.add(code);
        panel.add(new JLabel("First name:")); panel.add(name);
        panel.add(new JLabel("Creator:")); panel.add(creator);
        panel.add(new JLabel("Club:")); panel.add(club);
        panel.add(new JLabel("Activity type:")); panel.add(type);
        panel.add(new JLabel("Creation date:")); panel.add(creation);
        panel.add(new JLabel("Start date:")); panel.add(start);
        panel.add(new JLabel("End date:")); panel.add(end);
        panel.add(new JLabel("Available segments:")); panel.add(new JScrollPane(segmentInfo));
        panel.add(new JLabel("Route (comma-separated IDs):")); panel.add(path);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Create new race",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            int parsedCode = Integer.parseInt(code.getText().trim());
            String parsedName = name.getText().trim();
            var parsedCreation = LocalDateTime.parse(creation.getText().trim(), DATE_TIME_FORMAT);
            var parsedStart = LocalDateTime.parse(start.getText().trim(), DATE_TIME_FORMAT);
            var parsedEnd = LocalDateTime.parse(end.getText().trim(), DATE_TIME_FORMAT);

            if (parsedCode <= 0 || parsedName.isBlank()) {
                showError("Race code must be positive and name is required.");
                return null;
            }
            if (parsedEnd.isBefore(parsedStart)) {
                showError("The end date cannot precede the start date.");
                return null;
            }
            if (path.getText().trim().isBlank()) {
                showError("Il percorso deve contenere almeno un segmento.");
                return null;
            }

            var parsedSegments = new ArrayList<Segmento>();
            for (String token : path.getText().split(",")) {
                int id = Integer.parseInt(token.trim());
                Segmento found = null;
                for (var segment : segments) {
                    if (segment.id == id) {
                        found = segment;
                        break;
                    }
                }
                if (found == null) {
                    showError("Il segmento con ID " + id + " non esiste.");
                    return null;
                }
                parsedSegments.add(found);
            }

            return new NewRaceData(
                    parsedCode, parsedName, parsedCreation, parsedStart, parsedEnd,
                    (Utente) creator.getSelectedItem(),
                    (Club) club.getSelectedItem(),
                    (TipoAttivita) type.getSelectedItem(),
                    parsedSegments);
        } catch (NumberFormatException e) {
            showError("Race code and route IDs must be integers.");
            return null;
        } catch (DateTimeParseException e) {
            showError("Invalid date/time. Use the yyyy-MM-dd HH:mm format.");
            return null;
        }
    }

    public JoinRaceData askJoinRace(List<Utente> users, List<Gara> races) {
        if (users.isEmpty() || races.isEmpty()) {
            showError("At least one user and one race must exist to register for a race.");
            return null;
        }

        var user = new JComboBox<>(users.toArray(Utente[]::new));
        var race = new JComboBox<>(races.toArray(Gara[]::new));
        var dateTime = new JTextField(
                LocalDateTime.now().withSecond(0).withNano(0).format(DATE_TIME_FORMAT), 20);

        var panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("User:")); panel.add(user);
        panel.add(new JLabel("Race:")); panel.add(race);
        panel.add(new JLabel("Registration date/time:")); panel.add(dateTime);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Register user for race",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            return new JoinRaceData(
                    (Utente) user.getSelectedItem(),
                    (Gara) race.getSelectedItem(),
                    LocalDateTime.parse(dateTime.getText().trim(), DATE_TIME_FORMAT));
        } catch (DateTimeParseException e) {
            showError("Invalid date/time. Use the yyyy-MM-dd HH:mm format.");
            return null;
        }
    }

    public RaceResultData askRaceResult(List<Partecipazione> participations) {
        if (participations.isEmpty()) {
            showError("There are no participations to which a result can be assigned.");
            return null;
        }

        var participation = new JComboBox<>(participations.toArray(Partecipazione[]::new));
        var time = new JTextField(20);

        var panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Participation:")); panel.add(participation);
        panel.add(new JLabel("Elapsed time (seconds):")); panel.add(time);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Record race result",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            int parsedTime = Integer.parseInt(time.getText().trim());
            if (parsedTime <= 0) {
                showError("Time must be greater than zero.");
                return null;
            }
            return new RaceResultData(
                    (Partecipazione) participation.getSelectedItem(), parsedTime);
        } catch (NumberFormatException e) {
            showError("Time must be an integer number of seconds.");
            return null;
        }
    }

    public DateRangeData askDateRange(String title) {
        var start = new JTextField(
                LocalDateTime.now().minusDays(30).withSecond(0).withNano(0).format(DATE_TIME_FORMAT), 20);
        var end = new JTextField(
                LocalDateTime.now().withSecond(0).withNano(0).format(DATE_TIME_FORMAT), 20);

        var panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Start date/time:")); panel.add(start);
        panel.add(new JLabel("End date/time:")); panel.add(end);

        int result = JOptionPane.showConfirmDialog(frame, panel, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            var parsedStart = LocalDateTime.parse(start.getText().trim(), DATE_TIME_FORMAT);
            var parsedEnd = LocalDateTime.parse(end.getText().trim(), DATE_TIME_FORMAT);
            if (parsedEnd.isBefore(parsedStart)) {
                showError("The end date cannot precede the start date.");
                return null;
            }
            return new DateRangeData(parsedStart, parsedEnd);
        } catch (DateTimeParseException e) {
            showError("Invalid date/time. Use the yyyy-MM-dd HH:mm format.");
            return null;
        }
    }

    public Segmento askSegmentForReport(List<Segmento> segments, String title) {
        if (segments.isEmpty()) {
            showError("There are no segments in the database.");
            return null;
        }
        return (Segmento) JOptionPane.showInputDialog(
                frame, "Segment:", title, JOptionPane.PLAIN_MESSAGE, null,
                segments.toArray(Segmento[]::new), segments.getFirst());
    }

    public Gara askRaceForReport(List<Gara> races, String title) {
        if (races.isEmpty()) {
            showError("There are no races in the database.");
            return null;
        }
        return (Gara) JOptionPane.showInputDialog(
                frame, "Race:", title, JOptionPane.PLAIN_MESSAGE, null,
                races.toArray(Gara[]::new), races.getFirst());
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Activity Tracker", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public record NewUserData(String username, String password, String nome, String cognome, String email) { }
    public record NewActivityData(Utente utente, LocalDateTime dataOraInizio, int durata,
                                  double distanza, int dislivello, TipoAttivita tipoAttivita) { }
    public record NewEquipmentData(int codice, String marca, String modello,
                                   Utente utente, TipoAttrezzatura tipoAttrezzatura) { }
    public record NewEquipmentTypeData(int codice, String nome,
                                       List<TipoAttivita> tipiAttivitaCompatibili) { }
    public record EquipmentUseData(Attivita attivita, Attrezzatura attrezzatura) { }
    public record NewSegmentData(int idSegmento, String nome, double distanza, int dislivello) { }
    public record NewCrossingData(Attivita attivita, Segmento segmento,
                                  int ordine, int tempoImpiegato) { }
    public record NewClubData(int idClub, String nome, String descrizione, Utente creatore,
                              int idPosizione, LocalDateTime dataCreazione) { }
    public record JoinClubData(Utente utente, Club club, LocalDateTime dataIscrizione) { }
    public record LeaveClubData(Utente utente, Club club, LocalDateTime dataDisiscrizione) { }
    public record NewRaceData(int codiceGara, String nome, LocalDateTime dataCreazione,
                              LocalDateTime dataInizio, LocalDateTime dataFine,
                              Utente creatore, Club club, TipoAttivita tipoAttivita,
                              List<Segmento> segmenti) { }
    public record JoinRaceData(Utente utente, Gara gara, LocalDateTime dataIscrizione) { }
    public record RaceResultData(Partecipazione partecipazione, int tempoImpiegato) { }
    public record DateRangeData(LocalDateTime inizio, LocalDateTime fine) { }
}
