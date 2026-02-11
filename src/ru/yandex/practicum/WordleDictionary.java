package ru.yandex.practicum;

import java.util.List;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public boolean isContains(String word) {
        return words.contains(word);
    }

    public List<String> getListWords() {
        return words;
    }

    //метод для получения зашифрованной символьной подсказки
    public String getHint(String guessedWord,String answerWord) {
        StringBuilder hintedWord = new StringBuilder("*****");
        //usedInAnswer для отслеживания уже использованных позиций в answerWord, что предотвращает повторные совпадения
        //одних и тех же букв.
        boolean[] usedInAnswer = new boolean[5];

        //проверка букв на правильные позиции и отсутствующих букв
        for (int letterIndex = 0; letterIndex < answerWord.length(); letterIndex++) {
            if (guessedWord.charAt(letterIndex) == answerWord.charAt(letterIndex)) {
                hintedWord.setCharAt(letterIndex, '+');
                usedInAnswer[letterIndex] = true;
            } else {
                hintedWord.setCharAt(letterIndex,'-');
                usedInAnswer[letterIndex] = false;
            }
        }

        //проверка наличия буквы в слове, но не на своих местах
        for (int letterIndex = 0; letterIndex < answerWord.length(); letterIndex++) {
            if (hintedWord.charAt(letterIndex) != '+') {
                for (int answerLetterIndex = 0; answerLetterIndex < answerWord.length(); answerLetterIndex++) {
                    if (!usedInAnswer[answerLetterIndex] && guessedWord.charAt(letterIndex)
                            == answerWord.charAt(answerLetterIndex)) {
                        hintedWord.setCharAt(letterIndex, '^');
                        usedInAnswer[answerLetterIndex] = true;
                        break;
                    }
                }
            }
        }

        return hintedWord.toString();
    }

}
