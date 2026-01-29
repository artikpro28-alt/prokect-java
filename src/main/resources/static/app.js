document.addEventListener("DOMContentLoaded", () => {
    const buttons = document.querySelectorAll("button");
    const body = document.body;

    body.style.opacity = 0;
    body.style.transition = "opacity 0.6s ease";

    setTimeout(() => {
        body.style.opacity = 1;
    }, 100);

    buttons.forEach(btn => {
        btn.addEventListener("click", () => {

            buttons.forEach(b => b.style.pointerEvents = "none");

            btn.style.transform = "scale(1.05)";
            btn.style.background = "linear-gradient(90deg, #ff4d4d, #4d79ff)";
            btn.style.color = "#fff";

        });
    });
});
