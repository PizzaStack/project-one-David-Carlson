"use strict";
console.log("employeeHomeReimbursements.js started");

function startEmployeeHome() {
    console.log("emp onLoad started");
    function turnIntoPendingRow(pRow) {
        var purpose = 'pending';
        return `
            <tr>
                <th scope="row">${ pRow.id}</th>
                <td>${ pRow.item_name}</td>
                <td>${ pRow.item_price}</td>
                <td><a class="btn btn-primary" data-target="#modalIMG-${purpose}${pRow.id}" data-toggle='modal' href="'#'">View Receipt</a></td>
               ${addInvisibleModal('pending', pRow.id, 'soon to be image')}
            </tr>`;
    }
    function turnIntoResolvedRow(resolvedRow) {
        var reim = resolvedRow.reimbursement;
        var manager = resolvedRow.resolver;
        var purpose = 'resolved';
        return `
            <tr>
                <th scope="row">${ reim.id}</th>
                <td>${ reim.item_name}</td>
                <td>${ reim.item_price}</td>
                <td>${ reim.state}</td>
                <td>${ manager.lastName}, ${ manager.firstName}</td>
                <td><a class="btn btn-primary" data-target="#modalIMG-${purpose}${pRow.id}" data-toggle='modal' href="'#'">View Receipt</a></td>
               ${addInvisibleModal('pending', pRow.id, 'soon to be image')}
            </tr>`;
    }
    function addInvisibleModal(purpose, id, base64Img) {
        return `
            <div aria-hidden="true" aria-labelledby="myModalLabel" class="modal fade" id="modalIMG-${purpose}${id}" role="dialog" tabindex="-1">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-body mb-0 p-0">
                            <img src="https://i3.ytimg.com/vi/vr0qNXmkUJ8/maxresdefault.jpg" alt="" style="width:100%">
                        </div>
                        <div class="modal-footer">
                            <div><a href="https://i3.ytimg.com/vi/vr0qNXmkUJ8/maxresdefault.jpg" target="_blank">Download</a></div>
                            <button class="btn btn-outline-primary btn-rounded btn-md ml-4 text-center" data-dismiss="modal" type="button">Close</button>
                        </div>
                    </div>
                </div>
            </div>`;
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
        XHR.open("GET", "http://localhost:8080/reimbursements", true);

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
