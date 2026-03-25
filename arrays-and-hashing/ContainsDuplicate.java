/**
 * 217. Contains Duplicate
 * Difficulty: Easy
 * Pattern:    Arrays & Hashing
 * Link:       https://leetcode.com/problems/contains-duplicate/
 *
 * PROBLEM:
 * Given an integer array, return true if any value appears at least twice,
 * false if all elements are distinct.
 * Example: [1,2,3,1] → true, [1,2,3,4] → false
 *
 * INTUITION:
 * We need instant "have I seen this before?" checks. A HashSet does exactly
 * that in O(1). The moment add() returns false, we hit a duplicate.
 *
 * APPROACH:
 * 1. Create empty HashSet<Integer>
 * 2. For each num: if set contains it → return true
 * 3. Otherwise add it and continue
 * 4. Return false if loop completes
 *
 * COMPLEXITY:
 * Time:  O(n) — one pass with O(1) set operations
 * Space: O(n) — storing up to n elements
 */
import java.util.HashSet;
import java.util.Set;

class ContainsDuplicate {
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            if (seen.contains(num)) return true;
            seen.add(num);
        }
        return false;
    }
}
