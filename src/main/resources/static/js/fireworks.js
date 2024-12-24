document.addEventListener('DOMContentLoaded', function () {
    console.log("Fireworks script loaded!");

    const fireworksContainer = document.getElementById('fireworks-container');

    if (!fireworksContainer) {
        console.error("No se encontró el contenedor de fuegos artificiales.");
        return;
    }

    const fireworks = new Fireworks.default({
        target: fireworksContainer,
        maxRockets: 10,
        rocketSpawnInterval: 100,
        numParticles: 100,
        particleSpeed: 10,
        fadeOut: true,
        explosionMinHeight: 0.1,
        explosionMaxHeight: 0.4,
        rocketSpeed: 3,
        colors: ['#ff3333', '#ff9933', '#ffcc00', '#33cc33', '#3366ff'],
    });

    fireworks.start();

    setTimeout(() => {
        fireworks.stop();
    }, 15000);
});
