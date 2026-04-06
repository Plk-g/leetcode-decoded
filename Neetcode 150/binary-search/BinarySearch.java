/**
 * 704. Binary Search
 * Difficulty: Easy
 * Pattern:    Binary Search
 * Link:       https://leetcode.com/problems/binary-search/
 *
 * PROBLEM:
 * Given a sorted array of distinct integers nums and an integer target, return
 * the index of target if it exists, otherwise return -1.
 * Example: nums = [-1,0,3,5,9,12], target = 9 → 4
 *
 * INTUITION:
 * In a sorted array, comparing the middle element to target tells you whether
 * to search the left half or the right half, cutting the search space in half
 * each step — O(log n) instead of scanning linearly.
 *
 * APPROACH:
 * 1. Edge case: empty array → -1
 * 2. Maintain left and right bounds (inclusive)
 * 3. While left <= right: midpoint = left + (right - left) / 2 (avoids overflow)
 * 4. If nums[midpoint] == target, return midpoint
 * 5. Else shrink the range toward the half that can still contain target
 * 6. Return -1 if the loop exits without a hit
 *
 * COMPLEXITY:
 * Time:  O(log n) — halving the search interval each iteration
 * Space: O(1) — only index variables
 */
class BinarySearch {
    public int search(int[] nums, int target) {
        if (nums.length == 0) {
            return -1;
        }

        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int midpoint = left + (right - left) / 2;
            if (nums[midpoint] == target) {
                return midpoint;
            } else if (nums[midpoint] > target) {
                right = midpoint - 1;
            } else {
                left = midpoint + 1;
            }
        }
        return -1;
    }
}
