package bih.nic.medhasoft;

public class VowelUtil {

    protected static boolean isVowel(String strVowel) {
        // Log.logInfo("came in is_Vowel: Checking whether string is a Vowel");
        return strVowel.equals("a") || strVowel.equals("aa") || strVowel.equals("i") || strVowel.equals("ee") ||
                strVowel.equals("u") || strVowel.equals("oo") || strVowel.equals("ri") || strVowel.equals("lri") || strVowel.equals("e")
                || strVowel.equals("ai") || strVowel.equals("o") || strVowel.equals("au") || strVowel.equals("om");
    }

    protected static boolean isConsonant(String strConsonant) {
        // Log.logInfo("came in is_consonant: Checking whether string is a
        // consonant");
        return strConsonant.equals("k") || strConsonant.equals("kh") || strConsonant.equals("g")
                || strConsonant.equals("gh") || strConsonant.equals("ng") || strConsonant.equals("ch") || strConsonant.equals("chh") || strConsonant.equals("j")
                || strConsonant.equals("jh") || strConsonant.equals("ny") || strConsonant.equals("t") || strConsonant.equals("th") ||
                strConsonant.equals("d") || strConsonant.equals("dh") || strConsonant.equals("n") || strConsonant.equals("nn") || strConsonant.equals("p") ||
                strConsonant.equals("ph") || strConsonant.equals("b") || strConsonant.equals("bh") || strConsonant.equals("m") || strConsonant.equals("y") ||
                strConsonant.equals("r") || strConsonant.equals("rr") || strConsonant.equals("l") || strConsonant.equals("ll") || strConsonant.equals("lll") ||
                strConsonant.equals("v") || strConsonant.equals("sh") || strConsonant.equals("ss") || strConsonant.equals("s") || strConsonant.equals("h") ||
                strConsonant.equals("3") || strConsonant.equals("z") || strConsonant.equals("v") || strConsonant.equals("Ω") ||
                strConsonant.equals("κ") || strConsonant.equals("K") || strConsonant.equals("γ") || strConsonant.equals("ζ") || strConsonant.equals("φ") ||
                strConsonant.equals("δ") || strConsonant.equals("Δ") || strConsonant.equals("τ") || strConsonant.equals("θ") || strConsonant.equals("σ");
    }
}