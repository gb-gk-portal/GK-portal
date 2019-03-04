var lastQuestionIndex = 1;
var templateVars = {questionIndex: lastQuestionIndex, answerIndex: 2};

$(document).ready(function () {
    lastQuestionIndex = $('.question-template').length;

    // из html шаблона по id #questionTemplate создаём метод qTemplate,
    // куда можно передать объект, содержащий необходимые переменные,
    // и он вернёт уже обработанный текст.
    $.templates("qTemplate", "#questionTemplate");
    $.templates("aTemplate", "#answerTemplate");

    $('#add-question').on("click", function () {
        templateVars.questionIndex = lastQuestionIndex;
        templateVars.answerIndex = 0;
        var currentAnswerBlock = $.render.qTemplate(templateVars);
        lastQuestionIndex++;
        var $question = $(currentAnswerBlock); // создание из строкового объекта объекта jQuery

        for (var i = 0; i < 2; i++) {
            var answer = $.render.aTemplate(templateVars);
            templateVars.answerIndex++;
            $question.children('.add-answer-button').before(answer);
        }
        $('#question-group').append($question);

    });

    // листнер на весь блок вопросов с выборкой по кнопкам.
    // Дает возможность не вешать отдельные листнеры на кнопки при добавлении блока
    $('#question-group').on("click", '.add-answer-button', function () {
        var questionBlock = $(this).parent();
        templateVars.questionIndex = parseInt(questionBlock.attr('questionnumber'));
        templateVars.answerIndex = parseInt(questionBlock.children('.answer-container').last().attr('answerIndex')) + 1;
        var answer = $.render.aTemplate(templateVars);
        $(this).before(answer);

    });

});



