// Применение сохраненных настроек при загрузке страницы
window.onload = function() {
    const savedBackgroundColor = localStorage.getItem('backgroundColor');
    const savedTextColor = localStorage.getItem('textColor');
    const savedButtonColor = localStorage.getItem('buttonColor');

    if (savedBackgroundColor) {
        document.body.style.backgroundColor = savedBackgroundColor;
    }
    if (savedTextColor) {
        document.body.style.color = savedTextColor;
        const headings = document.querySelectorAll('h1, h2'); // Цвет заголовков
        headings.forEach(heading => {
            heading.style.color = savedTextColor;
        });
    }
    if (savedButtonColor) {
        // Установим цвет кнопки "Поиск" при загрузке
        const searchButton = document.querySelector('input[type="submit"]');
        searchButton.style.backgroundColor = savedButtonColor;
        searchButton.style.color = 'white'; // Убедитесь, что текст на кнопке виден
    }
};

// Обработчик для страницы настроек
document.getElementById('settingsForm')?.addEventListener('submit', function(event) {
    event.preventDefault();

    const backgroundColor = document.getElementById('backgroundColor').value;
    const textColor = document.getElementById('textColor').value;
    const buttonColor = document.getElementById('buttonColor').value;

    // Сохранение настроек в локальном хранилище
    localStorage.setItem('backgroundColor', backgroundColor);
    localStorage.setItem('textColor', textColor);
    localStorage.setItem('buttonColor', buttonColor);

    // Применение нового цвета кнопки
    const searchButton = document.querySelector('input[type="submit"]');
    searchButton.style.backgroundColor = buttonColor;
    searchButton.style.color = 'white'; // Убедитесь, что текст на кнопке виден

    alert('Настройки сохранены!');

    // Редирект на главную страницу
    window.location.href = '/index'; // Убедитесь, что здесь правильный путь к вашей главной странице
});