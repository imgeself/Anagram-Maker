package com.karakullukcu.huseyin.anagrammaker.anagram;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Reads from dictionary text file in assets folder. Loads words in to the memory. 
 */

public class ReadUtils {
    private ArrayList<String> sortedDictionary = new ArrayList<>();
    private static final String EN_DICT_FILENAME = "words_EN.txt";

    public ReadUtils(Context context, String givenText, int minWordSize) {
        readDictionary(context, givenText, minWordSize);
    }

    private void readDictionary(Context context, String givenText, int minWordSize) {

        if (givenText == null || givenText.isEmpty() || context == null) {
            return;
        }

        char[] givenTextAsCharArray = givenText.toCharArray();

        AssetManager assetManager;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            assetManager = context.getAssets();
            inputStream = assetManager.open(EN_DICT_FILENAME);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String word;

            while ((word = bufferedReader.readLine()) != null) {
                word = word.toLowerCase();
                char[] wordChars = word.toCharArray();
                if ((wordChars.length >= minWordSize) && AnagramHelper.isSubset(wordChars, givenTextAsCharArray)
                        && !sortedDictionary.contains(word) && !word.equals(givenText)) {
                    sortedDictionary.add(word);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
                if (inputStreamReader != null)
                    inputStreamReader.close();
                if (inputStream != null)
                    inputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public ArrayList<String> getSortedDictionary() {
        return sortedDictionary;
    }
}
