document.addEventListener("DOMContentLoaded", function () {
  const csrfToken = document
    .querySelector('meta[name="_csrf"]')
    .getAttribute("content");
  const csrfHeader = document
    .querySelector('meta[name="_csrf_header"]')
    .getAttribute("content");

  const anlegenBtn = document.getElementById("mitarbeiter-anlegen-btn");
  const dialog = document.getElementById("mitarbeiter-anlegen-dialog");
  const abbrechenBtn = document.getElementById("anlegen-abbrechen-btn");
  const form = document.getElementById("mitarbeiter-anlegen-form");
  const teamSelect = document.getElementById("team-select");

  if (anlegenBtn && dialog && abbrechenBtn && form) {
    anlegenBtn.addEventListener("click", () => dialog.showModal());
    abbrechenBtn.addEventListener("click", () => dialog.close());

    form.addEventListener("submit", function (e) {
      e.preventDefault();
      const data = {
        vorname: form.vorname.value,
        nachname: form.nachname.value,
        passwort: form.passwort.value,
        urlaubstageProJahr: form.urlaubstageProJahr.value,
        verfuegbareUrlaubstage: form.urlaubstageProJahr.value,
        team: {
          id: form.team.value,
          name: form.team.options[form.team.selectedIndex].text,
        },
        aktiv: form.aktiv.checked,
        passwortZuruecksetzen: form.passwortZuruecksetzen.checked,
      };
      const headers = {
        "Content-Type": "application/json",
      };
      if (csrfHeader && csrfToken) {
        headers[csrfHeader] = csrfToken;
      }
      fetch("/verwaltung/mitarbeiter/save", {
        method: "POST",
        headers: headers,
        body: JSON.stringify(data),
      })
        .then((response) => {
          if (response.ok) {
            ladeTabelleNeu();
            dialog.close();
            form.reset();
          } else {
            alert("Fehler beim Anlegen.");
          }
        })
        .catch(() => alert("Fehler beim Anlegen."));
    });
  }

  document.body.addEventListener("click", function (e) {
    if (e.target.classList.contains("delete-mitarbeiter-btn")) {
      if (e.target.dataset.confirming === "true") {
        const mitarbeiterId = e.target.getAttribute("data-id");
        const headers = {
          "Content-Type": "application/json",
        };
        if (csrfHeader && csrfToken) {
          headers[csrfHeader] = csrfToken;
        }

        fetch("/verwaltung/mitarbeiter/delete/" + mitarbeiterId, {
          method: "DELETE",
          headers: headers,
        }).then((response) => {
          if (response.ok) {
            ladeTabelleNeu();
          } else {
            alert("Fehler beim Löschen.");
          }
        });
        e.target.textContent = "Löschen";
        e.target.removeAttribute("data-confirming");
      } else {
        e.target.textContent = "Bestätigen";
        e.target.dataset.confirming = "true";
        setTimeout(() => {
          if (e.target.dataset.confirming === "true") {
            e.target.textContent = "Löschen";
            e.target.removeAttribute("data-confirming");
          }
        }, 3000);
      }
    }
  });

  function ladeTabelleNeu() {
    fetch(`/verwaltung/mitarbeiter-tabelle`)
      .then((response) => response.text())
      .then((html) => {
        document.getElementById("mitarbeiter-tabelle-container").innerHTML =
          html;
      })
      .catch((error) => {
        console.error("Fehler beim Laden der Tabelle:", error);
      });
  }

  if (anlegenBtn && teamSelect) {
    anlegenBtn.addEventListener("click", function () {
      fetch("/verwaltung/teams")
        .then((response) => response.json())
        .then((data) => {
          teamSelect.innerHTML = '<option value="">Bitte wählen...</option>';
          data.forEach((team) => {
            const option = document.createElement("option");
            option.value = team.id;
            option.textContent = team.name;
            teamSelect.appendChild(option);
          });
        });
    });
  }
});
