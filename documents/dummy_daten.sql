-- 1. Mitarbeiter (inkl. aktiv-Flag)
INSERT INTO mitarbeiter (id, vorname, nachname, email, passwort_hash, admin_role, aktiv) VALUES
(1, 'User', 'Entwicklung', 'user', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', null, true),
(2, 'Admin', 'Vertrieb', 'admin', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 3, true),
(3, 'Lukas', 'Lang', 'lukas@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 2, true),
(4, 'Sophie', 'Kurz', 'sophie@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 1, true),
(5, 'Jonas', 'Fischer', 'jonas@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', null, true),
(6, 'Maria', 'Neumann', 'maria@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 3, true),
(7, 'Max', 'Mustermann', 'max@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 2, true),
(8, 'Anna', 'Beispiel', 'anna@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 1, true);

-- 2. Teams
INSERT INTO team (id, name, max_urlaub_prozent) VALUES
(1, 'Entwicklung', 40),
(2, 'Vertrieb', 35);

-- 3. Mitarbeiter-Team-Zuordnung
INSERT INTO mitarbeiter_team (mitarbeiter_id, team_id) VALUES
(1, 1),
(2, 2),
(3, 1),
(4, 2),
(5, 1),
(6, 2),
(7, 1),
(8, 2);

-- 4. Mitarbeiterdaten (statt Urlaubsdaten)
INSERT INTO mitarbeiterdaten (
    id, mitarbeiter_id, verfuegbare_urlaubstage,
    urlaubstage_pro_jahr, aktuelles_jahr,
    resturlaub_vorjahr, admin_level
) VALUES
(1, 1, 30, 30, 2025, 5, 1),
(2, 2, 25, 28, 2025, 3, 4),
(3, 3, 28, 30, 2025, 0, 2),
(4, 4, 24, 26, 2025, 4, 0),
(5, 5, 27, 30, 2025, 2, 1),
(6, 6, 26, 30, 2025, 6, 3),
(7, 7, 30, 30, 2025, 5, 1),
(8, 8, 25, 28, 2025, 3, 0);

-- 5. Admin-Rollen
INSERT INTO admin_role (id, rolle_name) VALUES
(0, 'Kein Admin'),
(1, 'Team-Admin'),
(2, 'Bereichs-Admin'),
(3, 'Super-Admin');

-- 6. Urlaubsstatus
INSERT INTO urlaubsstatus (id, status_name) VALUES
(0, 'Beantragt'),
(1, 'Genehmigt'),
(2, 'Abgelehnt'),
(3, 'Storniert');

-- 7. Urlaubsanträge
INSERT INTO urlaubsantrag (
    id, mitarbeiter_id, start_datum, end_datum,
    status, genehmigt_von_id, antragsdatum
) VALUES
(1, 1, '2025-07-02', '2025-07-08', 1, 2, CURRENT_TIMESTAMP),
(2, 2, '2025-08-16', '2025-08-20', 0, NULL, CURRENT_TIMESTAMP),
(3, 3, '2025-06-10', '2025-06-20', 2, 1, CURRENT_TIMESTAMP),
(4, 4, '2025-09-01', '2025-09-05', 1, 3, CURRENT_TIMESTAMP),
(5, 5, '2025-10-12', '2025-10-20', 3, 4, CURRENT_TIMESTAMP),
(6, 6, '2025-11-01', '2025-11-03', 0, NULL, CURRENT_TIMESTAMP),
(7, 7, '2025-07-01', '2025-07-10', 1, 2, CURRENT_TIMESTAMP),
(8, 8, '2025-08-15', '2025-08-25', 0, NULL, CURRENT_TIMESTAMP);

-- 8. Weitere Urlaubsanträge für User-Account-Tests
INSERT INTO urlaubsantrag (
    id, mitarbeiter_id, start_datum, end_datum,
    status, genehmigt_von_id, antragsdatum
) VALUES
(9, 1, '2025-07-02', '2025-07-08', 1, 2, CURRENT_TIMESTAMP),
(10, 1, '2025-08-16', '2025-08-20', 0, NULL, CURRENT_TIMESTAMP),
(11, 1, '2025-06-10', '2025-06-20', 2, 1, CURRENT_TIMESTAMP),
(12, 1, '2025-09-01', '2025-09-05', 1, 3, CURRENT_TIMESTAMP),
(13, 1, '2025-10-12', '2025-10-20', 3, 4, CURRENT_TIMESTAMP),
(14, 1, '2025-11-01', '2025-11-03', 0, NULL, CURRENT_TIMESTAMP),
(15, 1, '2025-07-01', '2025-07-10', 1, 2, CURRENT_TIMESTAMP),
(16, 1, '2025-08-15', '2025-08-25', 0, NULL, CURRENT_TIMESTAMP);
