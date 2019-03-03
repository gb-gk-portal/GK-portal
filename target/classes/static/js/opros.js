$(document).ready(function () {
    $('label:contains("Проголосовал. Бюллетень передал/передам в УК")').each(function () {
        var forInput = $(this).attr('for');
        $('#' + forInput).on('change', function () {
            $(this).parents('.form-group').nextAll().find('input[type = radio]').prop({
                'disabled': false,
                'checked': false
            });
        })
    });
    $('label:contains("Я не принимаю участия в голосовании")').each(function () {
        var forInput = $(this).attr('for');
        console.log(forInput);
        $('#' + forInput).on('change', function () {
            $('label:contains("Я не принимаю участия в голосовании")').each(function () {
                var forInput = $(this).attr('for');
                console.log(forInput);
                var radio = $('#' + forInput);
                radio.parents('.form-group').find('input[type = radio]').prop('disabled', true);
                radio.prop('disabled', false).prop('checked', true);
            }).first().parents('.form-group').find('input[type = radio]').prop('disabled', false);

        });


    });
});
