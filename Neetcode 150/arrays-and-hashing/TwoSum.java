/**
 * 1. Two Sum
 * Difficulty: Easy
 * Pattern:    Arrays & Hashing
 * Link:       https://leetcode.com/problems/two-sum/
 *
 * PROBLEM:
 * Given an array of integers and a target, return indices of the two numbers
 * that add up to target. Exactly one solution exists.
 * Example: nums = [2,7,11,15], target = 9 → [0,1]
 *
 * INTUITION:
 * For every number x, we need (target - x). Instead of rescanning the array
 * each time (O(n²)), store visited numbers in a hash map for O(1) lookup.
 * The moment we find the complement already stored, we're done.
 *
 * APPROACH:
 * 1. Create HashMap<Integer, Integer> mapping value → index
 * 2. For each nums[i], compute complement = target - nums[i]
 * 3. If complement is in map → return [map.get(complement), i]
 * 4. Else store nums[i] → i and continue
 *
 * COMPLEXITY:
 * Time:  O(n) — one pass, O(1) lookups
 * Space: O(n) — storing up to n elements in the map
 */
import java.util.HashMap;
import java.util.Map;

class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) {
                return new int[]{ seen.get(complement), i };
            }
            seen.put(nums[i], i);
        }
        return new int[]{};
    }
}
