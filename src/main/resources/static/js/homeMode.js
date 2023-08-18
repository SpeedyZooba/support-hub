function changeColorMode(modeFlag)
{
    const page = document.querySelector("html");
    const profile = document.getElementById("profileCard");
    const tickets = document.getElementById("ticketCard");
    const ticketRows = document.getElementById("ticketRows");
    const notices = document.getElementById("noticeCard");
    const noticeRows = document.getElementById("noticeRows");
    const icon = document.getElementById("modeIcon");

    if (modeFlag === "enabled")
    {
        page.setAttribute("data-bs-theme", "dark");
        profile.classList.remove("text-bg-light");
        profile.classList.add("text-bg-dark");
        tickets.classList.remove("text-bg-light");
        ticketRows.classList.remove("table-light");
        tickets.classList.add("text-bg-dark");
        ticketRows.classList.add("table-dark");
        notices.classList.remove("text-bg-light");
        noticeRows.classList.remove("table-light");
        notices.classList.add("text-bg-dark");
        noticeRows.classList.add("table-dark");
        icon.innerHTML = '<i class="bi-sun-fill" style="margin: 7px;"></i>Light Mode';
    }
    else
    {
        page.removeAttribute("data-bs-theme");
        profile.classList.remove("text-bg-dark");
        profile.classList.add("text-bg-light");
        tickets.classList.remove("text-bg-dark");
        ticketRows.classList.remove("table-dark");
        tickets.classList.add("text-bg-light");
        ticketRows.classList.add("table-light");
        notices.classList.remove("text-bg-dark");
        noticeRows.classList.remove("table-dark");
        notices.classList.add("text-bg-light");
        noticeRows.classList.add("table-light");
        icon.innerHTML = '<i class="bi-moon-stars-fill" style="margin: 7px;"></i>Dark Mode';
    }

    document.getElementById("darkToggle").addEventListener("click", function() {
        if (!page.hasAttribute("data-bs-theme"))
        {                
            page.setAttribute("data-bs-theme", "dark");
            profile.classList.remove("text-bg-light");
            profile.classList.add("text-bg-dark");
            tickets.classList.remove("text-bg-light");
            ticketRows.classList.remove("table-light");
            tickets.classList.add("text-bg-dark");
            ticketRows.classList.add("table-dark");
            notices.classList.remove("text-bg-light");
            noticeRows.classList.remove("table-light");
            notices.classList.add("text-bg-dark");
            noticeRows.classList.add("table-dark");
            icon.innerHTML = '<i class="bi-sun-fill" style="margin: 7px;"></i>Light Mode';
            localStorage.setItem("darkMode", "enabled");
        }
        else
        {
            page.removeAttribute("data-bs-theme");
            profile.classList.remove("text-bg-dark");
            profile.classList.add("text-bg-light");
            tickets.classList.remove("text-bg-dark");
            ticketRows.classList.remove("table-dark");
            tickets.classList.add("text-bg-light");
            ticketRows.classList.add("table-light");
            notices.classList.remove("text-bg-dark");
            noticeRows.classList.remove("table-dark");
            notices.classList.add("text-bg-light");
            noticeRows.classList.add("table-light");
            icon.innerHTML = '<i class="bi-moon-stars-fill" style="margin: 7px;"></i>Dark Mode';
            localStorage.setItem("darkMode", "disabled");
        }
    });
}

const modeFlag = localStorage.getItem("darkMode");
changeColorMode(modeFlag);