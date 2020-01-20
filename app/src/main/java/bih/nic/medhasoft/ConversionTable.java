package bih.nic.medhasoft;


import android.util.Log;

import java.util.ArrayList;
import java.util.Hashtable;


public class ConversionTable {

    private String TAG = "Conversation Table";

    private Hashtable<String,String> unicode;

    private void populateHashTable()
    {
        unicode = new Hashtable<>();

        // unicode
        unicode.put("\u0901","rha"); // anunAsika - cchandra bindu, using ~ to // *
        unicode.put("\u0902","n"); // anusvara
        unicode.put("\u0903","ah"); // visarga

        unicode.put("\u0940","ee");
        unicode.put("\u0941","u");
        unicode.put("\u0942","oo");
        unicode.put("\u0943","rhi");
        unicode.put("\u0944","rhee");   //  * = Doubtful Case
        unicode.put("\u0945","e");
        unicode.put("\u0946","e");
        unicode.put("\u0947","e");
        unicode.put("\u0948","ai");
        unicode.put("\u0949","o");
        unicode.put("\u094a","o");
        unicode.put("\u094b","o");
        unicode.put("\u094c","au");

        unicode.put("\u094d","");
        unicode.put("\u0950","om");

        unicode.put("\u0958","k");
        unicode.put("\u0959","kh");
        unicode.put("\u095a","gh");
        unicode.put("\u095b","z");
        unicode.put("\u095c","dh");    // *
        unicode.put("\u095d","rh");
        unicode.put("\u095e","f");

        unicode.put("\u095f","y");
        unicode.put("\u0960","ri");
        unicode.put("\u0961","lri");
        unicode.put("\u0962","lr");       //  *
        unicode.put("\u0963","lree");     //  *

        unicode.put("\u093E","aa");
        unicode.put("\u093F","i");

        //  Vowels and Consonants...
        unicode.put("\u0905","a");
        unicode.put("\u0906","a");
        unicode.put("\u0907","i");
        unicode.put("\u0908","ee");
        unicode.put("\u0909","u");
        unicode.put("\u090a","oo");
        unicode.put("\u090b","ri");
        unicode.put("\u090c","lri"); // *
        unicode.put("\u090d","e"); // *
        unicode.put("\u090e","e"); // *
        unicode.put("\u090f","e");
        unicode.put("\u0910","ai");
        unicode.put("\u0911","o");
        unicode.put("\u0912","o");
        unicode.put("\u0913","o");
        unicode.put("\u0914","au");

        unicode.put("\u0915","k");
        unicode.put("\u0916","kh");
        unicode.put("\u0917","g");
        unicode.put("\u0918","gh");
        unicode.put("\u0919","ng");
        unicode.put("\u091a","ch");
        unicode.put("\u091b","chh");
        unicode.put("\u091c","j");
        unicode.put("\u091d","jh");
        unicode.put("\u091e","ny");
        unicode.put("\u091f","t"); // Ta as in Tom
        unicode.put("\u0920","th");
        unicode.put("\u0921","d"); // Da as in David
        unicode.put("\u0922","dh");
        unicode.put("\u0923","n");
        unicode.put("\u0924","t"); // ta as in tamasha
        unicode.put("\u0925","th"); // tha as in thanks
        unicode.put("\u0926","d"); // da as in darvaaza
        unicode.put("\u0927","dh"); // dha as in dhanusha
        unicode.put("\u0928","n");
        unicode.put("\u0929","nn");
        unicode.put("\u092a","p");
        unicode.put("\u092b","ph");
        unicode.put("\u092c","b");
        unicode.put("\u092d","bh");
        unicode.put("\u092e","m");
        unicode.put("\u092f","y");
        unicode.put("\u0930","r");
        unicode.put("\u0931","rr");
        unicode.put("\u0932","l");
        unicode.put("\u0933","ll"); // the Marathi and Vedic 'L'
        unicode.put("\u0934","lll"); // the Marathi and Vedic 'L'
        unicode.put("\u0935","v");
        unicode.put("\u0936","sh");
        unicode.put("\u0937","ss");
        unicode.put("\u0938","s");
        unicode.put("\u0939","h");

        // represent it\
        //  unicode.put("\u093c","'"); // avagraha using "'"
        //  unicode.put("\u093d","'"); // avagraha using "'"
        unicode.put("\u0969","3"); // 3 equals to pluta
        unicode.put("\u014F","Z");// Z equals to upadhamaniya
        unicode.put("\u0CF1","V");// V equals to jihvamuliya....but what character have u settled for jihvamuliya
     /*   unicode.put("\u0950","Ω"); // aum
        unicode.put("\u0958","κ"); // Urdu qaif
        unicode.put("\u0959","Κ"); //Urdu qhe
        unicode.put("\u095A","γ"); // Urdu gain
        unicode.put("\u095B","ζ"); //Urdu zal, ze, zoe
        unicode.put("\u095E","φ"); // Urdu f
        unicode.put("\u095C","δ"); // Hindi 'dh' as in padh
        unicode.put("\u095D","Δ"); // hindi dhh*/
        unicode.put("\u0926\u093C","τ"); // Urdu dwad
        unicode.put("\u0924\u093C","θ"); // Urdu toe
        unicode.put("\u0938\u093C","σ"); // Urdu swad, se
    }

    ConversionTable()
    {
        populateHashTable();
    }

    public String transform(String s1)
    {

        StringBuilder transformed = new StringBuilder();

        int strLen = s1.length();
        ArrayList<String> shabda = new ArrayList<>();
        String lastEntry = "";

        for (int i = 0; i < strLen; i++)
        {
            char c = s1.charAt(i);
            String varna = String.valueOf(c);

            Log.d(TAG, "transform: " + varna + "\n");

            String halant = "0x0951";

            if (VowelUtil.isConsonant(varna))
            {
                Log.d(TAG, "transform: " + unicode.get(varna));
                shabda.add(unicode.get(varna));
                shabda.add(halant); //halant
                lastEntry = halant;
            }

            else if (VowelUtil.isVowel(varna))
            {
                Log.d(TAG, "transform: " + "Vowel Detected...");
                if (halant.equals(lastEntry))
                {
                    if (varna.equals("a"))
                    {
                        shabda.set(shabda.size() - 1,"");
                    }
                    else
                    {
                        shabda.set(shabda.size() - 1, unicode.get(varna));
                    }
                }

                else
                {
                    shabda.add(unicode.get(varna));
                }
                lastEntry = unicode.get(varna);
            } // end of else if is-Vowel

            else if (unicode.containsKey(varna))
            {
                shabda.add(unicode.get(varna));
                lastEntry = unicode.get(varna);
            }
            else
            {
                shabda.add(varna);
                lastEntry = varna;
            }

        } // end of for

        for (String string: shabda)
        {
            transformed.append(string);
        }

        //Discard the shabda array
        shabda = null;
        return transformed.toString(); // return transformed;
    }

}