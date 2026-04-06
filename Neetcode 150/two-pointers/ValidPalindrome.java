/**
 * 125. Valid Palindrome
 * Difficulty: Easy
 * Pattern:    Two Pointers
 * Link:       https://leetcode.com/problems/valid-palindrome/
 *
 * PROBLEM:
 * Given a string s, return true if it reads the same forward and backward
 * after removing non-alphanumeric characters and ignoring case.
 * Example: "A man, a plan, a canal: Panama" → true
 *
 * INTUITION:
 * Palindrome means the first and last characters match, then the next inward
 * pair, and so on. Filter to letters/digits only, normalize case, then walk
 * two indices from both ends toward the center.
 *
 * APPROACH:
 * 1. Scan s and append alphanumeric chars to a new string
 * 2. Lowercase the filtered string
 * 3. Two pointers from both ends; compare and move inward
 * 4. Return false on mismatch; true when the pointers finish
 *
 * COMPLEXITY:
 * Time:  O(n²) worst case for building the filtered string with += in Java
 *        (strings are immutable); two-pointer pass is O(k) on filtered length k
 * Space: O(n) — filtered string
 */
class ValidPalindrome {
    public boolean isPalindrome(String s) {
        String new_s = "";

        for (char c : s.toCharArray()) {
            if (Character.isDigit(c) || Character.isLetter(c)) {
                new_s += c;
            }
        }
        new_s = new_s.toLowerCase();

        int a_pointer = 0;
        int b_pointer = new_s.length() - 1;

        while (a_pointer <= b_pointer) {
            if (new_s.charAt(a_pointer) != new_s.charAt(b_pointer)) {
                return false;
            }
            a_pointer += 1;
            b_pointer -= 1;
        }

        return true;
    }
}
