I dont understand why this leetcode works, mainly only this specific line (I dont get why the math would not produce a non integer):
            totalKeyPresses += frequency[i] * ((i + 9) / 9);


here is the whole code:
class Solution {
    public int minimumKeypresses(String s) {
        Integer[] frequency = new Integer[26];
        Arrays.fill(frequency, 0);
        int totalKeyPresses = 0;

        for (int i = 0; i < s.length(); i++) {
            frequency[s.charAt(i) - 'a']++;
        }

        Arrays.sort(frequency, (x, y) -> y - x);

        for (int i = 0; i < frequency.length; i++) {
            totalKeyPresses += frequency[i] * ((i + 9) / 9);
        }

        return totalKeyPresses;
    }
}

here is the question:
You have a keypad with 9 buttons, numbered from 1 to 9, each mapped to lowercase English letters. You can choose which characters each button is matched to as long as:

All 26 lowercase English letters are mapped to.
Each character is mapped to by exactly 1 button.
Each button maps to at most 3 characters.
To type the first character matched to a button, you press the button once. To type the second character, you press the button twice, and so on.

Given a string s, return the minimum number of keypresses needed to type s using your keypad.

Note that the characters mapped to by each button, and the order they are mapped in cannot be changed.

 