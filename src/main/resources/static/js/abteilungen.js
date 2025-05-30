document.addEventListener("DOMContentLoaded", function () {
  const csrfToken = document
    .querySelector('meta[name="_csrf"]')
    .getAttribute("content");
  const csrfHeader = document
    .querySelector('meta[name="_csrf_header"]')
    .getAttribute("content");

  const anlegenBtn = document.getElementById("abteilung-anlegen-btn");
  const dialog = document.getElementById("abteilung-anlegen-dialog");
  const abbrechenBtn = document.getElementById("anlegen-abbrechen-btn");
  const form = document.getElementById("abteilung-anlegen-form");
  const mitarbeiterSelect = document.getElementById("mitarbeiter-select");

  let abteilungId = null;

  if (anlegenBtn && dialog && abbrechenBtn && form) {
    anlegenBtn.addEventListener("click", () => {
      abteilungId = null;
      form.reset();
      ladeMitarbeiterSelect();
      dialog.showModal();
    });
    abbrechenBtn.addEventListener("click", () => dialog.close());
  }

  if (form) {
    form.addEventListener("submit", function (e) {
      e.preventDefault();
      const data = {
        id: abteilungId ?? null,
        name: form.name.value,
        maxUrlaubProzent: form.limit.value,
        abteilungsleiter: {
          id: form.abteilungsleiter.value,
          name: form.abteilungsleiter.options[
            form.abteilungsleiter.selectedIndex
          ].text,
        },
      };
      fetch("/verwaltung/abteilungen/save", {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data),
      })
        .then((response) => {
          if (response.ok) {
            abteilungId = null;
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
    if (e.target.classList.contains("bearbeite-abteilung-btn")) {
      abteilungId = e.target.getAttribute("data-id");
      form.name.value = e.target.getAttribute("data-name");
      form.limit.value = e.target.getAttribute("data-maxUrlaubProzent");
      ladeMitarbeiterSelect(
        e.target.getAttribute("data-abteilungsleiter-id"),
        e.target.getAttribute("data-abteilungsleiter-name")
      );
      dialog.showModal();
    }
  });

  function ladeMitarbeiterSelect(selectedId, selectedName) {
    fetch("/verwaltung/mitarbeiter-select")
      .then((response) => response.json())
      .then((data) => {
        mitarbeiterSelect.innerHTML = selectedId
          ? ""
          : '<option value="">Bitte wählen...</option>';

        if (selectedId && selectedName) {
          const option = document.createElement("option");
          option.value = selectedId;
          option.textContent = selectedName;
          option.selected = true;
          mitarbeiterSelect.appendChild(option);
        }

        data.forEach((mitarbeiter) => {
          if (!selectedId || mitarbeiter.id != selectedId) {
            const option = document.createElement("option");
            option.value = mitarbeiter.id;
            option.textContent = mitarbeiter.name;
            mitarbeiterSelect.appendChild(option);
          }
        });
      });
  }

  document.body.addEventListener("click", function (e) {
    if (e.target.classList.contains("delete-abteilung-btn")) {
      if (e.target.dataset.confirming === "true") {
        abteilungId = e.target.getAttribute("data-id");
        fetch("/verwaltung/abteilungen/remove/" + abteilungId, {
          method: "POST",
          headers: getHeaders(),
        })
          .then(() =>
            fetch("/verwaltung/abteilungen/delete/" + abteilungId, {
              method: "DELETE",
              headers: getHeaders(),
            })
          )
          .then((response) => {
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
    fetch(`/verwaltung/abteilungen-tabelle`)
      .then((response) => response.text())
      .then((html) => {
        document.getElementById("abteilungen-tabelle-container").innerHTML =
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
