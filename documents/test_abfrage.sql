SELECT
  m.id,
  m.vorname,
  m.nachname,
  m.email,
  m.aktiv,
  md.verfuegbare_urlaubstage,
  md.urlaubstage_pro_jahr,
  md.resturlaub_vorjahr,
  md.admin_level,
  ar.rolle_name
FROM mitarbeiter m
JOIN mitarbeiterdaten md ON m.id = md.mitarbeiter_id
LEFT JOIN admin_role ar ON md.admin_level = ar.id
ORDER BY m.id;
