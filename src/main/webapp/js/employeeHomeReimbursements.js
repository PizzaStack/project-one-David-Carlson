"use strict";
console.log("employeeHomeReimbursements.js started");

function startEmployeeHome() {
    console.log("startEmployeeHome");
    var pendingBody = document.getElementById('pending-tbody');
    var resolvedBody = document.getElementById('resolved-tbody');
    getReimbursements();
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
        XHR.open("POST", "http://localhost:8080/reimbursements/me", true);

        // The data sent is what the user provided in the form
        XHR.send(null);
    }

    function fillPending(pendingResponse) {
        if (pendingResponse.length == 0 ) {
            $('#pending-table').remove();
            $('#pending-title').text('No pending reimbursements!');
            return;
        }
        else {
            var bodyHTML = "";
            for (var i = 0; i < pendingResponse.length; i++) {
                bodyHTML += turnIntoPendingRow(pendingResponse[i]);
            }
            console.log(bodyHTML);
            console.log('is pending');
            pendingBody.innerHTML = bodyHTML;
        }
    }
    function fillResolved(resolvedResponse) {
        if (resolvedResponse.length == 0 ) {
            $('#resolved-table').remove();
            $('#resolved-title').text('No resolved reimbursements :(');

        }
        else {
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
    }

    function turnIntoPendingRow(pRow) {
        var purpose='pending';
        // let base64String = btoa(String.fromCharCode(...new Uint8Array(pRow.receipt_image)));
        let imageString = getImage[pRow.id];
        return `
            <tr>
                <th scope="row">${ pRow.id}</th>
                <td>${ pRow.item_name}</td>
                <td>${ pRow.item_price}</td>
                <td><a class="btn btn-primary" data-target="#modalIMG-${purpose}${pRow.id}" data-toggle='modal' href="'#'">View Receipt</a></td>
                ${addInvisibleModal(purpose, pRow.id, imageString)}
            </tr>`;
    }
    function turnIntoResolvedRow(rRow) {
        var reim = rRow.reimbursement;
        var manager = rRow.resolver;
        var purpose='resolved';
        // let base64String = btoa(String.fromCharCode(...new Uint8Array(rRow.receipt_image)));
        let imageString = getImage[rRow.id];
        return `
            <tr>
                <th scope="row">${ reim.id}</th>
                <td>${ reim.item_name}</td>
                
                <td>${ reim.item_price}</td>
                <td>${ reim.state}</td>
                <td>${ manager.lastName}, ${ manager.firstName}</td>
                <td><a class="btn btn-primary" data-target="#modalIMG-${purpose}${rRow.id}" data-toggle='modal' href="'#'">View Receipt</a></td>
                ${addInvisibleModal(purpose, rRow.id, imageString)}
            </tr>`;
    }
    // https://i3.ytimg.com/vi/vr0qNXmkUJ8/maxresdefault.jpg
    function addInvisibleModal(purpose, id, base64Img) {
        return `
            <div aria-hidden="true" aria-labelledby="myModalLabel" class="modal fade" id="modalIMG-${purpose}${id}" role="dialog" tabindex="-1">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-body mb-0 p-0">
                            <img class="modal-${id}" src='${base64Img}' alt="" style="width:100%">
                        </div>
                        <div class="modal-footer">
                            <!--<div><a href="https://i3.ytimg.com/vi/vr0qNXmkUJ8/maxresdefault.jpg" target="_blank">Download</a></div>-->
                            <button class="btn btn-outline-primary btn-rounded btn-md ml-4 text-center" data-dismiss="modal" type="button">Close</button>
                        </div>
                    </div>
                </div>
            </div>`;
    }

    var pendingBody = document.getElementById('pending-tbody');
    var resolvedBody = document.getElementById('resolved-tbody');
    var getImage = {
        1 : "https://www.printit4less.com/images/custom-receipts-sml-rec.jpg",
        2 : "https://receipthog.zendesk.com/hc/article_attachments/211630668/FAQ_CC_Stub.png",
        3 : "https://d85ecz8votkqa.cloudfront.net/support/help_center/Print_Payment_Receipt.JPG",
        4 : "https://media.treehugger.com/assets/images/2018/11/stack_of_receipts.jpg.860x0_q70_crop-scale.jpg",
        5 : "https://timedotcom.files.wordpress.com/2015/12/147205787.jpg",
        6 : "https://www.thebillfold.com/wp-content/uploads/2016/04/1af7r52mmXsEGqlpeM0mhiQ.jpeg",
        7 : "https://receipthog.zendesk.com/hc/article_attachments/211630688/FAQ_Movie_Stub.png",
        8 : "https://www.freeagent.com/components/images/netlify/blog__will-hmrc-accept-scanned-receipts-for-business-expenses__receipt-shoebox-1-blog-21db8305.png"

    }

    // let base64String = btoa(String.fromCharCode(...new Uint8Array(json.receipt)));
    // $("td img").attr('src', `data:image/png;base64,${base64String}`);
}

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
