$(document).ready(function () {
    google.charts.load("current", {packages:["corechart"]});
    google.charts.setOnLoadCallback(function () {
        $('.question-container').each(function () {
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Answer');
            data.addColumn('number', 'Count');
            var question=$(this).find('p').text();
            $(this).find('tbody tr').each(function () {
                var answer = $(this).find('.answer-name').text();
                var count = parseInt($(this).find('.answer-count').text());
                data.addRows([[answer, count]]);
            });
            var targetdiv=$(this).find('.diogram').get(0);
            var options = {
                title: question,
                is3D: true
            };
            var chart = new google.visualization.PieChart(targetdiv);
            chart.draw(data, options);
            $(this).find('p').hide();
            $(this).find('table').hide();

            //$(this).find('table').css('display' , 'none');
        })
    });

})