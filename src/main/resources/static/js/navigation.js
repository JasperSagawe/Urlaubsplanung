function toggleNavigation() {
  const navMenu = document.querySelector(".navMenu");
  const isOpen = navMenu.classList.contains("open");
  const navToggle = document.querySelector(".navToggle");

  if (isOpen) {
    navMenu.classList.remove("open");
    navToggle.classList.remove("open");
  } else {
    navMenu.classList.add("open");
    navToggle.classList.add("open");
  }
}
