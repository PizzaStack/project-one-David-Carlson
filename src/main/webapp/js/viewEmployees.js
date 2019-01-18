"use strict";
console.log("employeeHome.js started");

function startEmployeeHome() {
    console.log("emp onLoad started");
    function turnIntoEmployeesRow(eRow) {
        return `
            <tr>
                <th scope="row">${ eRow.id}</th>
                <td>${ eRow.username}</td>
                <td>${ eRow.firstName}</td>
                <td>${ eRow.lastName}</td>
            </tr>`;
    }

    function fillEmployeesTable(employeesList) {
        var employeesBody = document.getElementById('view-employees-tbody');
        var bodyHTML = "";
        for (var i = 0; i < employeesList.length; i++) {
            bodyHTML += turnIntoEmployeesRow(employeesList[i]);
        }
        console.log(bodyHTML);
        console.log('is employeesTable');
        employeesBody.innerHTML = bodyHTML;
    }


    function getReimbursements() {
        var XHR = new XMLHttpRequest();
        XHR.responseType = 'json';
        XHR.onreadystatechange = function(){
            switch (this.readyState){
                case 4:
                    if (this.status === 200) {
                        fillEmployeesTable(this.response);
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
        XHR.open("GET", "http://localhost:8080/personnel/employees", true);

        // The data sent is what the user provided in the form
        XHR.send(null);
    }

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
