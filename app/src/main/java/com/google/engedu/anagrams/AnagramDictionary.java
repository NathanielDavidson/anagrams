package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private int wordLength;
    private List<String> wordList = new ArrayList<>();
    private Set<String> wordSet = new HashSet<String>();
    private Map<String, List<String>> lettersToWord = new HashMap<String, List<String>>();
    private Map<Integer, List<String>> sizeToWords = new HashMap<Integer, List<String>>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            wordList.add(word);
            String sortedString = sortString(word);
            if(lettersToWord.containsKey(sortedString)){
                if(!lettersToWord.get(sortedString).contains(word)) {
                    lettersToWord.get(sortedString).add(word);
                }
            }else{
                List<String> tmp = new ArrayList<String>();
                tmp.add(word);
                lettersToWord.put(sortedString, tmp);
            }
            int length = word.length();
            if(sizeToWords.containsKey(length)){
                sizeToWords.get(length).add(word);
            }else{
                List<String> tmp = new ArrayList<String>();
                tmp.add(word);
                sizeToWords.put(length, tmp);
            }
        }
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        Log.d("anagrams","getAnagrams");
        ArrayList<String> result = new ArrayList<String>();
        targetWord = sortString(targetWord);
        for(String word : wordSet){
            if(word.length()==targetWord.length()) {
                String sortedWord = sortString(word);
                if (sortedWord.equals(targetWord)) {
                    result.add(word);
                }
            }
        }
        return result;
    }

    public boolean isGoodWord(String word, String base){
        return !word.contains(base);
    }

    private String sortString(String word){
        if(word==null){
            return word;
        }
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char extra = 'a';
        for(int i=0;i<26;i++){
            String wordExtra = sortString(word + (char)(extra+i));
            if(lettersToWord.containsKey(wordExtra)){
                for(String foundWord :lettersToWord.get(wordExtra)){
                    if(isGoodWord(foundWord, word)){
                        result.add(foundWord);
                    }
                }

            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int starting = (int) Math.floor(Math.random()*sizeToWords.size());
        boolean loop = false;
        int pos = starting;
        wordLength = DEFAULT_WORD_LENGTH;
        Log.d("picker","starting "+starting);
        while(!(starting==pos && loop)){
            if(pos>=wordList.size()){
                loop=true;
                pos=0;
            }
            if(wordList.get(pos).length()<=MAX_WORD_LENGTH) {
                if (getAnagrams(wordList.get(pos)).size() >= MIN_NUM_ANAGRAMS) {
                    Log.d("picker","test = "+wordList.get(pos));
                    return wordList.get(pos);
                }
            }
            pos++;
        }
        return null;
    }
}
