package com.karakullukcu.huseyin.anagrammaker.anagram;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Anagram algorithm for finding single and multi-word anagrams. Other algorithms
 * on internet to much complex and using so much memory.
 * So I tried to make clean, simple, memory efficent and fast(depends on dictionary)
 * multi-word algorithm designed for mobile apps.
 */

public class AnagramAlgorithm {
    private ArrayList<String> sortedDictionary;
    private List<String> anagrams;
    private int minWordSize;
    private Context mContext;

    private List<String> anagramTracker;

    public AnagramAlgorithm(Context context) {
        mContext = context;
        anagrams = new ArrayList<>();
        anagramTracker = new ArrayList<>();
    }

    public List<String> getAnagrams(String givenText) {
        minWordSize = givenText.length() / 2;
        if (minWordSize > 6)
            minWordSize = 6;
        ReadUtils readUtils = new ReadUtils(mContext,givenText,minWordSize);
        sortedDictionary = readUtils.getSortedDictionary();

        char[] givenChar = givenText.trim().toCharArray();
        for(int i = 0; i<sortedDictionary.size(); i++) {
            anagramTracker.clear();
            mainAlgorithm(i,givenChar);
        }
        return anagrams;
    }


    private void mainAlgorithm(int index, char[] givenTextAsChar) {

        String searchWord = sortedDictionary.get(index);
        char[] searchWordChars = searchWord.toCharArray();

        if (AnagramHelper.isSubset(searchWordChars,givenTextAsChar)) {
            char[] newChar = AnagramHelper.getDifference(givenTextAsChar,searchWordChars);

            if (newChar.length >= minWordSize ) {
                anagramTracker.add(searchWord);
                for (int i = 0; i < sortedDictionary.size(); i++) {
                    mainAlgorithm(i, newChar);
                }
                anagramTracker.remove(anagramTracker.size()-1);
            } else if (newChar.length == 0) {
                anagramTracker.add(searchWord);
                if (anagramTracker.size() == 1)
                    anagrams.add(0,TextUtils.join(" ", anagramTracker));
                else
                    anagrams.add(TextUtils.join(" ", anagramTracker));
                anagramTracker.remove(anagramTracker.size()-1);
            }
        }


    }

}
