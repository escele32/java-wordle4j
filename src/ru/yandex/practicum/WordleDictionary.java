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
        for (int i = 0; i < answerWord.length(); i++) {
            if (guessedWord.charAt(i) == answerWord.charAt(i)) {
                hintedWord.setCharAt(i, '+');
                usedInAnswer[i] = true;
            } else {
                hintedWord.setCharAt(i,'-');
                usedInAnswer[i] = false;
            }
        }

        //проверка наличия буквы в слове, но не на своих местах
        for (int i = 0; i < answerWord.length(); i++) {
            if (hintedWord.charAt(i) != '+') {
                for (int j = 0; j < answerWord.length(); j++) {
                    if (!usedInAnswer[j] && guessedWord.charAt(i) == answerWord.charAt(j)) {
                        hintedWord.setCharAt(i, '^');
                        usedInAnswer[j] = true;
                        break;
                    }
                }
            }
        }

        return hintedWord.toString();
    }

}
