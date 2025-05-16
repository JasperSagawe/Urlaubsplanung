document.addEventListener("DOMContentLoaded", function () {
  const calendarEl = document.getElementById("kalender");
  const dialog = document.getElementById("eventDialog");
  const form = document.getElementById("eventForm");
  const endDateInput = form.endDate;
  const startDateInput = form.startDate;
  const endDateError = document.getElementById("endDateError");
  const cancelBtn = document.getElementById("cancel");

  const calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: "dayGridMonth",
    dateClick: handleDateClick,
    locale: "de",
    firstDay: 1,
    buttonText: {
      today: "Heute",
    },
  });

  loadEvents();

  endDateInput.addEventListener("input", validateDateInputs);
  startDateInput.addEventListener("input", validateDateInputs);
  form.addEventListener("submit", handleFormSubmit);
  cancelBtn.addEventListener("click", () => dialog.close());

  calendar.render();

  function handleDateClick(info) {
    dialog.showModal();
    form.startDate.value = info.dateStr;
    form.endDate.value = info.dateStr;
    resetEndDateError();
  }

  function loadEvents() {
    fetch("/kalender/urlaubstage")
      .then((response) => response.json())
      .then((data) => {
        data.forEach((event) => {
          calendar.addEvent({
            title: event.eventName,
            start: event.startDate,
            end: event.endDate,
          });
        });
      })
      .catch((error) => {
        console.error("Fehler beim Laden der Urlaubstage:", error);
      });
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
    const title = form.eventName.value;
    const start = form.startDate.value;
    const end = form.endDate.value;

    if (new Date(start) > new Date(end)) {
      alert("Das Enddatum muss nach dem Startdatum sein.");
      return;
    }

    if (title && start) {
      calendar.addEvent({ title, start, end });
    }

    dialog.close();
    form.reset();
  }
});
