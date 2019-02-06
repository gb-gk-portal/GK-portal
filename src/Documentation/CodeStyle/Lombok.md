# Convention
https://projectlombok.org/features/all

Случаи использования следующих анотаций:
## @Data
Лучше использовать там, где действительно необходимо
@Data сочетает в себе аннотации:

- @ToString
- @EqualsAndHashCode
- @Getter on all fields
- @Setter on all non-final fields 
- @RequiredArgsConstructor
Стоит помнить, что если entity будет использоваться в коллекция, 
то Equals/Hashcode будет сгенерирован фреймворком

Стоит также помнить, что если из Entity в Entity есть зависимости
Many-To-Many, то при выводе toString может быть SOFException (даже с Lazy инициализацией).

# @Getter/@Setter
Предпочтительно перед @Data

