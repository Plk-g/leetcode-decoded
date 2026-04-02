/**
 * 20. Valid Parentheses
 * Difficulty: Easy
 * Pattern:    Stack
 * Link:       https://leetcode.com/problems/valid-parentheses/
 *
 * PROBLEM:
 * Given a string s containing only '(', ')', '{', '}', '[' and ']', determine
 * if the input string is valid: brackets must close in the correct order and
 * every opener must have a matching closer.
 * Example: "()" → true, "(]" → false
 *
 * INTUITION:
 * Scan left to right. Opening brackets need a future closing partner in LIFO
 * order — exactly what a stack provides. Push opens; on a close, the top of the
 * stack must be the matching open or the string is invalid.
 *
 * APPROACH:
 * 1. If length is odd, return false (cannot pair all symbols)
 * 2. Use Stack<Character> for pending opens
 * 3. For each char: if open, push; if close, require non-empty stack and peek match, then pop
 * 4. Otherwise return false (unexpected close or wrong type)
 * 5. Valid only if stack is empty at the end
 *
 * COMPLEXITY:
 * Time:  O(n) — one pass over s
 * Space: O(n) — stack holds up to n/2 opens in the worst case
 */
import java.util.Stack;

class ValidParentheses {
    public boolean isValid(String s) {
        if (s.length() % 2 != 0) {
            return false;
        }

        Stack<Character> stack = new Stack<>();

        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else if (c == ')' && !stack.isEmpty() && stack.peek() == '(') {
                stack.pop();
            } else if (c == ']' && !stack.isEmpty() && stack.peek() == '[') {
                stack.pop();
            } else if (c == '}' && !stack.isEmpty() && stack.peek() == '{') {
                stack.pop();
            } else {
                return false;
            }
        }

        return stack.isEmpty();
    }
}
