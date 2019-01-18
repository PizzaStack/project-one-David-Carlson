"use strict";
console.log("employeeHome.js started");

function startEmployeeHome() {
    console.log("emp onLoad started");
    function turnIntoPendingRow(pRow) {
        return `
            <tr>
                <th scope="row">${ pRow.id}</th>
                <td>${ pRow.item_name}</td>
                <td>${ pRow.item_price}</td>
            </tr>`;
    }
    function turnIntoResolvedRow(resolvedRow) {
        var reim = resolvedRow.reimbursement;
        var manager = resolvedRow.resolver;
        return `
            <tr>
                <th scope="row">${ reim.id}</th>
                <td>${ reim.item_name}</td>
                <td>${ reim.item_price}</td>
                <td>${ reim.state}</td>
                <td>${ manager.lastName}, ${ manager.firstName}</td>
            </tr>`;
    }
    function fillPending(pendingResponse) {
        var bodyHTML = "";
        for (var i = 0; i < pendingResponse.length; i++) {
            bodyHTML += turnIntoPendingRow(pendingResponse[i]);
        }
        console.log(bodyHTML);
        console.log('is pending');
        pendingBody.innerHTML = bodyHTML;
    }
    function fillResolved(resolvedResponse) {
        var bodyHTML = "";
        console.log(resolvedResponse);
        console.log("is rResponse");
        for (var i = 0; i < resolvedResponse.length; i++) {
            bodyHTML += turnIntoResolvedRow(resolvedResponse[i]);
        }
        console.log(bodyHTML);
        console.log('is pending');
        resolvedBody.innerHTML = bodyHTML;
    }

    function getReimbursements() {
        var XHR = new XMLHttpRequest();
        XHR.responseType = 'json';
        XHR.onreadystatechange = function(){
            switch (this.readyState){
                case 4:
                    if (this.status === 200) {
                        fillPending(this.response.pendingReimbursements);
                        fillResolved(this.response.resolvedReimbursements);
                    }

                    else if (this.status >= 400 || this.status < 500) {
                        alert("Login warning");
                        // var warning = document.getElementById('password-warning');
                        // warning.classList.remove('d-none');
                    }
                    break;
            }
        };
        // Set up our request
        XHR.open("GET", "http://localhost:8080/reimbursements/me", true);

        // The data sent is what the user provided in the form
        XHR.send(null);
    }
    var pendingBody = document.getElementById('pending-tbody');
    var resolvedBody = document.getElementById('resolved-tbody');
    // Do xml call. on finish,
    getReimbursements();

};


// https://www.htmlgoodies.com/beyond/javascript/article.php/3724571/Using-Multiple-JavaScript-Onload-Functions.htm
addLoadEvent(startEmployeeHome);
function addLoadEvent(func) {
    var oldonload = window.onload;
    if (typeof window.onload != 'function') {
        window.onload = func;
    } else {
        window.onload = function() {
            if (oldonload) {
                oldonload();
            }
            func();
        }
    }
}
