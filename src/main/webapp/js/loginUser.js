"use strict";

window.addEventListener("load", function () {
    console.log("on load");
    function sendData() {
        var XHR = new XMLHttpRequest();

        // Bind the FormData object and the form element
        var FD = new FormData(form);
        for (var p of FD)
            console.log(p + " is there");

        // Define what happens on successful data submission
        XHR.addEventListener("load", function(event) {
            alert(event.target.responseText);
        });

        // Define what happens in case of error
        XHR.addEventListener("error", function(event) {
            alert('Oops! Something went wrong.');
        });

        XHR.onreadystatechange = function(){
            if(this.readyState === 4 && this.status === 200){
                alert("SUCCESS");
            }
        };

        // Set up our request
        XHR.open("POST", "hello");

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
});
