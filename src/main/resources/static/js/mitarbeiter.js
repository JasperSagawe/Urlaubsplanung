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
        abteilung: {
          id: form.abteilung.value,
          name: form.abteilung.options[form.abteilung.selectedIndex].text,
        },
        rolle: {
          id: form.rolle.value,
          name: form.rolle.options[form.rolle.selectedIndex].text,
        },
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

  if (anlegenBtn && abteilungSelect) {
    anlegenBtn.addEventListener("click", function () {
      fetch("/verwaltung/abteilung-select")
        .then((response) => response.json())
        .then((data) => {
          abteilungSelect.innerHTML =
            '<option value="">Bitte wählen...</option>';
          data.forEach((abteilung) => {
            const option = document.createElement("option");
            option.value = abteilung.id;
            option.textContent = abteilung.name;
            abteilungSelect.appendChild(option);
          });
        });
    });
  }

  if (anlegenBtn && rolleSelect) {
    anlegenBtn.addEventListener("click", function () {
      fetch("/verwaltung/rolle-select")
        .then((response) => response.json())
        .then((data) => {
          rolleSelect.innerHTML = '<option value="">Bitte wählen...</option>';
          data.forEach((rolle) => {
            const option = document.createElement("option");
            option.value = rolle.id;
            option.textContent = rolle.name;
            rolleSelect.appendChild(option);
          });
        });
    });
  }
});
