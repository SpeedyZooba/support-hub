const queryParams = new URLSearchParams(window.location.search);
const failMessage = document.getElementById("wrongOldPassword");
if (queryParams.has('error'))
{
    failMessage.style.display = "block";
}
else
{
    failMessage.style.display = "none";
}

var isValid = false;
var areMatching = false;
var isPresent = false;
const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[+.$])(?!.*\s).{8,}$/;

function enable()
{
    if (isValid && areMatching && isPresent)
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
    var password = document.getElementById("password").value;
    var retypedPassword = document.getElementById("confirmPassword").value;
    var failMessage = document.getElementById("wrongPassword");
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
    var button = document.getElementById("savePassword");
    button.setAttribute("disabled", true);
    button.classList.add("unclickable");
    document.getElementById("cursorWrap").style.cursor = "not-allowed";
}

function activateButton()
{
    var button = document.getElementById("savePassword");
    button.removeAttribute("disabled");
    button.classList.remove("unclickable");
    document.getElementById("cursorWrap").style.cursor = "";
}

document.getElementById("oldPassword").addEventListener("input", function()
{   
    var password = document.getElementById("oldPassword").value;

    if (!password || password === "")
    {
        isPresent = false;
    }
    else
    {
        isPresent = true;
    }
    enable();
});

document.getElementById("password").addEventListener("input", function()
{
    var password = document.getElementById("password").value;
    var failMessage = document.getElementById("failedPassword");
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