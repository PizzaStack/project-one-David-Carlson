"use strict";
function startMatrixScroll() {
    var canvas = document.getElementById("matrix-canvas");
    var color = document.getElementsByTagName('body')[0].id;
    var refreshColor, textColor;
    switch(color) {        
        case 'red':
            refreshColor = 'rgba(0, 0, 0, 0.05)';
            textColor = '#822222';
            break;
        case 'purple':
            refreshColor = 'rgba(0, 0, 0, 0.05)';
            textColor = '#451e3e';
            break;
        default:
        case 'green':
            refreshColor = 'rgba(0, 0, 0, 0.05)';
            textColor = '#0F0';
            break;
    }

    var s = window.screen;
    var width = canvas.width = s.width;
    var height = canvas.height = s.height;
    var letters = Array(256).join(1).split('');
    var text = "";
    var x_pos = 0;

    var draw = function () {
        canvas.getContext('2d').fillStyle=refreshColor;
        canvas.getContext('2d').fillRect(0,0,width, 4000);
        canvas.getContext('2d').fillStyle=textColor;
        letters.map(function(y_pos, index){
            text = String.fromCharCode(3e4+Math.random()*33);
            x_pos = index * 10;
            canvas.getContext('2d').fillText(text, x_pos, y_pos);
            letters[index] = (y_pos > 758 + Math.random() * 1e4) ? 0 : y_pos + 10;
        });
    };
    setInterval(draw, 40);
}

// https://www.htmlgoodies.com/beyond/javascript/article.php/3724571/Using-Multiple-JavaScript-Onload-Functions.htm
addLoadEvent(startMatrixScroll);
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
