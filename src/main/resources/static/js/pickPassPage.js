let isValid = false;
let areMatching = false;
const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[+.$])(?!.*\s).{8,}$/;

function enable()
{
    if (isValid && areMatching)
    {
        activateButton();
    }
    else
    {
        deactivateButton();
    }
}

function checkMatch()
{
    const password = document.getElementById("password").value;
    const retypedPassword = document.getElementById("confirmPassword").value;
    const failMessage = document.getElementById("wrongPassword");
    areMatching = (password === retypedPassword);

    if (!areMatching)
    {
        failMessage.style.display = "block";
    }
    else
    {
        failMessage.style.display = "none";
    }
}

function deactivateButton()
{
    const button = document.getElementById("savePassword");
    button.setAttribute("disabled", true);
    button.classList.add("unclickable");
    document.getElementById("cursorWrap").style.cursor = "not-allowed";
}

function activateButton()
{
    const button = document.getElementById("savePassword");
    button.removeAttribute("disabled");
    button.classList.remove("unclickable");
    document.getElementById("cursorWrap").style.cursor = "";
}

document.getElementById("password").addEventListener("input", function()
{
    const password = document.getElementById("password").value;
    const failMessage = document.getElementById("failedPassword");
    isValid = passwordRegex.test(password);

    if (!isValid)
    {
        failMessage.style.display = "block";
    }
    else
    {
        failMessage.style.display = "none";
        checkMatch();
    }
    checkMatch();
    enable();
});

document.getElementById("confirmPassword").addEventListener("input", function()
{
    checkMatch();
    enable();
});
enable();