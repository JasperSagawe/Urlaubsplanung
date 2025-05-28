-- 0. Damit falls schon Daten vorhanden, die Datenbank zurückgesetzt wird 
TRUNCATE TABLE
    urlaubsantrag,
    urlaubsstatus,
    mitarbeiterdaten,
    abteilung,
    mitarbeiter,
    user_role
RESTART IDENTITY CASCADE;

-- 1. User-Rollen
INSERT INTO user_role (rolle_name) VALUES
('User'),
('Abteilungsleiter'),
('Admin');

-- 2. Mitarbeiter (inkl. aktiv-Flag)
INSERT INTO mitarbeiter (vorname, nachname, email, passwort_hash, user_role_id, aktiv) VALUES
('User', 'Entwicklung', 'user', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', null, true),
('Admin', 'Vertrieb', 'admin', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 3, true),
('Lukas', 'Lang', 'lukas@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 2, true),
('Sophie', 'Kurz', 'sophie@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 1, true),
('Jonas', 'Fischer', 'jonas@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', null, true),
('Maria', 'Neumann', 'maria@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 3, true),
('Max', 'Mustermann', 'max@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 2, true),
('Anna', 'Beispiel', 'anna@firma.de', '$2a$12$wkN76WF49Sh8m4DBo4PHqOfnu5f6CP/8lfNgMqLQCoruXhvg4cRke', 1, true);

-- 3. Abteilung (optional mit abteilungsleiter_id – Beispielhaft: abteilungsleiter ist Mitarbeiter 3 oder 4)
INSERT INTO abteilung (name, max_urlaub_prozent, abteilungsleiter_id) VALUES
('Entwicklung', 40, 3),
('Vertrieb', 35, 4);

-- 4. Mitarbeiterdaten (statt Urlaubsdaten, jetzt mit abteilung_id)
INSERT INTO mitarbeiterdaten (
    mitarbeiter_id, verfuegbare_urlaubstage,
    urlaubstage_pro_jahr, aktuelles_jahr,
    resturlaub_vorjahr, abteilung_id
) VALUES
(1, 30, 30, 2025, 5, 1),
(2, 25, 28, 2025, 3, 2),
(3, 28, 30, 2025, 0, 1),
(4, 24, 26, 2025, 4, 2),
(5, 27, 30, 2025, 2, 1),
(6, 26, 30, 2025, 6, 2),
(7, 30, 30, 2024, 5, 1),
(8, 25, 28, 2023, 3, 2);

-- 5. Urlaubsstatus
INSERT INTO urlaubsstatus (status_name) VALUES
('Beantragt'),
('Genehmigt'),
('Abgelehnt'),
('Storniert');

-- 6. Urlaubsanträge
INSERT INTO urlaubsantrag (
    mitarbeiter_id, start_datum, end_datum,
    status, antragsdatum
) VALUES
(1, '2025-07-02', '2025-07-08', 1, CURRENT_TIMESTAMP),
(2, '2025-08-16', '2025-08-20', 0, CURRENT_TIMESTAMP),
(3, '2025-06-10', '2025-06-20', 2, CURRENT_TIMESTAMP),
(4, '2025-09-01', '2025-09-05', 1, CURRENT_TIMESTAMP),
(5, '2025-10-12', '2025-10-20', 3, CURRENT_TIMESTAMP),
(6, '2025-11-01', '2025-11-03', 0, CURRENT_TIMESTAMP),
(7, '2025-07-01', '2025-07-10', 1, CURRENT_TIMESTAMP),
(8, '2025-08-15', '2025-08-25', 0, CURRENT_TIMESTAMP);

-- 7. Weitere Urlaubsanträge für User-Account-Tests
INSERT INTO urlaubsantrag (
    mitarbeiter_id, start_datum, end_datum,
    status, antragsdatum
) VALUES
(1, '2025-07-02', '2025-07-08', 1, CURRENT_TIMESTAMP),
(1, '2025-08-16', '2025-08-20', 0, CURRENT_TIMESTAMP),
(1, '2025-06-10', '2025-06-20', 2, CURRENT_TIMESTAMP),
(1, '2025-09-01', '2025-09-05', 1, CURRENT_TIMESTAMP),
(1, '2025-10-12', '2025-10-20', 3, CURRENT_TIMESTAMP),
(1, '2025-11-01', '2025-11-03', 0, CURRENT_TIMESTAMP),
(1, '2025-07-01', '2025-07-10', 1, CURRENT_TIMESTAMP),
(1, '2025-08-15', '2025-08-25', 0, CURRENT_TIMESTAMP);
