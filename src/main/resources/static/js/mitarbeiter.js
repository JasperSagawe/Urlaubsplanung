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
  const abteilungSelect = document.getElementById("abteilung-select");
  const rolleSelect = document.getElementById("rolle-select");

  let mitarbeiterId = null;

  if (anlegenBtn && dialog && abbrechenBtn && form) {
    anlegenBtn.addEventListener("click", () => {
      mitarbeiterId = null;
      form.reset();
      ladeAbteilungUndRolleSelect();
      dialog.showModal();
    });
    abbrechenBtn.addEventListener("click", () => dialog.close());
  }

  if (form) {
    form.addEventListener("submit", function (e) {
      e.preventDefault();
      const data = {
        id: mitarbeiterId ?? null,
        vorname: form.vorname.value,
        nachname: form.nachname.value,
        passwort: form.passwort.value,
        urlaubstageProJahr: form.urlaubstageProJahr.value,
        verfuegbareUrlaubstage: form.urlaubstageProJahr.value,
        abteilung: {
          id: form.abteilung.value,
          name: form.abteilung.options[form.abteilung.selectedIndex].text,
        },
        rolle: {
          id: form.rolle.value,
          name: form.rolle.options[form.rolle.selectedIndex].text,
        },
      };
      fetch("/verwaltung/mitarbeiter/save", {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data),
      })
        .then((response) => {
          if (response.ok) {
            mitarbeiterId = null;
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
    if (e.target.classList.contains("bearbeite-mitarbeiter-btn")) {
      (mitarbeiterId = e.target.getAttribute("data-id")),
        (form.vorname.value = e.target.getAttribute("data-vorname"));
      form.nachname.value = e.target.getAttribute("data-nachname");
      form.passwort.value = e.target.getAttribute("data-passwort");
      form.urlaubstageProJahr.value = e.target.getAttribute(
        "data-urlaubstageProJahr"
      );
      ladeAbteilungUndRolleSelect(
        e.target.getAttribute("data-abteilung-id"),
        e.target.getAttribute("data-rolle-id")
      );
      dialog.showModal();
    }
  });

  function ladeAbteilungUndRolleSelect(abteilungId, rolleId) {
    console.log(abteilungId, rolleId);
    fetch("/verwaltung/abteilung-select")
      .then((response) => response.json())
      .then((data) => {
        abteilungSelect.innerHTML = '<option value="">Bitte wählen...</option>';
        data.forEach((abteilung) => {
          const option = document.createElement("option");
          option.value = abteilung.id;
          option.textContent = abteilung.name;
          option.selected = abteilungId == abteilung.id;
          abteilungSelect.appendChild(option);
        });
      });

    fetch("/verwaltung/rolle-select")
      .then((response) => response.json())
      .then((data) => {
        rolleSelect.innerHTML = '<option value="">Bitte wählen...</option>';
        data.forEach((rolle) => {
          const option = document.createElement("option");
          option.value = rolle.id;
          option.textContent = rolle.name;
          option.selected = rolleId == rolle.id;
          rolleSelect.appendChild(option);
        });
      });
  }

  document.body.addEventListener("click", function (e) {
    if (e.target.classList.contains("delete-mitarbeiter-btn")) {
      if (e.target.dataset.confirming === "true") {
        mitarbeiterId = e.target.getAttribute("data-id");
        fetch("/verwaltung/mitarbeiter/delete/" + mitarbeiterId, {
          method: "DELETE",
          headers: getHeaders(),
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
      .catch((error) => console.error("Fehler beim Laden der Tabelle:", error));
  }

  function getHeaders() {
    const headers = { "Content-Type": "application/json" };
    if (csrfHeader && csrfToken) headers[csrfHeader] = csrfToken;

    return headers;
  }
});
