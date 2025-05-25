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
    selectable: true,
    select: handleSelect,
    locale: "de",
    firstDay: 1,
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
  });

  endDateInput.addEventListener("input", validateDateInputs);
  startDateInput.addEventListener("input", validateDateInputs);
  form.addEventListener("submit", handleFormSubmit);
  cancelBtn.addEventListener("click", () => dialog.close());

  calendar.render();

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
    dialog.close();
    form.reset();
  }
});
