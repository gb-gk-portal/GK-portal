// https://datatables.net/


$.fn.dataTable.render.answerResult = function () {
    return function (data) {
        if (data != null) return data;
        else return "-";
    };
};


$(document).ready(function () {
    var table = $('#resultTable').DataTable({
        ajax: {
            "url": "/rest/questionnaire-result?questionnaireId=bb2248ae-2d7e-427d-85ef-7b85888f0319",
            "dataSrc": ""
        },
        deferRender: true,
        language: {
            "url": "//cdn.datatables.net/plug-ins/1.10.19/i18n/Russian.json"
        },
        lengthMenu: [[15, 30, 50, -1], [15, 30, 50, "Все"]],
        // order: [0, 'asc'],
        ordering: false,
        fixedHeader: {
            "footer": "false"
        },
        columns:
            [
                {
                    data: "contactDTO",
                    render: function (data, type, row) {
                        return data.lastName + ' ' + data.firstName + ' ' + data.middleName + ' ';
                    }
                },

                {
                    data: "contactDTO",
                    render: function (data, type, row) {
                        var res = "";
                        $.each(data.communications, function (index, communication) {
                            switch (communication.communicationType.description) {
                                case 'Email':
                                    res += "<a href=\"mailto:";
                                    break;
                                case 'Телефон':
                                    res += "<a href=\"tel:+7";
                                    break;
                                default:
                                    break;
                            }
                            res += communication.identify + "\">" + communication.identify + "</a></br>";
                        });
                        return res;
                    }
                },

                {
                    data: "contactDTO",
                    render: function (data, type, row) {
                        var res = "";

                        $.each(data.ownerships, function (index, ownership) {
                            var style = "<i class=\"";
                            switch (ownership.ownershipType.name) {
                                case 'Квартира':
                                    style += "far fa-building\"></i>";
                                    break;
                                case 'Машиноместо':
                                    style += "fas fa-parking\"></i>";
                                    break;
                                case 'Автомобиль':
                                    style += "fas fa-car\"></i>";
                                    break;
                                case 'Нежилое помещение':
                                    style += "fas fa-briefcase\"></i>";
                                    break;
                                default:
                                    break;
                            }

                            res += '<p>' + style + '</br>' +
                                ownership.houseBuildNum + ' корпус, ' + '</br>' +
                                ownership.buildNumber + ' объект, ' + '</br>' +
                                '(S' + ownership.square + 'м<sup>2</sup>, ' + ownership.percentageOfOwner + '%)' +
                                '</p>';
                        });
                        return res;
                    }
                },

                {
                    "data": "integerAnswerResultDTO1Map.2",
                    "render": $.fn.dataTable.render.answerResult()
                },

                {
                    "data": "integerAnswerResultDTO1Map.3",
                    "render": $.fn.dataTable.render.answerResult()
                },

                {
                    "data": "integerAnswerResultDTO1Map.4",
                    "render": $.fn.dataTable.render.answerResult()
                },

                {
                    "data": "integerAnswerResultDTO1Map.5",
                    "render": $.fn.dataTable.render.answerResult()
                },

                {
                    "data": "integerAnswerResultDTO1Map.6",
                    "render": $.fn.dataTable.render.answerResult()
                },

                {
                    "data": "contactDTO.questionnaireContactConfirm.confirmed",
                    "render": function (data, type, row) {
                        if (data) return 'да';
                        else return 'нет';
                    }
                },

                {
                    "data": "contactDTO.questionnaireContactConfirm",
                    "render": function (data, type, row, meta) {
                        if (type === 'sort' || type === 'filter') {
                            return data.questionnaireConfirmedType.name;
                        } else {
                            var classNotFind = "btn-outline-secondary";
                            var classMatches = "btn-outline-success";
                            var classNotMatch = "btn-outline-danger";
                            var res = "";
                            switch (data.questionnaireConfirmedType.name) {
                                case 'не искали':
                                    classNotFind = "btn-secondary";
                                    break;
                                case 'совпадает':
                                    classMatches = "btn-success";
                                    break;
                                case 'различается':
                                    classNotMatch = "btn-danger";
                                    break;
                                default:
                                    break;
                            }
                            res += '<button type="button" class="btn ' + classNotFind + ' add-item-btn" confirmId="' + data.uuid + '" typeId="1b4976f3-8a81-45d0-b3b1-9f31dfafaad5">не искали</button>';
                            res += '<button type="button" class="btn ' + classMatches + ' add-item-btn" confirmId="' + data.uuid + '" typeId="6973e035-5196-48ed-bfba-2ca2935d47da">совпадает</button>';
                            res += '<button type="button" class="btn ' + classNotMatch + ' add-item-btn" confirmId="' + data.uuid + '" typeId="fff634b9-f5ae-4342-9135-1c086b75c39a">различается</button>';

                            return res;
                        }


                    }
                }

            ],
        "createdRow": function (row, data, dataIndex) {
            if (data.contactDTO.questionnaireContactConfirm.confirmed === false) {
                $(row).addClass('lightpink');
            }
        }
    });

    $('#resultTable thead tr').clone(true).appendTo('#resultTable thead');
    $('#resultTable thead tr:eq(1) th').each(function (i) {
        $(this).html('<input type="text" placeholder="Поиск"/>');

        $('input', this).on('keyup change', function () {
            if (table.column(i).search() !== this.value) {
                table
                    .column(i)
                    .search(this.value)
                    .draw();
            }
        });
    });
    $('#resultTable').on('click', '.add-item-btn', function questionnaireConfirmedTypesListener() {
            var confirmId = $(this).attr('confirmId');
            var typeId = $(this).attr('typeId');
            $.ajax({
                url: "/rest/change-questionnaireConfirmedType",
                type: "POST",
                data: {
                    "questionnaireContactConfirmId": confirmId,
                    "questionnaireConfirmedTypeId": typeId
                },
                complete: function (e, data, xhr, settings) {
                    if (e.status === 200) {
                        table.ajax.reload();
                    } else if (e.status === 204) {
                        alert('контент не найден');
                    } else if (e.status === 403) {
                        alert('доступ запрещен');
                        window.location.href = "/login"
                    } else if (e.status === 302) {
                        alert('необходимо авторизироваться');
                    } else {
                        alert('неизвестная ошибка, код ' + e.status);
                    }
                }
            })
        }
    );
});

