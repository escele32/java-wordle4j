package ru.yandex.practicum;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleGame {

    private String answer;
    private int steps;
    private WordleDictionary dictionary;
    private List<String> listPossibleWords;
    private List<String> listHintsHistory;
    private List<String> listWordsHistory;

    private String pickSolution() {
        Random random = new Random();
        return dictionary.getListWords().get(random.nextInt(dictionary.getListWords().size()));
    }

    private boolean matchesHint(String guessedWord, String word, String hintedWord) {
        boolean[] usedInWord = new boolean[5];

        for (int i = 0; i < 5; i++) {
            if (hintedWord.charAt(i) == '+') {
                if (word.charAt(i) != guessedWord.charAt(i)) {
                    return false;
                }
            } else if (hintedWord.charAt(i) == '^') {
                boolean found = false;
                for (int j = 0; j < 5; j++) {
                    if (!usedInWord[j] && word.charAt(j) == guessedWord.charAt(i) && j != i) {
                        found = true;
                        usedInWord[j] = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            } else if (hintedWord.charAt(i) == '-') {
                for (int j = 0; j < 5; j++) {
                    if (word.charAt(j) == guessedWord.charAt(i)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void filterListPossibleWords(String guessedWord, String hintedWord) {
        List<String> listFiltered = new ArrayList<>();
        for (String word : listPossibleWords) {
            if (matchesHint(guessedWord, word, hintedWord)) {
                listFiltered.add(word);
            }
        }
        listPossibleWords = listFiltered;
    }

    //вспомогательный метод к методу "getSuggestedWord" для проверки букв
    private boolean isSuitable(String word, Map<Character, String> letterStatuses, List<String> listWordsHistory,
                               List<String> listHintsHistory) {
        //проверка исключённых букв
        for (char symbol : letterStatuses.keySet()) {
            String status = letterStatuses.get(symbol);
            if (status.equals("-") && word.indexOf(symbol) != -1) {
                return false;
            }
        }

        //проверка букв на правильных позициях
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < listWordsHistory.size(); j++) {
                String wordHistory = listWordsHistory.get(j);
                String hintHistiry = listHintsHistory.get(j);
                char guessedChar = wordHistory.charAt(i);
                char hintChar = hintHistiry.charAt(i);
                if (hintChar == '+' && word.charAt(i) != guessedChar) {
                    return false;
                } else if (hintChar == '^' && (word.charAt(i) == guessedChar
                        || !word.contains(Character.toString(guessedChar)))) {
                    return false;
                }
            }
        }

        //проверка, что все "+" и "^" буквы есть в слове
        for (Character character : letterStatuses.keySet()) {
            String status = letterStatuses.get(character);
            if ((status.equals("+") || status.equals("^")) && !word.contains(Character.toString(character))) {
                return false;
            }
        }

        return true;
    }

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
        this.listHintsHistory = new ArrayList<>();
        this.listPossibleWords = new ArrayList<>(dictionary.getListWords());
        this.listWordsHistory = new ArrayList<>();
        this.steps = 6;
        this.answer = pickSolution();
    }

    public String getAnswer() {
        return answer;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isWin(String guessedWord) {
        return guessedWord.equals(getAnswer());
    }

    public boolean isGameOver() {
        return steps <= 0;
    }

    public List<String> getListHintsHistory() {
        return new ArrayList<>(listHintsHistory);
    }

    public List<String> getListWordsHistory() {
        return new ArrayList<>(listWordsHistory);
    }

    public List<String> getListPossibleWords() {
        return new ArrayList<>(dictionary.getListWords());
    }

    public String makeGuess(String guessedWord) throws WordNotFoundInDictionaryException, InvalidWordFormatException {
        guessedWord = guessedWord.toLowerCase();

        if (guessedWord.length() != 5) {
            throw new InvalidWordFormatException("Слово должно состоять из 5 букв");
        }
        if (!dictionary.getListWords().contains(guessedWord)) {
            throw new WordNotFoundInDictionaryException("Слово не найдено в словаре");
        }

        String hintedWord = dictionary.getHint(guessedWord, getAnswer());
        listWordsHistory.add(guessedWord);
        listHintsHistory.add(hintedWord);
        steps--;

        filterListPossibleWords(guessedWord, hintedWord);

        return hintedWord;
    }

    //метод для получения подходящего слова для подсказки на основе текущей истории
    public String getSuggestedWord() {
        List<String> candidates = new ArrayList<>(dictionary.getListWords());
        Map<Character, String> letterStatuses = new LinkedHashMap<>();

        //анализ истории
        for (int i = 0; i < listWordsHistory.size(); i++) {
            String word = listWordsHistory.get(i);
            String hint = listHintsHistory.get(i);
            for (int j = 0; j < 5; j++) {
                char charWord = word.charAt(j);
                char charHint = hint.charAt(j);
                if (charHint == '+') {
                    letterStatuses.put(charWord, "+");
                } else if (charHint == '^') {
                    if (!letterStatuses.containsKey(charWord) || !letterStatuses.get(charWord).equals("+")) {
                        letterStatuses.put(charWord, "^");
                    }
                } else if (charHint == '-') {
                    letterStatuses.put(charWord, "-");
                }
            }
        }

        //фильтрация кандидатов
        List<String> filteredCandidates = new ArrayList<>();
        for (String word : candidates) {
            if (isSuitable(word, letterStatuses, listWordsHistory, listHintsHistory)) {
                filteredCandidates.add(word);
            }
        }

        if (filteredCandidates.isEmpty()) {
            return null;
        } else {
            Random random = new Random();
            return filteredCandidates.get(random.nextInt(filteredCandidates.size()));
        }
    }

}
