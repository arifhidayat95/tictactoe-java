$(document).ready(function () {
    let turn = 'X';

    let score = {
        X: 0,
        O: 0,
        Tie: 0
    };

    $('.box').on('click', function () {
        if($(this).hasClass('disabled')){
            alert("Already selected");
        }else{
            columnClick($(this));

            const playerModel = $('#playerModel').val();

            if(playerModel === 'com'){
                const data = document.querySelectorAll('.square:not(.disabled)');
                const selected = Math.floor(Math.random()*data.length);
                columnClick(data[selected]);
            }

            setResult();
        }
    });

    $("#resetGame").on('click', function () {
        reset();
    });

    function getResult(boxValue) {
        return $.ajax({
            url: "/api/result",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({box: boxValue})
        });
    }

    function setResult() {
        const data = $('.box');
        const gameModel = $('#gameModel').val();
        const boxValue = [];

        for(let x = 0; x<gameModel; x++){
            const row = [];
            for(let y = 0; y<gameModel; y++){
                row.push(data[x*gameModel+y].value);
            }
            boxValue.push(row);
        }

        const result = getResult(boxValue);

        result.done(function (response) {
            if(response.message === "Success"){
                if(result.win === "Tie"){
                    alert("Its a tie. It will restart.");
                }else{
                    alert(response.win+" has won the game. Start a new game");

                    switch (response.win) {
                        case 'X':
                            score.X += 1;
                            $("#scoreX").text(score.X);
                            break;
                        case 'O':
                            score.O += 1;
                            $("#scoreO").text(score.O);
                            break;
                        case 'Tie':
                            score.Tie += 1;
                            $("#scoreTie").text(score.Tie);
                            break;
                    }
                }
                reset();
            }
        })
    }

    function reset() {
        $('.box').removeClass('btn-primary btn-danger disabled').text('+').val('+');
    }

    function columnClick(e) {
        const colorClass = turn === 'X' ? 'btn-primary' : 'btn-danger';
        $(e).addClass(colorClass+' disabled').text(turn).val(turn);

        turn = turn !== 'X' ? 'X' : 'O';
        $('#turn').text(turn);
    }
});