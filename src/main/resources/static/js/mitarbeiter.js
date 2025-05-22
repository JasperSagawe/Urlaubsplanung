document.addEventListener("DOMContentLoaded", function () {
const csrfToken = document
  .querySelector('meta[name="_csrf"]')
  .getAttribute("content");
const csrfHeader = document
  .querySelector('meta[name="_csrf_header"]')
  .getAttribute("content");
  
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
        document.getElementById("mitarbeiter-tabelle-container").innerHTML = html;
      })
      .catch((error) => {
        console.error("Fehler beim Laden der Tabelle:", error);
      });
  }
});
