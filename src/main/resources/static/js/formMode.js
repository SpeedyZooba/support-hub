function changeColorMode(modeFlag)
{
    const page = document.querySelector("html");

    if (modeFlag === "enabled")
    {
        page.setAttribute("data-bs-theme", "dark");
        document.querySelector(".field-box").style.backgroundColor = "#272d31";
    }
    else
    {
        page.removeAttribute("data-bs-theme");
    }
}
const modeFlag = localStorage.getItem("darkMode");
changeColorMode(modeFlag);