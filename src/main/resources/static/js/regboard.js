$(document).ready(function () {
    var currentNum = {num: 0};
    $('.gk-flat').click(function clickFlat(th) {
        var flat = constructFlatObject($(th));
        $('#porchNumber').val(flat.porch);
        $('#floorNumber').val(flat.floor);
        $('#flatNumber').val(flat.flatNumber);
    }).disableSelection().mouseenter(function () {
        $('body').css({cursor: 'pointer'})
    }).mouseleave(function () {
        $('body').css({cursor: 'auto'})
    });

    $.templates("flatTemplate", "#flatTemplate");
    $('#add-flat').click(function () {
        currentNum.num = currentNum.num + 1;
        var html = $.render.flatTemplate(currentNum);
        $('#gk-reg-flats').append(html);
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
    })


});

function fillPorchChooser(caller, houseNum) {
    var jsonData;
    $.ajax({
        url: '/rest/house/' + houseNum + '/porches',
        success: function (data) {
            console.log(data.length);
            //$('#div_porch').html(data);
            jsonData = data;
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
            var div = caller.find('.div_show_flat')
            div.html(data);

        }
    });
}