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

document.getElementById("noticeEraser").addEventListener("click", function() {
    var noticeId = this.dataset.noticeId;
    var csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
    var csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
    confirmationPop("Are you sure you want to remove this notice?", function(confirmedByUser)
    {
        if (confirmedByUser)
        {
            var xhr = new XMLHttpRequest();
            xhr.open('DELETE', '/adminpanel/notices/' + noticeId + '/delete', true);
            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.setRequestHeader(csrfHeader, csrfToken);
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4)
                {
                    if (xhr.status === 200)
                    {
                        alertPop(xhr.responseText, function()
                        {
                            window.location.href = '/adminpanel/notices';
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
    })
});