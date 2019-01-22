"use strict";
console.log("employeeViewInfo.js started");

function startEmployeeHome() {
    function fillInfoForm(response) {
        $('#id-input').val(response.id);
        $('#username-input').val(response.username);
        $("#first-name-input").val(response.firstName);
        $('#last-name-input').val(response.lastName);
    }

    function getEmployeeInfo() {
        var XHR = new XMLHttpRequest();
        XHR.responseType = 'json';
        XHR.onreadystatechange = function(){
            switch (this.readyState){
                case 4:
                    if (this.status === 200) {
                        fillInfoForm(this.response);
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
        XHR.open("GET", "http://localhost:8080/personnel/employees/me", true);

        // The data sent is what the user provided in the form
        XHR.send(null);
    }

    getEmployeeInfo();

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
