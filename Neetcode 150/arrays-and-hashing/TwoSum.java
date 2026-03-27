/**
 * 1. Two Sum
 * Difficulty: Easy
 * Pattern:    Arrays & Hashing
 * Link:       https://leetcode.com/problems/two-sum/
 *
 * PROBLEM:
 * Given an integer array nums and a target, return indices of the two numbers
 * such that they add up to target. Exactly one valid answer exists.
 * Example: nums = [2,7,11,15], target = 9 → [0,1]
 *
 * INTUITION:
 * For each value x, we need to know whether we have already seen (target - x).
 * Instead of scanning previous values each time, store the needed complement as
 * a key in a hash map and its index as the value. When current nums[i] is a key,
 * we found the earlier index immediately.
 *
 * APPROACH:
 * 1. Create HashMap<Integer, Integer> to store needed complement -> index
 * 2. Iterate through nums with index i
 * 3. If nums[i] exists as a key in the map, return [storedIndex, i]
 * 4. Otherwise store (target - nums[i]) -> i and continue
 * 5. Return empty array only as a fallback
 *
 * COMPLEXITY:
 * Time:  O(n) — one pass with O(1) average hash lookups
 * Space: O(n) — map stores up to n complements
 */
import java.util.HashMap;
import java.util.Map;

class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> complements = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            Integer complementIndex = complements.get(nums[i]);
            if (complementIndex != null) {
                return new int[]{complementIndex, i};
            }
            complements.put(target - nums[i], i);
        }
        return new int[]{};
    }
}
