package db_lab;

import db_lab.data.DAOException;
import db_lab.model.Model;

public final class Controller {
    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void userRequestedInitialPage() {
        view.showInitialPage();
    }

    public void userRequestedUsersPage() {
        refreshUsers();
        view.showUsersPage();
    }

    public void userRequestedAddUser() {
        var data = view.askNewUser();
        if (data == null) {
            return;
        }

        try {
            model.addUtente(data.username(), data.password(), data.nome(), data.cognome(), data.email());
            refreshUsers();
            view.showMessage("User registered successfully.");
        } catch (DAOException e) {
            view.showError("Unable to register user:\n" + e.getMessage());
        }
    }

    public void userRequestedActivitiesPage() {
        refreshActivitiesAndEquipment();
        view.showActivitiesPage();
    }

    public void userRequestedAddActivity() {
        try {
            var users = model.listUtenti();
            var types = model.listTipiAttivita();
            var data = view.askNewActivity(users, types);
            if (data == null) {
                return;
            }

            model.addAttivita(
                    data.utente().username,
                    data.dataOraInizio(),
                    data.durata(),
                    data.distanza(),
                    data.dislivello(),
                    data.tipoAttivita().codice);

            refreshActivitiesAndEquipment();
            view.showMessage("Activity registered successfully.");
        } catch (DAOException e) {
            view.showError("Unable to register the activity:\n" + e.getMessage());
        }
    }

    public void userRequestedAddEquipment() {
        try {
            var users = model.listUtenti();
            var types = model.listTipiAttrezzatura();
            var data = view.askNewEquipment(users, types);
            if (data == null) {
                return;
            }

            model.addAttrezzatura(
                    data.codice(),
                    data.marca(),
                    data.modello(),
                    data.utente().username,
                    data.tipoAttrezzatura().codice);

            refreshActivitiesAndEquipment();
            view.showMessage("Equipment registered successfully.");
        } catch (DAOException e) {
            view.showError("Unable to register equipment:\n" + e.getMessage());
        }
    }

    public void userRequestedAddEquipmentType() {
        try {
            var activityTypes = model.listTipiAttivita();
            var data = view.askNewEquipmentType(activityTypes);
            if (data == null) {
                return;
            }

            model.addTipoAttrezzatura(
                    data.codice(),
                    data.nome(),
                    data.tipiAttivitaCompatibili().stream().map(t -> t.codice).toList());

            refreshActivitiesAndEquipment();
            view.showMessage("Equipment type registered successfully.");
        } catch (DAOException e) {
            view.showError("Unable to register equipment type:\n" + e.getMessage());
        }
    }

    public void userRequestedAssociateEquipment() {
        try {
            var activities = model.listAttivita();
            var equipment = model.listAttrezzature();
            var data = view.askEquipmentUse(activities, equipment);
            if (data == null) {
                return;
            }

            model.addUtilizzo(
                    data.attivita().username,
                    data.attivita().dataOraInizio,
                    data.attrezzatura().codice);

            refreshActivitiesAndEquipment();
            view.showMessage("Equipment associated with the activity. Mileage updated.");
        } catch (DAOException e) {
            view.showError("Unable to associate equipment:\n" + e.getMessage());
        }
    }

    public void userRequestedSegmentsPage() {
        refreshSegments();
        view.showSegmentsPage();
    }

    public void userRequestedAddSegment() {
        var data = view.askNewSegment();
        if (data == null) {
            return;
        }

        try {
            model.addSegmento(data.idSegmento(), data.nome(), data.distanza(), data.dislivello());
            refreshSegments();
            view.showMessage("Segment registered successfully.");
        } catch (DAOException e) {
            view.showError("Unable to register segment:\n" + e.getMessage());
        }
    }

