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
 * Duplicates mean we have seen a value before. A HashSet gives O(1) lookup
 * and insert: if the current number is already in the set, we found a
 * duplicate; otherwise we record it and continue.
 *
 * APPROACH:
 * 1. Create HashSet<Integer> for seen values
 * 2. Scan nums with an index loop
 * 3. If the set already contains nums[i], return true
 * 4. Otherwise add nums[i] to the set
 * 5. Return false if the loop finishes with no hit
 *
 * COMPLEXITY:
 * Time:  O(n) — one pass, O(1) expected set operations
 * Space: O(n) — up to n distinct values in the set
 */
import java.util.HashSet;

class ContainsDuplicate {
    public boolean containsDuplicate(int[] nums) {
        HashSet<Integer> numbers = new HashSet<>();

        for (int i = 0; i < nums.length; i++) {
            if (numbers.contains(nums[i])) {
                return true;
            }
            numbers.add(nums[i]);
        }

        return false;
    }
}
