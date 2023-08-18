function changeColorMode(modeFlag)
{
    const page = document.querySelector("html");

    if (modeFlag === "enabled")
    {
        page.setAttribute("data-bs-theme", "dark");
        document.getElementById("modeIcon").innerHTML = '<i class="bi-sun-fill" style="margin: 7px;"></i>Light Mode';
    }
    else
    {
        page.removeAttribute("data-bs-theme");
        document.getElementById("modeIcon").innerHTML = '<i class="bi-moon-stars-fill" style="margin: 7px;"></i>Dark Mode';
    }

    document.getElementById("darkToggle").addEventListener("click", function() {
        const icon = document.getElementById("modeIcon");
        if (!page.hasAttribute("data-bs-theme"))
        {                
            page.setAttribute("data-bs-theme", "dark");
            icon.innerHTML = '<i class="bi-sun-fill" style="margin: 7px;"></i>Light Mode';
            localStorage.setItem("darkMode", "enabled");
        }
        else
        {
            page.removeAttribute("data-bs-theme");
            icon.innerHTML = '<i class="bi-moon-stars-fill" style="margin: 7px;"></i>Dark Mode';
            localStorage.setItem("darkMode", "disabled");
        }
    });
}

const modeFlag = localStorage.getItem("darkMode");
changeColorMode(modeFlag);