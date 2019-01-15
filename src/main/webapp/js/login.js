"use strict";

window.addEventListener("load", function () {
    function sendData() {
        var XHR = new XMLHttpRequest();

        // Bind the FormData object and the form element
        var FD = new FormData(form);
        console.log("Data: " + FD.toString());

        // Define what happens on successful data submission
        XHR.addEventListener("load", function(event) {
            alert(event.target.responseText);
        });

        // Define what happens in case of error
        XHR.addEventListener("error", function(event) {
            alert('Oops! Something went wrong.');
        });

        // Set up our request
        XHR.open("POST", "http://localhost:8080/servlet-demo/hello");

        // The data sent is what the user provided in the form
        XHR.send(FD);
    }

    // Access the form element...
    var form = document.getElementById("login");

    // ...and take over its submit event.
    form.addEventListener("submit", function (event) {
        event.preventDefault();

        sendData();
    });
});
