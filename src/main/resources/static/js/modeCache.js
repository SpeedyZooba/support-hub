function applyColorMode(modeFlag)
{
    const page = document.querySelector("html");

    if (modeFlag === "enabled")
    {
        page.setAttribute("data-bs-theme", "dark");
    }
    else
    {
        page.removeAttribute("data-bs-theme");
    }
}

const modeFlag = localStorage.getItem("darkMode");
applyColorMode(modeFlag);