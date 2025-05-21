document.addEventListener("DOMContentLoaded", function () {
  const calendarEl = document.getElementById("kalender");
  const dialog = document.getElementById("eventDialog");
  const form = document.getElementById("eventForm");
  const endDateInput = form.endDate;
  const startDateInput = form.startDate;
  const endDateError = document.getElementById("endDateError");
  const cancelBtn = document.getElementById("cancel");
  const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

  const calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: "dayGridMonth",
    selectable: true,
    select: handleSelect,
    locale: "de",
    allDay: true,
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

  function loadEvents() {
    fetch("/kalender/urlaubstage")
      .then((response) => response.json())
      .then((data) => {
        data.forEach((event) => {
          const endDate = new Date(event.endDate);
          endDate.setDate(endDate.getDate() + 1);
          end = endDate.toISOString().slice(0, 10);
          calendar.addEvent({
            title: event.eventName,
            start: event.startDate,
            end,
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
        .then((response) => {
          if (!response.ok) throw new Error("Fehler beim Speichern");
          return response.json();
        })
        .then((savedEvent) => {
			const endDate = new Date(savedEvent.endDate);
			endDate.setDate(endDate.getDate() + 1);
			end = endDate.toISOString().slice(0, 10);
			
          calendar.addEvent({
            title: savedEvent.eventName,
            start: savedEvent.startDate,
            end,
          });
          dialog.close();
          form.reset();
        })
        .catch((error) => {
          alert("Fehler beim Speichern des Urlaubs: " + error.message);
        });
    }
  }
});
