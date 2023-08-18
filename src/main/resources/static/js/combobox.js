function selectCategory(event, category) 
{
    event.preventDefault();
    document.getElementById('categoryDropdown').innerText = category;
    document.getElementById('categoryInput').value = category;
}

function applyColorMode(boxColor)
{
    const combobox = document.getElementById("categoryDropdown");

    if (boxColor === "enabled")
    {
        combobox.classList.remove("combobox");
        combobox.classList.add("combobox-dark");
    }
    else
    {
        combobox.classList.remove("combobox-dark");
        combobox.classList.add("combobox");
    }
}

const boxColor = localStorage.getItem("darkMode");
applyColorMode(boxColor);