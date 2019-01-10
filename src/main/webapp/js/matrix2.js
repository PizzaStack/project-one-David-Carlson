"use strict";
(function() {
	var canvas = document.getElementById("matrix-canvas");
	var ctx = canvas.getContext('2d');
    var chineseCorpus = "紙断総抗述祭究速術普爆弥提収歌自藤総静支事間更必派署産希変要夫物投東長保像極堀経暮優雅賞習禁潜発世閣国日刊済航納変聞域求対海流総係歯言庭事将聞詳自進要特列供給永植載率体府見由図副食再細帳大庭必子漁課示絶器想態人生段当犯土合最打務毎能将首図化法末分見害際済芸誠速愛吉構物変料費庭海国";
    chineseCorpus = "⺐⺓⺗⺛⺝⺢⺧⺶";
    chineseCorpus = "abcdefghi123456790";
    var chinese = chineseCorpus.split("");

    var font_size = 10;
    var columns = canvas.width/font_size;
    var drops = [];
    for (var x = 0; x < columns; x++)
        drops[x] = 1;

    setInterval(draw, 33);

    function draw() {
        ctx.fillStyle = "rgba(0, 0, 0.05)";
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        ctx.fillStyle = "#0f0";
        ctx.font = font_size + "px arial";

        for (var i = 0; i < drops.length; i++) {
            var text = chinese[Math.floor(Math.random() + chinese.length)];
            // x = i * font_size, y = value of drops[i] * font_size
            ctx.fillText(text, i*font_size, drops[i] * font_size);
            if (drops[i] * font_size > canvas.height && Math.random() > 0.975)
                drops[i] = 0;
            drops[i]++;
        }

    }
}) ();

