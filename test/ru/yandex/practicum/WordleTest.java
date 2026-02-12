package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

class WordleTest {

    private static WordleGame game;
    private static WordleDictionary dictionary;
    private static final String testFileName = "words_file_test.txt";

    @BeforeAll
    public static void beforeAll() throws IOException {

        PrintWriter writerTest = new PrintWriter(new FileWriter(testFileName, StandardCharsets.UTF_8));
        writerTest.write("Фабрика\n");
        writerTest.write("ёёёёё\n");
        writerTest.write("олОво\n");
        writerTest.write("ГоРоД\n");
        writerTest.write("лес\n");
        writerTest.write("пРосо\n");
        writerTest.write("СлоВо");
        writerTest.close();
        dictionary = WordleDictionaryLoader.readWordsFromFile(testFileName);
        game = new WordleGame(dictionary);

    }

    @Test
    public void testReadWordsFromFile() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(testFileName, StandardCharsets.UTF_8));
        System.out.println("До преобразования:");
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.printf(" %s ", line);
        }

        System.out.println();
        System.out.println("После преобразования: ");
        for (String word : dictionary.getListWords()) {
            System.out.printf(" %s ", word);
        }

        String fileNull = "words_file_null.txt";
        System.out.println();
        System.out.print("Исключение: ");
        WordleDictionary dictionaryNull = WordleDictionaryLoader.readWordsFromFile(fileNull);
        System.out.println();

    }

    @Test
    public void testGetHint() {

        String answer = "слово";
        String guess = "просо";
        Assertions.assertEquals("--+^+", dictionary.getHint(guess, answer));
        System.out.printf("Загаданное слово: %s\n", answer);
        System.out.printf("Предполагаемое слово: %s\n", guess);
        System.out.println(dictionary.getHint(guess, answer));
        System.out.println();

    }

    @Test
    public void testMakeGuess() throws Exception {

        System.out.println();
        String word = "формула";
        System.out.println(word);
        try {
            game.makeGuess(word);
        } catch (Exception exception) {

            System.out.println(exception.getMessage());
        }

        String worNotDictionary = "марка";
        System.out.println(worNotDictionary);
        try {
            game.makeGuess(worNotDictionary);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        System.out.printf("Количество попыток: %d\n",game.getSteps());
        String guess = "просо";
        game.makeGuess(guess);
        System.out.printf("Предполагаемое слово: %s\n", guess);
        System.out.printf("Осталось попыток: %d\n",game.getSteps());
        System.out.printf("Загаданное слово: %s\n", game.getAnswer());
        System.out.println(game.makeGuess(guess));
        System.out.println();

    }

    @Test
    public void testGetSuggestedWord() {

        String suggestion = game.getSuggestedWord();
        System.out.println();
        System.out.println(suggestion);
        System.out.println(game.getListPossibleWords());
        Assertions.assertNotNull(suggestion);
        Assertions.assertTrue(game.getListPossibleWords().contains(suggestion));

    }

}
