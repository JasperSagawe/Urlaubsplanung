document.addEventListener("DOMContentLoaded", function () {
  var calendarEl = document.getElementById("kalender");
  var calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: "dayGridMonth",
  });
  calendar.render();
});
