"use strict";
console.log("Login.js started");

function startLogin() {

    function sendData() {
        var XHR = new XMLHttpRequest();

        // Bind the FormData object and the form element
        var FD = new FormData(form);
        alert(FD.toString());
        for (var p of FD)
            console.log(p.v + " is there");


        XHR.onreadystatechange = function(){
            switch (this.readyState){
                case 4:
                    if (this.status === 200) {

                        alert("yay");s
                    }

                    else if (this.status >= 400 || this.status < 500) {
                        alert("Login warning");
                        var warning = document.getElementById('password-warning');
                        warning.classList.remove('d-none');
                    }
                    break;
            }
        };
        // Add the required HTTP header for form data POST requests
        XHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        // Set up our request
        XHR.open("POST", "http://localhost:8080/servlet-demo/login", false);

        // The data sent is what the user provided in the form
        XHR.send(FD);
    }

    // Access the form element...
    var form = document.getElementById("login");

    // ...and take over its submit event.
    form.addEventListener("submit", function (event) {
        console.log("On submit");
        event.preventDefault();
        sendData();
    });
};

addLoadEvent(startLogin);
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
