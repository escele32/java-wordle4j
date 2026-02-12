package ru.yandex.practicum;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) throws IOException,
            WordNotFoundInDictionaryException, InvalidWordFormatException {
        String fileName = "words_ru.txt";
        String logFileName = "log_file.txt";

        try (PrintWriter log = new PrintWriter(new FileWriter(logFileName,
                StandardCharsets.UTF_8, true))) {

            WordleDictionary dictionary = WordleDictionaryLoader.readWordsFromFile(fileName);
            System.out.printf("Общее количество слов: %d\n", dictionary.getListWords().size());
            log.printf("Словарь успешно загружен, слов: %d\n", dictionary.getListWords().size());

            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
            WordleGame game = new WordleGame(dictionary);

            boolean isHasWin = false;

            while (game.getSteps() > 0 && !isHasWin) {
                System.out.printf("Попытка %d. Введите слово или нажмите Enter для подсказки: \n", 7 - game.getSteps());
                String input = scanner.nextLine().trim().toLowerCase();

                try {
                    if (input.isEmpty()) {
                        String suggestion = game.getSuggestedWord();
                        if (!suggestion.isEmpty()) {
                            System.out.printf("Подсказка: %s\n", suggestion);
                            log.printf("Подсказка: %s\n", suggestion);
                        } else {
                            System.out.println("Нет подходящих слов для подсказки");
                            log.println("Нет подходящих слов для подсказки");
                        }
                        continue;
                    }

                    String hint = game.makeGuess(input);
                    System.out.println(hint);
                    log.printf("Попытка: %s, подсказка: %s\n", input, hint);

                    if (game.isWin(input)) {
                        System.out.println("Поздравляем! Вы отгадали слово!");
                        log.println("Игрок выиграл");
                        isHasWin = true;
                    }
                } catch (WordNotFoundInDictionaryException | InvalidWordFormatException exception) {
                    System.out.println(exception.getMessage());
                    log.printf("Ошибка: %s\n", exception.getMessage());
                }
            }

            if (!isHasWin) {
                System.out.printf("К сожалению, попытки закончились. Загаданное слово было: %s\n", game.getAnswer());
                log.printf("Игра завершена. Загаданное слово: %s\n", game.getAnswer());
            }
            scanner.close();
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

    }

}
