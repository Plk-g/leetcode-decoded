/**
 * 242. Valid Anagram
 * Difficulty: Easy
 * Pattern:    Arrays & Hashing
 * Link:       https://leetcode.com/problems/valid-anagram/
 *
 * PROBLEM:
 * Given two strings s and t, return true if t is an anagram of s (same
 * letters with the same frequencies). Assume lowercase English letters.
 * Example: s = "anagram", t = "nagaram" → true
 *
 * INTUITION:
 * Anagrams are permutations: same length and the same character counts.
 * If lengths differ, they cannot match. Otherwise increment counts for
 * letters in s and decrement for letters in t in lockstep; an anagram leaves
 * every count at zero.
 *
 * APPROACH:
 * 1. If s.length() != t.length(), return false
 * 2. Use int[26] for a..z
 * 3. One loop: for each index i, bump count for s.charAt(i), drop count for t.charAt(i)
 * 4. Check all 26 slots are zero
 *
 * COMPLEXITY:
 * Time:  O(n) — n = s.length(), single pass + fixed alphabet scan
 * Space: O(1) — 26 integers, independent of n
 */
class ValidAnagram {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        int[] charCounts = new int[26];
        for (int i = 0; i < s.length(); i++) {
            charCounts[s.charAt(i) - 'a']++;
            charCounts[t.charAt(i) - 'a']--;
        }
        for (int count : charCounts) {
            if (count != 0) {
                return false;
            }
        }
        return true;
    }
}
