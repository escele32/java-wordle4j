package ru.yandex.practicum;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    //метод для выборки соответствующих слов из файла
    public static WordleDictionary readWordsFromFile(String filename) throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            String list;
            while ((list = bufferedReader.readLine()) != null) {
                if (list.length() == 5) {
                    words.add(list.toLowerCase().replace("ё", "е"));
                }
            }
        } catch (IOException ioException) {
            System.out.printf(ioException.getMessage());
        }

        return new WordleDictionary(words);
    }

}
