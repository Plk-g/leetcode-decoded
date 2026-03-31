/**
 * 121. Best Time to Buy and Sell Stock
 * Difficulty: Easy
 * Pattern:    Sliding Window
 * Link:       https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
 *
 * PROBLEM:
 * Given an array prices where prices[i] is a stock price on day i, choose one
 * buy day and one later sell day to maximize profit. If no profit is possible,
 * return 0.
 * Example: [7,1,5,3,6,4] → 5 (buy at 1, sell at 6)
 *
 * INTUITION:
 * The best sell on day i uses the cheapest price seen before day i. One pass
 * tracks the minimum price so far and updates the best profit whenever today’s
 * price beats that minimum.
 *
 * APPROACH:
 * 1. Track min_val as the lowest price seen so far (start at +∞)
 * 2. Track max_profit as the best gain seen so far
 * 3. For each price: update min_val if today is cheaper
 * 4. Else update max_profit if (today - min_val) is larger
 * 5. Return max_profit
 *
 * COMPLEXITY:
 * Time:  O(n) — single pass over prices
 * Space: O(1) — only two scalars
 */
class BestTimeToBuyAndSellStock {
    public int maxProfit(int[] prices) {
        int min_val = Integer.MAX_VALUE;
        int max_profit = 0;

        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < min_val) {
                min_val = prices[i];
            } else if (prices[i] - min_val > max_profit) {
                max_profit = prices[i] - min_val;
            }
        }
        return max_profit;
    }
}
