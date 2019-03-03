$(document).ready(function () {

    $('#edit').click(function () {
        $('.gk-flat-holder').sortable({
            items: '> .gk-flat',
            connectWith: '.gk-flat-holder',
            cursor: 'move',
            stop: function (event, ui) {
                // var posX = ui.item.parent().children().index(ui.item);
                $('.gk-flat-holder').each(function () {
                    if ($(this).children().length > 1) {
                        $(this).css({
                            'background-color': 'red',
                            'border-color' : 'red'
                        })
                    } else {
                        $(this).css({
                            'background-color': 'inherit',
                            'border-color' : 'inherit'
                        });
                    }
                });
                const flat = ui.item;
                if (!(flat.parent().children().length>1)){
                    var flatObject = constructFlatObject(flat);
                    $('#area').text(JSON.stringify(flatObject));
                    $.ajax({
                        type: "PUT",
                        url: "/rest/flat",
                        contentType: "application/json; charset=utf-8",
                        async: true,
                        data : JSON.stringify(flatObject),


                    }).fail(function () {
                        alert("Что-то пошло не так!");

                    })
                }
            },
        }).disableSelection();
    });
    $('#stopEdit').click(function () {
        $('.gk-flat-holder').sortable('destroy');
    })

});



