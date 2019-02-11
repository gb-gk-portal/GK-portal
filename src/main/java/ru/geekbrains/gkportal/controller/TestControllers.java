package ru.geekbrains.gkportal.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.gkportal.service.MailService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestControllers {

    private MailService mailService;

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/email")
    public int sendMail() {
        mailService.sendRegistrationMail("admin@chertenok.ru");
        return HttpStatus.OK.value();
    }

    @GetMapping("/emailGroup")
    public int sendMailGroup() {

        String subject = "ЖК Город. Сбор голосов по ООС";
        String text = "Здравствуйте! \n\n Вы получили это письмо, т.к. зарегистрировались в шахматке. \n\n " +
                "Как Вы знаете, до 20.02 проходит собрание собственников. Начиная с 21.02 будет работать счётная\n" +
                " комиссия, для подведения итогов голосования. В её состав входят представители ИГ и просто собственники.\n" +
                "Для контроля правильности подсчёта голосов, просим Вас заполнить анкету на сайте, по адресу ниже.\n\n" +
                " http://212.80.216.73/regQuestion \n\n" +
                "Анкету надо заполнить один раз на каждого собственника, указав весь список Вашей недвижимости. \n" +
                "Телефон нужен для того, чтобы связаться с Вами при выявлении несостыковок с бумажной анкетой.\n" +
                "После заполнения нужно будет подтвердить ваш опрос по ссылке присланной на указанный Вами емайл.\n" +
                "Если у Вас несколько собственников, то после заполнения анкеты первогособственника, \n" +
                "Вам будет предложена ссылка на упрощенное заполнение данных для других владельцев этих же объектов.\n\n" +
                "Обратите внимание, все данные вносятся по АПП - площадь, адрес (строительный).\n\n" +
                "Анкету можно заполнить только один раз на человека.\n\n" +
                "С Уважением, инициативная группа ЖК Город";
//        List<String> mails = Arrays.asList("admin@chertenok.ru");
        List<String> mails = Arrays.asList(
                "Legati.ru@yandex.ru",
                "rychkov@bk.ru",
                "elenna.vladimirovna@mail.ru",
                "lychik73@mail.ru",
                "Mazepa2@yandex.ru",
                "butova12@mail.ru",
                "ta.belotelova@gmail.com",
                "zinaida.alekseevna@bk.ru",
                "1382981@gmail.com",
                "23121978polina@gmail.com",
                "2350402@bk.ru",
                "28012009glasha@gmail.com",
                "4anatoliy@mail.ru",
                "5048131@mail.ru",
                "5329599@gmail.com",
                "6226159@mail.ru",
                "6418143@mail.ru",
                "727rus777@gmail.com",
                "89214032870@mail.ru",
                "89262723552@mail.ru",
                "9165896544@ mail.ru",
                "9265632104t@gmail.com",
                "959pop@gmail.com",
                "9776655@mail.ru",
                "9851944880@mail.ru",
                "9853442856@mail.ru",
                "a.artem@my.com",
                "a.bagdalov@gmail.com",
                "a.demkin@mail.ru",
                "A.mikhailyn@gmail.com",
                "A806ko@gmail.com",
                "abatenev@gmail.com",
                "Abramenko88@mail.ru",
                "Adat.s@уandex.ru",
                "admin@chertenok.ru",
                "aeis@mail.ru",
                "agapov-andrei@rambler.ru",
                "Ak@dir-l.ru",
                "Al_romanov_87@mail.ru",
                "Aleksandr_shamin@mail.ru",
                "aleksandra.abril@yandex.ru",
                "Alenkij050@mail.ru",
                "aleshin90@gmail.com",
                "alesolomatin@gmail.com",
                "alex.estet@aol.com",
                "alexander.e.bobkov@gmail.com",
                "alexander.pekin@mail.ru",
                "alexander-705@mail.ru",
                "Alexeifomin1969@rambler.ru",
                "alexey_simakov@mail.ru",
                "AlexKhodyrev@yandex.ru",
                "Alex-marshall@yandex.ru",
                "alexshadow007@gmail.com",
                "alina23683@yandex.ru",
                "alserday@gmail.com",
                "Altch@mail.ru",
                "Alyamochka01@gmail.com",
                "amenyashev@gmail.com",
                "ammednikov@rambler.ru",
                "an_15_1989@mail.ru",
                "anastasiaradio1@gmail.com",
                "andremark@bk.ru",
                "andrevdovin@mail.ru",
                "AndruKrug@inbox.ru",
                "AndyPetrovich@yandex.ru",
                "anmorozov@gmail.com",
                "Anna.glazyuk@gmail.com",
                "Annamanyukhina28@yandex.ru",
                "annastarostina01@gmail.com",
                "Anton.lomovskih@gmail.com",
                "Anton.Mozgovoy@chelpipe.ru",
                "antoniof1@mail.ru",
                "Anya240890@yandex.ru",
                "anyuta160877@rambler.ru",
                "Aquaroom@gmail.com",
                "arahna2005@yandex.ru",
                "arf@deskit.ru",
                "artemkin2@mail.ru",
                "artpit@mail.ru",
                "artpopov@mail.ru",
                "asdfsok@gmail.com",
                "aspirinn@mail.ru",
                "astimofeev@rambler.ru",
                "aumalgina@gmail.com",
                "avarnovskaya@yandex.ru",
                "AVP.polyak@gmail.com",
                "avs308@yandex.ru",
                "sashazolot@gmail.com",
                "avtorklif@gmail.com",
                "bannov-59@mail.ru",
                "bannovalv@mail.ru",
                "baranov.smolensk@gmail.com",
                "Basovv@gmail.com",
                "Bela-r@mail.ru",
                "belyaev.d.p@yandex.ru",
                "Bintbox@mail.ru",
                "Bms_smb@mail.ru",
                "bossv84@mail.ru",
                "Boxches@bk.ru",
                "boychencos@gmail.com",
                "boytsova_1989@mail.ru",
                "burdin.maksim@mail.ru",
                "butova12@mail.ru",
                "by.stas@gmail.com",
                "cavenaghi-goal@yandex.ru",
                "cekaterina@yandex.ru",
                "cepmikh@gmail.com",
                "Ch.n.s@mail.ru",
                "Chakkchak@mail.ru",
                "chempion@bk.ru",
                "cherashev@gmail.com",
                "Chernikova_me@mail.ru",
                "cherrypushka@yandex.ru",
                "Chesnok.anastasia@yandex.ru",
                "chikara360@gmail.com",
                "comaros-sp@mail.ru",
                "Conte76@mail.ru",
                "d.i.leonov@yandex.ru",
                "d107k3@yandex.ru",
                "Dan2002admin1@mail.ru",
                "Davis1701@mail.ru",
                "deniskomkov2007@yandex.ru",
                "Densim@inbox.ru",
                "denzo1982@mail.ru",
                "dezire.s9000@gmail.com",
                "dimasite@narod.ru",
                "dimitrieva88@gmail.com",
                "dimon.husainov@mail.ru",
                "dklimova2011@gmail.com",
                "dlarin79@gmail.com",
                "dlexandr@yahoo.com",
                "dmitry.paliy@mail.ru",
                "dmitry.zimin@bk.ru",
                "dmitry@korolev.dk",
                "dmitrysid@icloud.com",
                "dmtr_ann@mail.ru",
                "Dolinnaya2017@mail.ru",
                "donnik2@mail.ru",
                "dr.bubyreva@gmail.com",
                "Ds.voronkova@gmail.com",
                "dubnikova14@mail.ru",
                "dunk23@mail.ru",
                "e.i.a@bk.ru",
                "e.zharova@gmail.com",
                "eezakharova@gmail.com",
                "egulyaeva@mail.ru",
                "ei.a@bk.ru",
                "Elek21@mail.ru",
                "elemur04@mail.ru",
                "elena.smol.83@mail.ru",
                "elena_kis@mail.ru",
                "elena_kis81@mail.ru",
                "elenakirilenko@rambler.ru",
                "elenhim@yandex.ru",
                "elenvna@gmail.com",
                "elkakostynina@gmail.com",
                "elquattro51@gmail.com",
                "ena20ivan@gmail.com",
                "Enin_Konstantin@inbox.ru",
                "Entia11@gmail.com",
                "erohina.m@gmail.com",
                "ershov_oleg@mail.ru",
                "esakova.u@gmail.com",
                "e-se@mail.ru",
                "Eula0293@gmail.com",
                "evgen89bachinin@gmail.com",
                "evgeny.sivoushkov@ru.pwc.com",
                "evs-ol@mail.ru",
                "eyakimnuk@gmail.com",
                "ez89161003806@ya.ru",
                "farmel@mail.ru",
                "fedservice@yandex.ru",
                "fedyunina@gmail.com",
                "felix1988@mail.ru",
                "fioredifelce@gmail.com",
                "fire95@mail.ru",
                "fols@list.ru",
                "fomalermont85@gmail.com",
                "fomi@bk.ru",
                "foomin@rambler.ru",
                "Fornamst@gmail.com",
                "fryazanov@gmail.com",
                "galivas@yandex.ru",
                "galiya84@mail.ru",
                "ganzh84@mail.ru",
                "gaynutdinovramiz@gmail.com",
                "Gbokshtein@gmail.com",
                "gibedinger@mail.ru",
                "glav_buh@sanep.vrn.ru",
                "Gogopupken@yandex.ru",
                "gorobecev@mail.ru",
                "Goryavinalexey@rambler.ru",
                "graps@mail.ru",
                "Grishin_s@rambler.ru",
                "gruz87@mail.ru",
                "Gwertu@mail.ru",
                "hasekkk@mail.ru",
                "homutovsky@arh-group.ru",
                "I.r.Goryacheva@yandex.ru",
                "Igor@interdialectplus.com",
                "Igorkimv@yandex.ru",
                "igortitov@inbox.ru",
                "iimarin@mail.ru",
                "Ildar.ru@gmail.com",
                "ilyascher@gmail.com",
                "ilyasorokin93@gmail.com",
                "imort58@gmail.com",
                "info@funtk.ru",
                "Innulia21@mail.ru",
                "irina2007m66@mail.ru",
                "irinagl@mail.ru",
                "itonkih@yandex.ru",
                "i-van_ip@mail.ru",
                "ivanpetukhovs@yandex.ru",
                "ivanpetukhovs@yandex.ru",
                "JACKOV@LIST.ru",
                "jc-art@list.ru",
                "jekaap@ya.ru",
                "johny_p84@mail.ru",
                "Jsdavydova@gmail.com",
                "Julia.boyadzhi@gmail.com",
                "Kadriye@inbox.ru",
                "kaluzhnaya.ekaterina@gmail.com",
                "Kamikadzerush@gmail.com",
                "karlovvit@rambler.ru",
                "karmello.art@bk.ru",
                "karolina-irina@mail.ru",
                "kasapchuk@mail.ru",
                "Kash86@mail.ru",
                "kate_vbm@mail.ru",
                "katerina_sh06@mail.ru",
                "kattim67@gmail.com",
                "Katysha637@yandex.ru",
                "Kavln@mail.ru",
                "kds.660@gmail.com",
                "kheycheeva@mail.ru",
                "king555-90@mail.ru",
                "kiov@mail.ru",
                "kirilldiz@gmail.com",
                "kkrus@rambler.ru",
                "Klevelena@yandex.ru",
                "kn.myasoedova@mail.ru",
                "Kolasina@gmail.com",
                "koler2@rambler.ru",
                "kolosov_ion@mail.ru",
                "Komvalery@mail.ru",
                "Koroleva_23@bk.ru",
                "Kosmos-Anna@yandex.ru",
                "kovsvet@list.ru",
                "krat@bk.ru",
                "kravtsov.au@gmail.com",
                "kraynov2014@icloud.com",
                "kris2005a@yandex.ru",
                "ksenia_bubnova@mail.ru",
                "k-tvs@mail.ru",
                "Kudasovivan@bk.ru",
                "kudinovmikhail@yandex.ru",
                "Kuskoff@list.ru",
                "Kuv.87@bk.ru",
                "L.chelushkina@gmail.com",
                "Lanap@list.ru",
                "lanschikovdn@gmail.com",
                "Lapsh.v@gmail.com",
                "larisamoskva@rambler.ru",
                "Laycom@yandex.ru",
                "lena18071980@mail.ru",
                "leo88d@bk.ru",
                "lhannanshi@gmail.com",
                "Libman1990@gmail.com",
                "lilxen@mail.ru",
                "linlh@yandex.ru",
                "litvinova@jsbshop.ru",
                "lobov.vitaliy@gmail.com",
                "loldrood@hotmail.com",
                "lorik3460@mail.ru",
                "Lorry05@yandex.ru",
                "lovenet@yandex.ru",
                "lysenko_kv@mail.ru",
                "m.lobyzenko@gmail.com",
                "maa28@mail.ru",
                "Maksimkraynev@gmail.com",
                "malahova2002@mail.ru",
                "Mamy2008@yandex.ru",
                "marchuk.id@gmail.com",
                "margaritka0717@yandex.ru",
                "mariya.medvedeva@phystech.edu",
                "marozov69@mail.ru",
                "Mashar@bk.ru",
                "mat8413@mail.ru",
                "Matveev.tdkas@gmail.com",
                "maxa.rekodis@mail.ru",
                "maxim.ptpv@gmail.com",
                "medvedeva1702@mail.ru",
                "melkadze_97@mail.ru",
                "melnikov-aw@mail.ru",
                "Melyakin86@mail.ru",
                "meshkova.t@yandex.ru",
                "Metrik_89@mail.ru",
                "michaelchuiko@mail.ru",
                "Mike_d-hot@mail.ru",
                "mikhail.maksimov@gmail.com",
                "mikhaylov-89@ya.ru",
                "milja1998@yandex.ru",
                "miltonnev@yandex.ru",
                "mishabroch@gmail.com",
                "Mishkin.a.v@gmail.com",
                "mmitrofanov@gmail.com",
                "Mnv12041974@rambler.ru",
                "molot85@yandex.ru",
                "moshkova.tatyana@gmail.com",
                "motveichik@yandex.ru",
                "mr.agafonov@mail.ru",
                "mr.veter997@mail.ru",
                "Mrscutie@mail.ru",
                "muromcevn@gmail.com",
                "mv.gorod495@gmail.com",
                "Nadejda.shatalina@yandex.ru",
                "nafanya1975@mail.ru",
                "Nafikova.sr@yandex.ru",
                "Nastasia.92@mail.ru",
                "nastya-klevakina@yandex.ru",
                "nata79@gmail.com",
                "natakalina@yahoo.com",
                "Natali-510@mail.ru",
                "Natasha996@rambler.ru",
                "natulyna@yandex.ru",
                "Neizvestno@yandex.ru",
                "nekto85@mail.ru",
                "Nelyawbd@mail.ru",
                "Nesh_2312@mail.ru",
                "Neukuhren83@mail.ru",
                "Never07@mail.ru",
                "Nm.14@mail.ru",
                "nmara@yandex.ru",
                "npronichkin@gmail.com",
                "nyke@mail.ru",
                "nzanevskaya@mail.ru",
                "O2bar@mail.ru",
                "ocheretniy@yahoo.com",
                "oe2002@mail.ru",
                "oe9779426393@yandex.ru",
                "ol.sadokha@gmail.com",
                "Oleg.yashin@icloud.com",
                "ole-titar@yandex.ru",
                "olga.and.17@mail.ru",
                "Olga@shelopugina.ru",
                "Olga1164@yandex.ru",
                "olgaalexseeva@mail.ru",
                "Olgaklemez@gmail.com",
                "Olgaslm@mail.ru",
                "olgpahomova76@mail.ru",
                "oly-332@yandex.ru",
                "olya_chu@mail.ru",
                "olykrasavischa@mail.ru",
                "olysh1981@gmail.com",
                "ondrew@yandex.ru",
                "onikzubkova@gmail.com",
                "oo.7575@mail.ru",
                "Openlighthouse@gmail.com",
                "orlik2011b@yandex.ru",
                "oselkova@mail.ru",
                "Ospitsyna@inbox.ru",
                "otabek5@ya.ru",
                "ovchinnikovaleks@gmail.com",
                "Owri2012@mail.ru",
                "oxana.lukashevich@mail.ru",
                "p_ja@bk.ru",
                "p89162569869@gmail.com",
                "Panka1990@mail.ru",
                "pankovaolya14@gmail.com",
                "Patisov@gmail.com",
                "Pavel-simakov@mail.ru",
                "perevedentsevpv@gmail.com",
                "pev-0805@mail.ru",
                "Pismennaya@yandex.ru",
                "pismo.for.me@yandex.ru",
                "podgdan@mail.ru",
                "polinam67@mail.ru",
                "Polo777@bk.ru",
                "ponival@gmail.com",
                "Pronin.a@list.ru",
                "Proz78@mail.ru",
                "puma.80@mail.ru",
                "Pupkinmen@yandex.ru",
                "Queenofdiscourse@gmail.com",
                "R.a.khamzin@gmail.com",
                "r_k_mus@mail.ru",
                "razumtseva_lena@mail.ru",
                "reshsv@gmail.com",
                "rgadeev@mail.ru",
                "rinasweet@mail.ru",
                "rn.yarulin@gmail.com",
                "rockbomber@gmail.com",
                "rokannon@gmail.com",
                "Rosfeer@mail.ru",
                "rotovmaxim@mail.ru",
                "Roza-vetrov89@yandex.ru",
                "ruspsydoc@hotmail.com",
                "russaransk@gmail.com",
                "Rvnik@list.ru",
                "s.ilushkina@ya.ru",
                "sadovnikov.dmitry@gmail.com",
                "saena.av@gmail.com",
                "safronov33@yandex.ru",
                "salexey7@gmail.com",
                "Samin@inbox.ru",
                "sanchocrab@mail.ru",
                "saritka@list.ru",
                "Sarshor@gmail.com",
                "sasha-kornilov92@yandex.ru",
                "sav1007sav@ya.ru",
                "savva1020@ya.ru",
                "schekin77@mail.ru",
                "Semka82@list.ru",
                "semyon2707@mail.ru",
                "senen2003@mail.ru",
                "sepeseva@mail.ru",
                "ser20091984@yandex.ru",
                "serg.ananev@gmail.com",
                "sergeenko20094@yandex.ru",
                "sergey.abkaryan@gmail.com",
                "Sergey.korchagin@roust.com",
                "shatalov.peter@gmail.com",
                "Shedov@gmail.com",
                "shephert@mail.ru",
                "Sh-eugene@mail.ru",
                "Shev_i_v@mail.ru",
                "Shimanjulia@rambler.ru",
                "Shine123@mail.ru",
                "Shmel526@yandex.ru",
                "sineok@yandex.ru",
                "Skapik@bk.ru",
                "slusareva-olga-01@yandex.ru",
                "Soglasnov@mail.ri",
                "sokira11@mail.ru",
                "Solovvey@yahoo.com",
                "Solovvey89@gmail.com",
                "Sorokin0903551@yandex.ru",
                "sorokinamarina@inbox.ru",
                "Sorokovov@yandex.ru",
                "Sparrk@yandex.ru",
                "spektr-st@list.ru",
                "ssvetik22@mail.ru",
                "stas_r79@mail.ru",
                "stas069@list.ru",
                "Stepnyakova_inna@mail.ru",
                "stpalex74@gmail.ru",
                "stvvs@yandex.ru",
                "Susanya@yandex.ru",
                "Sveta_1991-91@mail.ru",
                "svetamorozova@inbox.ru",
                "svetlana-dyakova.1520@mail.ru",
                "svetlanalv@yandex.ru",
                "sychev.d.s@yandex.ru",
                "ta.belotelova@gmail.com",
                "Taranovslav@yandex.ru",
                "Tasp@yandex.ru",
                "tataa21@yandex.ru",
                "tatpolov@rambler.ru",
                "Tatysya2@rambler.ru",
                "t-a-u-rus@mail.ru",
                "terni@inbox.ru",
                "Testardo@yandex.ru",
                "tiger.sa@gmail.com",
                "timercan@gmail.com",
                "tinamulol@mail.ru",
                "tolyupa2011@rambler.ru",
                "toma.spb61@yandex.ru",
                "Trofimovaelenaa@yandex.ru",
                "Tropez@live.ru",
                "u_e_l@mail.ru",
                "U_love_me@mail.ru",
                "V.o.y59@mail.ru",
                "va.yakutin@gmail.com",
                "vadim_m205@mail.ru",
                "vadvim@inbox.ru",
                "valenti-eliseeva@mail.ru",
                "valentin0@mail.ru",
                "Valeraprada@mail.ru",
                "vanmenshenin@gmail.com",
                "Vasilich16@gmail.com",
                "vavilinaa82@mail.ru",
                "vayer7@gmail.com",
                "Velbrother@yandex.ru",
                "velieva-katya@yandex.ru",
                "velikanov.atwork@gmail.com",
                "vendetta-6@mail.ru",
                "Vervitskiyp@yandex.ru",
                "vev2991@mail.ru",
                "victoria.umk@yandex.ru",
                "viktorrmbox@gmail.com",
                "vl@pwip.ru",
                "vladimir.kolyada@thisislogic.ru",
                "vladimir19-74@mail.ru",
                "vladislav.kassym@gmail.com",
                "vladislav.markin@gmail.com",
                "vlasen.irena@gmail.com",
                "vlasova-uliya@mail.ru",
                "vmorozzka@gmail.com",
                "Vmstankevich@inbox.ru",
                "Volovik@iep.ru",
                "Vorobtseva@yandex.ru",
                "vroshal@mail.ru",
                "Vv.petrov86@gmail.com",
                "Vychikov@mail.ru",
                "Woitov@list.ru",
                "yapepyaka@yandex.ru",
                "Yastrebovmn@gmail.com",
                "Your.head.is.my.head@gmail.com",
                "zapertov.mihail@yandex.ru",
                "Zevasmail1@mail.ru",
                "zhukov_b@mail.ru",
                "Zhukovasf@mail.ru",
                "zlatopolskiy@yandex.ru",
                "zolotavina@bk.ru",
                "Zyryakova@mail.ru",
                "ЕAPonomareva@outlook.com"











































































































        );


        mailService.sendMail(mails, subject, text, false);


        return HttpStatus.OK.value();
    }


}
