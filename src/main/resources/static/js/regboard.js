$(document).ready(function () {
    var currentNum = {num: 0};
    currentNum.num=$('#gk-reg-flats').children('.gk-flat-chooser').length-1;
    var opsList = $('#gk-reg-flats').children('.gk-flat-chooser').first().find('.gk-select-house select').html();
    $.templates("flatTemplate", "#flatTemplate");
    $('#add-flat').click(function () {
        currentNum.num = currentNum.num + 1;
        var html = $.render.flatTemplate(currentNum);
        $('#gk-reg-flats').children('.gk-flat-chooser').last().after(html);
        $('#gk-reg-flats').children('.gk-flat-chooser').last().find('.gk-select-house select').html(opsList).val(-3);
    });
    $('#del-flat').click(function () {
        if (currentNum.num==0) return;
        currentNum.num = currentNum.num -1;
        $('#gk-reg-flats').children('.gk-flat-chooser').last().remove();

    });
    $('#gk-reg-flats').on('change', '.gk-select-house select', function () {
        var caller = $(this).parents('.gk-flat-chooser');
        var houseNum = $(this).val();
        fillPorchChooser(caller, houseNum);
    });
    $('#gk-reg-flats').on('change', '.gk-select-porch select', function () {
        var caller = $(this).parents('.gk-flat-chooser');
        var houseNum = caller.find('.gk-select-house select').val();
        var porchNum = $(this).val();
        showFlatChooser(caller, houseNum, porchNum);
    });
    $('#gk-reg-flats').on('click', '.gk-flat', function () {
        var caller = $(this).parents('.gk-flat-chooser');
        var flat = constructFlatObject($(this));

        caller.find('.gk-reg-floor-number input').val(flat.floor);
        caller.find('.gk-reg-flat-number input').val(flat.flatNumber);
        $(this).parents('.div_show_flat').empty();
    })


});

function fillPorchChooser(caller, houseNum) {
    $.ajax({
        url: '/rest/house/' + houseNum + '/porches',
        success: function (data) {
            console.log(data.length);
            //$('#div_porch').html(data);
            var jsonData = data;
            console.log(jsonData);
            var selector = caller.find('.gk-select-porch select');
            selector.empty();
            selector.append('<option>Выбирай</option>');
            console.log(jsonData);
            for (var i = 0; i < jsonData.length; i++) {
                var option = '<option multiple="false" value="' + jsonData[i] + '">' + jsonData[i] + '</option>';
                selector.append(option);
            }
        }
    });

}

function showFlatChooser(caller, houseNum, porchNum) {
    $.ajax({
        url: '/showPorch/' + houseNum + '/' + porchNum + '/',
        success: function (data) {
            console.log(data);
            var div = caller.find('.div_show_flat');
            div.html(data);

        }
    });
}

function chooseFlat() {
    
}