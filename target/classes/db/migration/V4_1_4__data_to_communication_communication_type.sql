INSERT INTO `support_boot_db`.`communication_type` (`id`, `description`)
VALUES ('dd2ff57f-901e-4dee-b914-56659187fe85', 'Email'),
       ('b5bc1897-c6f4-4efd-9dbd-cf89002997e4', 'Телефон');

INSERT INTO `support_boot_db`.`communication` (`id`,
                                               `communication_type_id`,
                                               `contact_id`,
                                               `identify`,
                                               `description`,
                                               `confirmed`,
                                               `confirm_code_date`,
                                               `confirm_code`)
VALUES ('71d3f6d8-7be1-4a7e-a35f-0ae054d505bb',
        'dd2ff57f-901e-4dee-b914-56659187fe85',
        '27fa708c-394d-43b7-8358-b58050ecfdb8',
        'uatilman@gmail.com',
        'основной телефон',
        b'1',
        '2019-01-31',
        'kvjakjfdb');