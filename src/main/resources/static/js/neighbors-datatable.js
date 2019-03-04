$(document).ready(function () {
    var table = $('#flat-neighbors').DataTable({
        "language": {
            processing: "Подождите...",
            search: "Поиск:",
            lengthMenu: "Показать _MENU_ записей",
            info: "Записи с _START_ до _END_ из _TOTAL_ записей",
            infoEmpty: "Записи с 0 до 0 из 0 записей",
            infoFiltered: "(отфильтровано из _MAX_ записей)",
            infoPostFix: "",
            loadingRecords: "Загрузка записей...",
            zeroRecords: "Записи отсутствуют.",
            emptyTable: "В таблице отсутствуют данные",
            paginate: {
                first: "Первая",
                previous: "Предыдущая",
                next: "Следующая",
                last: "Последняя"
            },
            aria: {
                sortAscending: ": активировать для сортировки столбца по возрастанию",
                sortDescending: ": активировать для сортировки столбца по убыванию"
            }
        },

        "ajax": {
            "url": "/rest/lk/neighbors-flats/1",
            "dataSrc": ""
        },
        "columns": [
            {
                "data": "id",
                'targets': 0,
                'checkboxes': {
                    'selectRow': true
                }
            },
            {"data": "flatNumber"},
            {"data": "house"},
            {"data": "porch"},
            {"data": "floor"}

        ],
        'select': {
            'style': 'multi'
        },
        'order': [[1, 'asc']]
    });


    $('.menu-link').click(function () {
        var menuLink = $(this).attr('link');
        table.ajax.url('/rest/lk/neighbors-flats/' + menuLink).load();
    });


    $('#frm-flat-neighbors').on('submit', function (e) {
        var form = this;

        var rows_selected = table.column(0).checkboxes.selected();
        var msg = $('#message').val();

        if (rows_selected == null || rows_selected.length == 0) {
            alert("Необходимо отметить галочками номера квартир, операция будет отменена");
            return false;
        }

        if (msg == null || msg.length == 0) {
            alert("Сообщение не может быть пустым, операция будет отменена");
            return false;
        }

        msg = msg.substring(0, 10000);
        // console.log(rows_selected.join(","));

        $.ajax({
            type: "POST",
            url: "/rest/lk/neighbors-flats/selected",
            data: {
                "ids": rows_selected.join(","),
                "msg": msg
            }
        }).done(function () {
            alert("Сообщение отправлено");
        });


        // Remove added elements
        $('input[name="id\[\]"]', form).remove();

        // Prevent actual form submission
        e.preventDefault();

    });

});
