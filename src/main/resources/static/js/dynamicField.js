document.addEventListener("DOMContentLoaded", function() 
{
    const textArea = document.getElementById("inputArea");
    textArea.addEventListener("input", function()
    {
        textArea.style.height = "auto";
        textArea.style.height = Math.max(textArea.scrollHeight) + "px"
    });
});