/**
 * Created by rlarl on 2017-01-19.
 */

if (!('webkitSpeechRecognition' in window)) {
    $('#recognition_result').text('지원하지 않는 브라우저입니다.');
}
else {
    var mic = new webkitSpeechRecognition();
    mic.continuous = false;
    mic.interimResults = true;
    mic.lang = 'ko-KR';
    //영어 : 'en-US'
    mic.onresult = function (e) {
        var textResult = '', c = false;
        for (var i = e.resultIndex; i < e.results.length; ++i) {
            textResult += e.results[i][0].transcript;
            c = e.results[i].isFinal;
        }
        if ($('#recognition_result .cning').length < 1)
            $('#recognition_result').html('<span class="cning"></span>');
        $('#recognition_result .cning').text(textResult);
        if (textResult == '사과') {
            ws.send('!ON?');
        }
        else if (textResult == '포도') {
            ws.send('!OFF?');
        }
        else if (textResult == '바나나') {
            ws.send('!REVERSELED?');
        }
    };
    mic.onend = function () {
        $('#recognition_btn').text('시작하기');
        nowRecognizing = false;
    };
    mic.onstart = function () {
        $('#recognition_btn').text('말씀하세요');
        nowRecognizing = true;
    };
}
var nowRecognizing = false;
function onclickB() {
    if (nowRecognizing) {
        mic.stop();
    }
    else {
        mic.start();
    }
}