    public void userRequestedAddCrossing() {
        try {
            var activities = model.listAttivita();
            var segments = model.listSegmenti();
            var data = view.askNewCrossing(activities, segments);
            if (data == null) {
                return;
            }

            model.addAttraversamento(
                    data.attivita().username,
                    data.attivita().dataOraInizio,
                    data.ordine(),
                    data.tempoImpiegato(),
                    data.segmento().id);

            refreshSegments();
            view.showMessage("Crossing recorded. Crossing count updated.");
        } catch (DAOException e) {
            view.showError("Unable to record crossing:\n" + e.getMessage());
        }
    }

    public void userRequestedClubsPage() {
        refreshClubs();
        view.showClubsPage();
    }

    public void userRequestedAddClub() {
        try {
            var users = model.listUtenti();
            var data = view.askNewClub(users);
            if (data == null) {
                return;
            }

            model.addClub(
                    data.idClub(),
                    data.nome(),
                    data.descrizione(),
                    data.creatore().username,
                    data.idPosizione(),
                    data.dataCreazione());

            refreshClubs();
            view.showMessage("Club created. The creator was registered as a member and administrator.");
        } catch (DAOException e) {
            view.showError("Unable to create club:\n" + e.getMessage());
        }
    }

    public void userRequestedJoinClub() {
        try {
            var users = model.listUtenti();
            var clubs = model.listClub();
            var data = view.askJoinClub(users, clubs);
            if (data == null) {
                return;
            }

            model.addIscrizione(
                    data.utente().username,
                    data.club().id,
                    data.dataIscrizione());

            refreshClubs();
            view.showMessage("Club registration recorded successfully.");
        } catch (DAOException e) {
            view.showError("Unable to register user with club:\n" + e.getMessage());
        }
    }

    public void userRequestedLeaveClub() {
        try {
            var users = model.listUtenti();
            var clubs = model.listClub();
            var data = view.askLeaveClub(users, clubs);
            if (data == null) {
                return;
            }

            model.leaveClub(
                    data.utente().username,
                    data.club().id,
                    data.dataDisiscrizione());

            refreshClubs();
            view.showMessage("Club departure recorded successfully.");
        } catch (DAOException e) {
            view.showError("Unable to record club departure:\n" + e.getMessage());
        }
    }

    public void userRequestedAddRace() {
        try {
            var users = model.listUtenti();
            var clubs = model.listClub();
            var types = model.listTipiAttivita();
            var segments = model.listSegmenti();
            var data = view.askNewRace(users, clubs, types, segments);
            if (data == null) {
                return;
            }

            model.addGara(
                    data.codiceGara(),
                    data.nome(),
                    data.dataCreazione(),
                    data.dataInizio(),
                    data.dataFine(),
                    data.creatore().username,
                    data.club().id,
                    data.tipoAttivita().codice,
                    data.segmenti().stream().map(s -> s.id).toList());

            refreshClubs();
            view.showMessage("Race created successfully with its route.");
        } catch (DAOException e) {
            view.showError("Unable to create race:\n" + e.getMessage());
        }
    }

    public void userRequestedJoinRace() {
        try {
            var users = model.listUtenti();
            var races = model.listGare();
            var data = view.askJoinRace(users, races);
            if (data == null) {
                return;
            }

            model.addPartecipazione(
                    data.utente().username,
                    data.gara().codice,
                    data.dataIscrizione());

            refreshClubs();
            view.showMessage("Race participation registered successfully.");
        } catch (DAOException e) {
            view.showError("Unable to register user for race:\n" + e.getMessage());
        }
    }

    public void userRequestedRecordRaceResult() {
        try {
            var participations = model.listPartecipazioni();
            var data = view.askRaceResult(participations);
            if (data == null) {
                return;
            }

            model.registraRisultato(
                    data.partecipazione().username,
                    data.partecipazione().codiceGara,
                    data.tempoImpiegato());

            refreshClubs();
            view.showMessage("Race result recorded successfully.");
        } catch (DAOException e) {
            view.showError("Unable to record race result:\n" + e.getMessage());
        }
    }

