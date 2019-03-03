$(document).ready(function () {
    $('.question-container').each(function () {
        var dataSet = new Array();
        $(this).find('tbody tr').each(function () {
            var answer = $(this).find('.answer-name').text();
            var count = $(this).find('.answer-count').text();
            var dataItem = {label: answer, data: count};
            dataSet.push(dataItem);
        });
        var targetdiv=$(this).find('.diogram');
        var options = {
            series: {
                pie: {
                    show: true
                }
            }
        };
        console.log(dataSet);
        $.plot(targetdiv, dataSet, options);
        //$(this).find('table').css('display' , 'none');
    })
})