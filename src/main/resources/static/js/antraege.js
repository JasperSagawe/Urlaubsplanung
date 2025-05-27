document.addEventListener("DOMContentLoaded", function () {
  const csrfToken = document
    .querySelector('meta[name="_csrf"]')
    .getAttribute("content");
  const csrfHeader = document
    .querySelector('meta[name="_csrf_header"]')
    .getAttribute("content");

  document.body.addEventListener("click", function (e) {
    const actions = [
      {
        btnClass: "genehmigen-urlaub-btn",
        url: "/antraege/genehmigen/",
        defaultText: "Genehmigen",
        confirmText: "Bestätigen",
        errorMsg: "Fehler beim Genehmigen.",
      },
      {
        btnClass: "ablehnen-urlaub-btn",
        url: "/antraege/ablehnen/",
        defaultText: "Ablehnen",
        confirmText: "Bestätigen",
        errorMsg: "Fehler beim Ablehnen.",
      },
    ];

    for (const action of actions) {
      if (e.target.classList.contains(action.btnClass)) {
        if (e.target.dataset.confirming === "true") {
          const urlaubId = e.target.getAttribute("data-id");
          const headers = { "Content-Type": "application/json" };
          if (csrfHeader && csrfToken) headers[csrfHeader] = csrfToken;

          fetch(action.url + urlaubId, {
            method: "POST",
            headers: headers,
          }).then((response) => {
            if (response.ok) {
              ladeTabelleNeu();
              if (typeof calendar !== "undefined") calendar.refetchEvents();
            } else {
              alert(action.errorMsg);
            }
          });
          e.target.textContent = action.defaultText;
          e.target.removeAttribute("data-confirming");
        } else {
          e.target.textContent = action.confirmText;
          e.target.dataset.confirming = "true";
          setTimeout(() => {
            if (e.target.dataset.confirming === "true") {
              e.target.textContent = action.defaultText;
              e.target.removeAttribute("data-confirming");
            }
          }, 3000);
        }
        break;
      }
    }
  });

  function ladeTabelleNeu() {
    fetch(`/antraege/tabelle`)
      .then((response) => response.text())
      .then((html) => {
        document.getElementById("antraege-tabelle-container").innerHTML = html;
      })
      .catch((error) => {
        console.error("Fehler beim Laden der Tabelle:", error);
      });
  }
});
