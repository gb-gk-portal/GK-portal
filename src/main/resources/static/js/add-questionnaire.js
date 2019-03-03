var lastQuestionIndex=1;
var templateVars = {questionIndex :lastQuestionIndex, answerIndex : 2};

$(document).ready(function () {
    lastQuestionIndex = $('.question-template').length;
    $.templates("qTemplate", "#questionTemplate");
    $.templates("aTemplate", "#answerTemplate");
    $('#add-question').on("click", function () {
        templateVars.questionIndex=lastQuestionIndex;
        templateVars.answerIndex=0;
        var currentAnswerBlock = $.render.qTemplate(templateVars);
        lastQuestionIndex++;
        var $question = $(currentAnswerBlock);

        for(i=0; i<2;i++){
            var answer = $.render.aTemplate(templateVars);
            templateVars.answerIndex++;
            $question.children('.add-answer-button').before(answer);
        }
        $('#question-group').append($question);

    });
    $('#question-group').on("click", '.add-answer-button',function () {

        var questionBlock = $(this).parent();
        var questionId = parseInt(questionBlock.attr('questionnumber'));
        var answerIndex = parseInt(questionBlock.children('.answer-container').last().attr('answerIndex'));

        templateVars.questionIndex=questionId;
        templateVars.answerIndex=answerIndex+1;
        var answer = $.render.aTemplate(templateVars);
        $(this).before(answer);

    });

});



