SELECT
  m.id,
  m.vorname,
  m.nachname,
  m.email,
  md.verfuegbare_urlaubstage,
  md.urlaubstage_pro_jahr,
  md.resturlaub_vorjahr,
  ar.rolle_name
FROM mitarbeiter m
JOIN mitarbeiterdaten md ON m.id = md.mitarbeiter_id
ORDER BY m.id;
