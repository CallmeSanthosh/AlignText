# Text Alignment

Write a small program in your first practical that reads in a number of paragraphs of text from a file and aligns the text with a line wrapping at a specified line length.

## Requirement
1. Accept two arguments from the command line:
* First argument is the name (and path) of the file containing the text
* Second argument is the desired length of the line for wrapping the text
* If either argument is missing or invalid, you should output the following message:
usage: java AlignText file_name line_length
2. Left-align text from the file:
* Ensure each line contains the maximum number of words possible within the specified limit
* Ensure no word is split across lines
* If a single word is longer than line_length then it may go over the limit
* Output the left-aligned text to the standard output
3. Use a third command line argument to support one or more of the additional options detailed below:
* Left-align text from the file
  * Third command line argument = L
  * This should be the default if a third argument is not specified
* Right-align text from the file
  * Third command line argument = R
* Centre-align text from the file
  * Third command line argument = C
  * If there are an odd number of spaces, place the extra space at the start of the line
* Put text in a speech bubble
  * Third command line argument = B
  * Align text to left and surround it with ‘|’, ‘-’ and ‘_’ symbols
  * Add a tail at the bottom made of two ‘\’ characters
  * Every line should be of length line_length if possible
  * If a word is too long for this, the whole speech bubble should be widened
  * Should precisely match the examples below, and the automated tests
* If any argument is missing or invalid, you should output the following message:
usage: java AlignText file_name line_length [align_mode]

## MIT License
This project is covered under MIT License.
