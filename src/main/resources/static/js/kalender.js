document.addEventListener("DOMContentLoaded", function () {
  var calendarEl = document.getElementById("kalender");
  var dialog = document.getElementById("eventDialog");
  var form = document.getElementById("eventForm");
  var endDateInput = form.endDate;
  var startDateInput = form.startDate;
  var endDateError = document.getElementById("endDateError");

  var calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: "dayGridMonth",
    dateClick: function (info) {
      dialog.showModal();
      form.startDate.value = info.dateStr;
      form.endDate.value = info.dateStr;
      endDateError.style.display = "none";
      endDateInput.style.borderColor = "";
    },
  });

  function validateDateInputs() {
    if (new Date(endDateInput.value) < new Date(startDateInput.value)) {
      endDateError.style.display = "block";
      endDateInput.style.borderColor = "red";
    } else {
      endDateError.style.display = "none";
      endDateInput.style.borderColor = "";
    }
  }

  endDateInput.addEventListener("input", validateDateInputs);
  startDateInput.addEventListener("input", validateDateInputs);

  form.addEventListener("submit", function (event) {
    event.preventDefault();
    var title = form.eventName.value;
    var start = form.startDate.value;
    var end = form.endDate.value;

    if (new Date(start) > new Date(end)) {
      alert("Das Enddatum muss nach dem Startdatum sein.");
      return;
    }

    if (title && start) {
      calendar.addEvent({
        title: title,
        start: start,
        end: end,
      });
    }

    dialog.close();
    form.reset();
  });

  document.getElementById("cancel").addEventListener("click", function () {
    dialog.close();
  });

  calendar.render();
});
