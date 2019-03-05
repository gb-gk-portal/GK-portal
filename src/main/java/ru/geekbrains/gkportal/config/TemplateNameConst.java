package ru.geekbrains.gkportal.config;

import org.springframework.ui.Model;

public class TemplateNameConst {

    public static final String TEMPLATE_MAIN = "main_template";

    // ============= Главная страницв =========================
    public static final String INDEX_FORM = "index";
    public static final String LOGIN_FORM = "login";

    // ============= обычная регистрация =======================
    // Форма подтверждения аккаунта или опроса по почте
    public static final String REGISTRATION_CONFIRM_MAIL_FORM = "registration/confirm-mail";
    // Форма полной регистрации
    public static final String REGISTRATION_FULL_FORM = "registration/reg-form";
    // Форма быстрой регистрации
    public static final String REGISTRATION_FAST_FORM = "registration/reg-form-fast";
    // Форма регистрация завершена, проверьте почту
    public static final String REGISTRATION_SUCCESS_FROM = "registration/reg-success";
    // Письмо для подтверждения регистрации
    public static final String REGISTRATION_MAIL = "registration/mail/registration-mail";

    // ============= регистрация с опросом ======================
    //Форма создания опроса
    public static final String QUESTIONNAIRE_ADD_FORM = "questionnaire/add-questionnaire";
    // Форма регистрации с опросом
    public static final String REGISTRATION_QUESTIONNAIRE_FORM = "registration/reg-question-form";
    // Письмо для подтверждения регистрации с опросом
    public static final String REGISTRATION_QUESTIONNAIRE_MAIL = "registration/mail/registration-question-mail";
    // ============= опросы ==================================
    // Форма результата запроса отправки повторного подтверждающего письма о голосовании
    public static final String QUESTIONNAIRE_CONFIRM_MAIL = "questionnaire/request-confirm-mail";
    // форма результата опроса
    public static final String QUESTIONNAIRE_RESULT_FORM = "questionnaire-result/result";
    // форма результата опроса с поиском
    public static final String QUESTIONNAIRE_RESULT_FIND_FORM = "questionnaire-result/datatable";
    // Форма регистрация с опросом завершена, проверьте почту
    public static final String REGISTRATION_QUESTIONNAIRE_SUCCES_FORM = "registration/reg-question-success";
    // список голосований
    public static final String QUESTIONNAIRE_LIST_FORM = "questionnaire/questionnaire";
    public static final String QUESTIONNAIRE_PIE = "questionnaire/pie-diog";
    // ============= ШАХМАТКА ===================================
    // форма шахматка подъезда
    public static final String SCHEME_PORCH_FORM = "scheme/porch-form";
    // = Админка =
    // просмотр/редактирование шахматки дома
    public static final String SCHEME_ADMIN_EDIT_HOUSE_FORM = "scheme/edit-house-form";
    // ============= Личный кабинет =====================================
    // Главная форма
    public static final String LK_MAIN_FORM = "lk/lk";
    // результаты опросов в лк
    public static final String LK_QUESTIONNAIRE_RESULT = "lk/lk-questionnaire-answer-result";
    // шаблон письма от пользователя к пользователю
    public static final String LK_MESSAGE_USER_TO_USER_MAIL = "lk/lk-message-user-to-user";
    // форма отправки сообщений соседям
    public static final String LK_NEIGHBORS_MESSAGE_FORM = "lk/lk-neighbors-message";
    // шахматка для ЛК
    public static final String LK_SHOW_FLATS = "lk/lk-show-flats";

    /**
     * Автоматизация возврата шаблона
     *
     * @param model    Модель для помещения нужных данных
     * @param template Имя шаблона для включения в общий шаблон
     * @return Имя общего шаблона
     */
    public static String returnShablon(Model model, String template) {
        model.addAttribute("content", template);
        return TEMPLATE_MAIN;
    }


}
