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

document.addEventListener("DOMContentLoaded", function()
{
    const button = document.getElementById("ticketEraser");
    const answer = button.dataset.status;
    const requestURI = button.dataset.uri;
    if (!requestURI.startsWith("/adminpanel") && answer === 'ANSWERED')
    {
        button.setAttribute('disabled', true);
        document.getElementById("cursorWrap").style.cursor = "not-allowed";
    }
});

document.addEventListener("DOMContentLoaded", function() {
    const ticketResolverButton = document.getElementById("ticketResolver");
    if (ticketResolverButton) 
    {
        var answer = ticketResolverButton.dataset.status;
            if (answer === 'ANSWERED') 
            {
                ticketResolverButton.setAttribute('disabled', true);
                var cursorWrap = document.getElementById("cursorWrap");
                if (cursorWrap) 
                {
                    cursorWrap.style.cursor = "not-allowed";
                }
            }
        ticketResolverButton.addEventListener("click", function() {
            const ticketId = this.dataset.ticketId;
            const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
            const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

            const xhr = new XMLHttpRequest();
            xhr.open('POST', '/adminpanel/tickets/' + ticketId + '/update', true);
            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.setRequestHeader(csrfHeader, csrfToken);
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4)
                {
                    if (xhr.status === 200)
                    {
                        alertPop(xhr.responseText, function()
                        {
                            window.location.href = '/adminpanel/tickets';
                        });
                    }
                    else
                    {
                        console.log("An error has occurred.");
                    }
                }
            };
            xhr.send();
        });
    }
});

document.getElementById("ticketEraser").addEventListener("click", function() {
    const requestURI = this.dataset.uri;
    const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
    confirmationPop("Are you sure you want to delete this ticket?", function(confirmedByUser)
    {
        if (confirmedByUser)
        {
            const xhr = new XMLHttpRequest();
            xhr.open('DELETE', requestURI + '/delete', true);
            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.setRequestHeader(csrfHeader, csrfToken);
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4)
                {
                    if (xhr.status === 200)
                    {
                        alertPop(xhr.responseText, function()
                        {
                            if (requestURI.startsWith("/adminpanel"))
                            {
                                window.location.href = '/adminpanel/tickets';
                            }
                            else
                            {
                                window.location.href = '/tickets';
                            }
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