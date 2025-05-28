document.addEventListener("DOMContentLoaded", function () {
  const calendarEl = document.getElementById("kalender");
  const dialog = document.getElementById("eventDialog");
  const form = document.getElementById("eventForm");
  const endDateInput = form.endDate;
  const startDateInput = form.startDate;
  const endDateError = document.getElementById("endDateError");
  const cancelBtn = document.getElementById("cancel");
  const csrfToken = document
    .querySelector('meta[name="_csrf"]')
    .getAttribute("content");
  const csrfHeader = document
    .querySelector('meta[name="_csrf_header"]')
    .getAttribute("content");

  const calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: "dayGridMonth",
    selectable: true,
    select: handleSelect,
    locale: "de",
    firstDay: 1,
    dayMaxEvents: 2,
    buttonText: {
      today: "Heute",
    },
    events: {
      url: "/kalender/urlaubstage",
      method: "GET",
      failure: function () {
        alert("Fehler beim Laden der Urlaubstage!");
      },
    },
    eventDidMount: function (info) {
      info.el.setAttribute("title", info.event.title);
    },
  });

  endDateInput.addEventListener("input", validateDateInputs);
  startDateInput.addEventListener("input", validateDateInputs);
  form.addEventListener("submit", handleFormSubmit);
  cancelBtn.addEventListener("click", () => dialog.close());

  calendar.render();

  document.body.addEventListener("click", function (e) {
    if (e.target.classList.contains("delete-urlaub-btn")) {
      if (e.target.dataset.confirming === "true") {
        const urlaubId = e.target.getAttribute("data-id");
        const headers = {
          "Content-Type": "application/json",
        };
        if (csrfHeader && csrfToken) {
          headers[csrfHeader] = csrfToken;
        }

        fetch("/kalender/urlaubstage/delete/" + urlaubId, {
          method: "DELETE",
          headers: headers,
        }).then((response) => {
          if (response.ok) {
            ladeTabelleNeu();
            calendar.refetchEvents();
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

  function handleSelect(event) {
    dialog.showModal();
    form.startDate.value = event.startStr;
    if (event.endStr) {
      const endDate = new Date(event.endStr);
      endDate.setDate(endDate.getDate() - 1);
      form.endDate.value = endDate.toISOString().slice(0, 10);
    } else {
      form.endDate.value = event.startStr;
    }
    resetEndDateError();
  }

  function validateDateInputs() {
    const start = new Date(startDateInput.value);
    const end = new Date(endDateInput.value);
    if (end < start) {
      endDateError.style.display = "block";
      endDateInput.style.borderColor = "red";
    } else {
      resetEndDateError();
    }
  }

  function resetEndDateError() {
    endDateError.style.display = "none";
    endDateInput.style.borderColor = "";
  }

  function handleFormSubmit(event) {
    event.preventDefault();
    const startDate = form.startDate.value;
    const endDate = form.endDate.value;

    if (new Date(startDate) > new Date(endDate)) {
      alert("Das Enddatum muss nach dem Startdatum sein.");
      return;
    }

    if (startDate && endDate) {
      const headers = {
        "Content-Type": "application/json",
      };
      if (csrfHeader && csrfToken) {
        headers[csrfHeader] = csrfToken;
      }
      fetch("/kalender/urlaubstage/save", {
        method: "POST",
        headers: headers,
        body: JSON.stringify({
          startDate,
          endDate,
        }),
      })
        .then(async (response) => {
          if (!response.ok) {
            const errorText = await response.text();
            if (errorText.includes("ZU_VIELE_ABTEILUNGSMITGLIEDER_IM_URLAUB")) {
              throw new Error(
                "In diesem Zeitraum nehmen zu viele Abteilungsmitglieder Urlaub."
              );
            } else if (errorText.includes("DU_KANNST_MAXIMAL_")) {
              const matches = errorText.match(
                /DU_KANNST_MAXIMAL_(\d+)_BEANTRAGEN/
              );
              const maxTage = matches ? matches[1] : "?";
              throw new Error(
                `Du kannst maximal ${maxTage} weitere Urlaubstage beantragen.`
              );
            } else {
              throw new Error("Unbekannter Fehler beim Speichern.");
            }
          }
          return response.json();
        })
        .then((savedEvent) => {
          ladeTabelleNeu();
          calendar.refetchEvents();
          dialog.close();
          form.reset();
        })
        .catch((error) => {
          alert("Fehler beim Speichern des Urlaubs: " + error.message);
        });
    }
  }

  function ladeTabelleNeu() {
    fetch(`/kalender/urlaub-tabelle`)
      .then((response) => response.text())
      .then((html) => {
        document.getElementById("urlaub-tabellen-container").innerHTML = html;
      })
      .catch((error) => {
        console.error("Fehler beim Laden der Tabelle:", error);
      });
  }
});
