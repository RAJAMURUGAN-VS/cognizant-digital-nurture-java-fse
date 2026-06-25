public class StringUtils {

    public String reverse(String input) {
        if (input == null) return null;
        return new StringBuilder(input).reverse().toString();
    }

    public boolean isPalindrome(String input) {
        if (input == null) return false;
        String cleaned = input.toLowerCase();
        return cleaned.equals(new StringBuilder(cleaned).reverse().toString());
    }

    public int countVowels(String input) {
        if (input == null) return 0;
        int count = 0;
        for (char c : input.toLowerCase().toCharArray()) {
            if ("aeiou".indexOf(c) != -1) {
                count++;
            }
        }
        return count;
    }
}
