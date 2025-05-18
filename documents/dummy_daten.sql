-- 0. Damit falls schon Daten vorhanden, die Datenbank zurückgesetzt wird 
TRUNCATE TABLE
    urlaubsantrag,
    urlaubsstatus,
    mitarbeiterdaten,
    mitarbeiter_team,
    team,
    mitarbeiter,
    admin_role
RESTART IDENTITY CASCADE;

-- 1. Admin-Rollen
INSERT INTO admin_role (rolle_name) VALUES
('Kein Admin'),
('Team-Admin'),
('Bereichs-Admin'),
('Super-Admin');

-- 2. Mitarbeiter (inkl. aktiv-Flag)
INSERT INTO mitarbeiter (vorname, nachname, email, passwort_hash, admin_role_id, aktiv) VALUES
('User', 'Entwicklung', 'user', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', null, true),
('Admin', 'Vertrieb', 'admin', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 3, true),
('Lukas', 'Lang', 'lukas@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 2, true),
('Sophie', 'Kurz', 'sophie@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 1, true),
('Jonas', 'Fischer', 'jonas@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', null, true),
('Maria', 'Neumann', 'maria@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 3, true),
('Max', 'Mustermann', 'max@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 2, true),
('Anna', 'Beispiel', 'anna@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 1, true);

-- 3. Teams
INSERT INTO team (name, max_urlaub_prozent) VALUES
('Entwicklung', 40),
('Vertrieb', 35);

-- 4. Mitarbeiter-Team-Zuordnung
INSERT INTO mitarbeiter_team (mitarbeiter_id, team_id) VALUES
(1, 1),
(2, 2),
(3, 1),
(4, 2),
(5, 1),
(6, 2),
(7, 1),
(8, 2);

-- 5. Mitarbeiterdaten (statt Urlaubsdaten)
INSERT INTO mitarbeiterdaten (
    mitarbeiter_id, verfuegbare_urlaubstage,
    urlaubstage_pro_jahr, aktuelles_jahr,
    resturlaub_vorjahr
) VALUES
(1, 30, 30, 2025, 5),
(2, 25, 28, 2025, 3),
(3, 28, 30, 2025, 0),
(4, 24, 26, 2025, 4),
(5, 27, 30, 2025, 2),
(6, 26, 30, 2025, 6),
(7, 30, 30, 2025, 5),
(8, 25, 28, 2025, 3);

-- 6. Urlaubsstatus
INSERT INTO urlaubsstatus (status_name) VALUES
('Beantragt'),
('Genehmigt'),
('Abgelehnt'),
('Storniert');

-- 7. Urlaubsanträge
INSERT INTO urlaubsantrag (
    mitarbeiter_id, start_datum, end_datum,
    status, genehmigt_von_id, antragsdatum
) VALUES
(1, '2025-07-02', '2025-07-08', 1, 2, CURRENT_TIMESTAMP),
(2, '2025-08-16', '2025-08-20', 0, NULL, CURRENT_TIMESTAMP),
(3, '2025-06-10', '2025-06-20', 2, 1, CURRENT_TIMESTAMP),
(4, '2025-09-01', '2025-09-05', 1, 3, CURRENT_TIMESTAMP),
(5, '2025-10-12', '2025-10-20', 3, 4, CURRENT_TIMESTAMP),
(6, '2025-11-01', '2025-11-03', 0, NULL, CURRENT_TIMESTAMP),
(7, '2025-07-01', '2025-07-10', 1, 2, CURRENT_TIMESTAMP),
(8, '2025-08-15', '2025-08-25', 0, NULL, CURRENT_TIMESTAMP);

-- 8. Weitere Urlaubsanträge für User-Account-Tests
INSERT INTO urlaubsantrag (
    mitarbeiter_id, start_datum, end_datum,
    status, genehmigt_von_id, antragsdatum
) VALUES
(1, '2025-05-02', '2025-05-06', 1, 2, CURRENT_TIMESTAMP),
(1, '2025-06-16', '2025-06-18', 0, NULL, CURRENT_TIMESTAMP),
(1, '2025-04-10', '2025-04-18', 2, 1, CURRENT_TIMESTAMP),
(1, '2025-07-01', '2025-07-03', 1, 3, CURRENT_TIMESTAMP),
(1, '2025-08-12', '2025-08-18', 3, 4, CURRENT_TIMESTAMP),
(1, '2025-09-01', '2025-09-01', 0, NULL, CURRENT_TIMESTAMP),
(1, '2025-05-01', '2025-05-08', 1, 2, CURRENT_TIMESTAMP),
(1, '2025-06-15', '2025-06-23', 0, NULL, CURRENT_TIMESTAMP);

-- 8. Weitere Urlaubsanträge für User-Account-Tests
INSERT INTO urlaubsantrag (
    mitarbeiter_id, start_datum, end_datum,
    status, genehmigt_von_id, antragsdatum
) VALUES
(2, '2025-05-10', '2025-05-14', 1, 2, CURRENT_TIMESTAMP),
(2, '2025-06-24', '2025-06-26', 0, NULL, CURRENT_TIMESTAMP),
(2, '2025-04-19', '2025-04-22', 2, 1, CURRENT_TIMESTAMP),
(2, '2025-07-11', '2025-07-13', 1, 3, CURRENT_TIMESTAMP),
(2, '2025-08-20', '2025-08-23', 3, 4, CURRENT_TIMESTAMP),
(2, '2025-09-09', '2025-09-10', 0, NULL, CURRENT_TIMESTAMP),
(2, '2025-05-16', '2025-05-19', 1, 2, CURRENT_TIMESTAMP),
(2, '2025-06-27', '2025-06-29', 0, NULL, CURRENT_TIMESTAMP);
