document.addEventListener("DOMContentLoaded", function () {
  var calendarEl = document.getElementById("kalender");
  var dialog = document.getElementById("eventDialog");
  var form = document.getElementById("eventForm");
  var calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: "dayGridMonth",
    dateClick: function (info) {
      dialog.showModal();
      form.startDate.value = info.dateStr;
      form.endDate.value = info.dateStr;
    },
  });

  form.addEventListener("submit", function (event) {
    event.preventDefault();
    var title = form.eventName.value;
    var start = form.startDate.value;
    var end = form.endDate.value;

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