    public void userRequestedStatisticsPage() {
        view.clearReport();
        view.showStatisticsPage();
    }

    public void userRequestedOp13() {
        var range = view.askDateRange("OP13 - User ranking by distance");
        if (range == null) return;
        try {
            view.setReport("OP13 - User ranking by distance",
                    model.op13ClassificaDistanzaUtenti(range.inizio(), range.fine()));
        } catch (DAOException e) {
            view.showError("Unable to execute OP13:\n" + e.getMessage());
        }
    }

    public void userRequestedOp14() {
        try {
            var segment = view.askSegmentForReport(model.listSegmenti(),
                    "OP14 - Best times on a segment");
            if (segment == null) return;
            view.setReport("OP14 - Best times: " + segment.nome,
                    model.op14MiglioriTempiSegmento(segment.id));
        } catch (DAOException e) {
            view.showError("Unable to execute OP14:\n" + e.getMessage());
        }
    }

    public void userRequestedOp15() {
        try {
            view.setReport("OP15 - Equipment by mileage",
                    model.op15AttrezzaturePerChilometraggio());
        } catch (DAOException e) {
            view.showError("Unable to execute OP15:\n" + e.getMessage());
        }
    }

    public void userRequestedOp16() {
        try {
            view.setReport("OP16 - Averages by activity type",
                    model.op16MediePerTipoAttivita());
        } catch (DAOException e) {
            view.showError("Unable to execute OP16:\n" + e.getMessage());
        }
    }

    public void userRequestedOp17() {
        try {
            view.setReport("OP17 - Most frequently traveled segments",
                    model.op17SegmentiPiuPercorsi());
        } catch (DAOException e) {
            view.showError("Unable to execute OP17:\n" + e.getMessage());
        }
    }

    public void userRequestedOp18() {
        var range = view.askDateRange("OP18 - Club ranking by distance");
        if (range == null) return;
        try {
            view.setReport("OP18 - Clubs by distance covered",
                    model.op18ClassificaClub(range.inizio(), range.fine()));
        } catch (DAOException e) {
            view.showError("Unable to execute OP18:\n" + e.getMessage());
        }
    }

    public void userRequestedOp19() {
        try {
            var race = view.askRaceForReport(model.listGare(),
                    "OP19 - Participants ranking for a race");
            if (race == null) return;
            view.setReport("OP19 - Ranking: " + race.nome,
                    model.op19ClassificaGara(race.codice));
        } catch (DAOException e) {
            view.showError("Unable to execute OP19:\n" + e.getMessage());
        }
    }

    private void refreshClubs() {
        try {
            view.setClubs(model.listClub());
            view.setMemberships(model.listIscrizioni());
            view.setAdminPositions(model.listPosizioniAmministratore());
            view.setRaces(model.listGare());
            view.setRaceSegments(model.listTrattiGara());
            view.setParticipations(model.listPartecipazioni());
        } catch (DAOException e) {
            view.showError("Unable to read clubs, races, and registrations:\n" + e.getMessage());
        }
    }

    private void refreshSegments() {
        try {
            view.setSegments(model.listSegmenti());
            view.setCrossings(model.listAttraversamenti());
        } catch (DAOException e) {
            view.showError("Unable to read segments and crossings:\n" + e.getMessage());
        }
    }

    private void refreshUsers() {
        try {
            view.setUsers(model.listUtenti());
        } catch (DAOException e) {
            view.showError("Unable to read users:\n" + e.getMessage());
        }
    }

    private void refreshActivitiesAndEquipment() {
        try {
            view.setActivities(model.listAttivita());
            view.setActivityTypes(model.listTipiAttivita());
            view.setEquipment(model.listAttrezzature());
            view.setEquipmentTypes(model.listTipiAttrezzatura());
        } catch (DAOException e) {
            view.showError("Unable to read activities and equipment:\n" + e.getMessage());
        }
    }
}
