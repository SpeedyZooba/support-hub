document.addEventListener("DOMContentLoaded", function() {
    var button = document.getElementById("userEraser");
    var userId = button.dataset.userId;
    var ownerId = button.dataset.ownerId;
    if (userId === ownerId)
    {
        button.setAttribute('disabled', true);
        button.style.pointerEvents = 'none';
        button.style.opacity = '0.5';
        document.getElementById("cursorWrap").style.cursor = "not-allowed";
    }
});

function confirmationPop(message, callback)
{
    $('#confirmationModal .modal-body').text(message);
    $('#confirmationModal').modal('show');

    $('#confirmationModal').on('click', '#confirmButton', function()
    {
        callback(true);
        $('#confirmationModal').modal('hide');
    });
    $('#confirmationModal').on('click', '#cancelButton', function() 
    {
        callback(false);
        $('#confirmationModal').modal('hide');
    });
}

function alertPop(message, callback)
{
    $('#alertModal .modal-body').text(message);
    $('#alertModal').modal('show');
    $('#alertModal').on('hidden.bs.modal', function()
    {
        if (callback && typeof callback === 'function')
        {
            callback();
        }
    });
}

document.getElementById("userEraser").addEventListener("click", function() 
{
    var userId = this.dataset.userId;
    var csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
    var csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
    confirmationPop("Are you sure you want to remove this user?", function(confirmedByUser)
    {
        if (confirmedByUser)
        {
            var xhr = new XMLHttpRequest();
            xhr.open('DELETE', '/adminpanel/users/' + userId + '/delete', true);
            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.setRequestHeader(csrfHeader, csrfToken);
            xhr.onreadystatechange = function() 
            {
                if (xhr.readyState === 4)
                {
                    if (xhr.status === 200)
                    {
                        alertPop(xhr.responseText, function()
                        {
                            window.location.href = '/adminpanel/users';
                        });
                    }
                    else if (xhr.status === 403)
                    {
                        alertPop(xhr.responseText, function()
                        {
                            window.location.reload;
                        });
                    }
                    else
                    {
                        console.log("An error has occurred.");
                    }
                }
            };
            xhr.send();
        }
    });
});