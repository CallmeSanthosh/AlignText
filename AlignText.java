/**
*
* A class file that reads in a number of paragraphs of text from a file and aligns the text with a line wrapping at a specified line length.
*
* @author skj6
*
*/
public class AlignText {
    public static final String EMPTY_SPACE = " ";
    public static final String PIPE = "|";
    public static final String HYPHEN = "-";
    public static final String UNDERSCORE = "_";
    public static final String BACKSLASH = "\\";
    public static final String NUM_REGEX = "[0-9]+";
    public static final String L = "L";
    public static final String R = "R";
    public static final String B = "B";
    public static final String C = "C";
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final String TXT_EXT = ".txt";
    /** regex pattern to validate text align arguments. */
    public static final String ARG_REGEX = "[L|R|C|B]";
    /** Error message for invalid/missing first or second or both arguments. */
    public static final String ERR_MSG1 = "usage: java AlignText file_name line_length";
    /** Error message for invalid third argument. */
    public static final String ERR_MSG2 = "usage: java AlignText file_name line_length [align_mode]";

     /**
     * This main function handles argument conditions. On valid arguments, passes the control to traverseParagraph function which executes the text alignment logic. For invalid argument, prints corresponding error message and throws exception.
     * @param args it comes with values like file name, alignment length and alignment style (optional) for text alignment.
     */
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println(AlignText.ERR_MSG1);
                throw new Exception();
            } else if (args.length == 1 || (args.length == 2 && (!args[0].endsWith(AlignText.TXT_EXT) || !args[1].matches(AlignText.NUM_REGEX)))) {
                System.out.println(AlignText.ERR_MSG1);
                throw new Exception();
            } else if (args.length == AlignText.THREE && !args[2].matches(AlignText.ARG_REGEX)) {
                System.out.println(AlignText.ERR_MSG2);
                throw new Exception();
            } else {
                traverseParagraph(args);
            }
        } catch (Exception e) { }
    }
     /**
     * This method handles the business logic for aligning the text with a line wrapping at a specified line length.
     * @param args it comes with values like file name, alignment length and alignment style (optional) for text alignment.
     */
    public static void traverseParagraph(String[] args) {
        //Read the text file specified by filename and store an array of paragraphs found in the file.
        String[] paragraphs = FileUtil.readFile(args[0]);
        StringBuilder para = new StringBuilder();
        //Condition to left align default if a third argument is not specified
        String align = args.length == 2 ? AlignText.L : args[2];
        int lineLen =  Integer.parseInt(args[1]);
        //Find a longest word length in an array of paragraphs and add 4 to include pipe and empty space before and after that word.
        int longestWordLen = findLongestWord(paragraphs) + AlignText.FOUR;
        //Condition to find which length (longest word length or wrap line length) should be considered for bubble alignment.
        longestWordLen = longestWordLen >= lineLen ? longestWordLen : lineLen;
        for (int i = 0; i < paragraphs.length; i++) {
            String[] words = paragraphs[i].split(AlignText.EMPTY_SPACE);
            for (int j = 0; j < words.length; j++) {
                para.append(words[j]).append(AlignText.EMPTY_SPACE);
                if (AlignText.B.equals(align)) {
                    //Condition to print underscore line at the top of bubble text align
                    if (i == 0 && j == 0) {
                        System.out.println(AlignText.EMPTY_SPACE + AlignText.UNDERSCORE.repeat(longestWordLen - 2));
                    }
                    //Insert pipe symbol and empty space at the start of each paragraph's first word.
                    if (j == 0) {
                        para.insert(0, AlignText.PIPE + AlignText.EMPTY_SPACE);
                    }
                    //If the word length length plus 4 () is equal to wrap line length, print it as bubble aligned text.
                    if ((words[j].length() + AlignText.FOUR) == longestWordLen) {
                        System.out.println(AlignText.PIPE + AlignText.EMPTY_SPACE + words[j].trim() + AlignText.EMPTY_SPACE + AlignText.PIPE);
                        para.replace(0, para.length(), AlignText.PIPE + AlignText.EMPTY_SPACE);
                    }
                    //If the concatenated strings length plus next word length in the same array of words are greater than wrap line length, print it as bubble aligned text.
                    //Else if condition ignores to check last word of a paragraph to avoid array out of index exception while comparing with next index word.
                    //Added 2 considering a empty space and pipe symbol at the end of each line.
                    else if ((j != words.length - 1) && ((para.length() + words[j + 1].length() + 2) > lineLen)) {
                        System.out.println(para.toString().trim() + AlignText.EMPTY_SPACE.repeat(longestWordLen - para.toString().length()) + AlignText.PIPE);
                        para.replace(0, para.length(), AlignText.PIPE + AlignText.EMPTY_SPACE);
                    }
                    //Else if condition always prints the last word of a paragraph with/without the concatenated strings.
                    else if ((j == words.length - 1)) {
                        System.out.println(para.toString().trim() + AlignText.EMPTY_SPACE.repeat(longestWordLen - para.toString().length()) + AlignText.PIPE);
                    }
                    //Condition to print hyphen line and backslash tail at the bottom of bubble text align
                    if (i == paragraphs.length - 1 && j == words.length - 1) {
                        System.out.println(AlignText.EMPTY_SPACE + AlignText.HYPHEN.repeat(longestWordLen - 2));
                        System.out.println(AlignText.EMPTY_SPACE.repeat(AlignText.EIGHT) + AlignText.BACKSLASH);
                        System.out.println(AlignText.EMPTY_SPACE.repeat(AlignText.NINE) + AlignText.BACKSLASH);
                    }
                }
                //This section executes all the alignments except for bubble align.
                else {
                    if (words[j].length() > lineLen) {
                        System.out.println(words[j].trim());
                        para.setLength(0);
                    } else if (AlignText.L.equals(align)) {
                        leftAlign(j, words, para, lineLen);
                    } else if (AlignText.R.equals(align)) {
                        rightAlign(j, words, para, lineLen);
                    } else if (AlignText.C.equals(align)) {
                        centreAlign(j, words, para, lineLen);
                    }
                }
            }
            para.setLength(0);
        }
    }
     /**
     * Attempts to find the longest word length in the array of paragraphs. This is useful for bubble alignment to avoid words falling out of boundary.
     * @param paragraphs Array of paragraphs that were read from the file.
     * @return Returns a integer, a longest word length in the array of paragraphs.
     */
    public static int findLongestWord(String[] paragraphs) {
        int longestWordLen = 0;
        for (int i = 0; i < paragraphs.length; i++) {
            String[] words = paragraphs[i].split(AlignText.EMPTY_SPACE);
            for (int j = 0; j < words.length; j++) {
                if (words[j].length() > longestWordLen) {
                    longestWordLen = words[j].length();
                }
            }
        }
        return longestWordLen;
    }
    /**
     * Attempts to print the concatenated strings to a specified line wrap length in the left alignment format.
     * @param j index to read a specific word from a array of words.
     * @param words an array of strings, each string representing a word from a single paragraph.
     * @param para concatenated strings, its length is less than line wrapping length.
     * @param lineLen line wrapping length.
     */
    public static void leftAlign(int j, String[] words, StringBuilder para, int lineLen) {
        if ((j != words.length - 1) && ((para.length() + words[j + 1].length()) > lineLen)) {
            System.out.println(para.toString().trim());
            para.setLength(0);
        } else if ((j == words.length - 1)) {
            System.out.println(para.toString().trim());
        }
    }
    /**
     * Attempts to print the concatenated strings to a specified line wrap length in the right alignment format.
     * @param j index to read a specific word from a array of words.
     * @param words an array of strings, each string representing a word from a single paragraph.
     * @param para concatenated strings, its length is less than line wrapping length.
     * @param lineLen line wrapping length.
     */
    public static void rightAlign(int j, String[] words, StringBuilder para, int lineLen) {
        if ((j != words.length - 1) && ((para.length() + words[j + 1].length()) > lineLen)) {
            //Introduced repeat function over regular iteration to add empty spaces before the line for right alignment
           System.out.println(AlignText.EMPTY_SPACE.repeat(lineLen - para.toString().trim().length()) + para.toString().trim());
            para.setLength(0);
        } else if ((j == words.length - 1)) {
            System.out.println(AlignText.EMPTY_SPACE.repeat(lineLen - para.toString().trim().length()) + para.toString().trim());
        }
    }
    /**
     * Attempts to print the concatenated strings to a specified line wrap length in the centre alignment format.
     * @param j index to read a specific word from a array of words.
     * @param words an array of strings, each string representing a word from a single paragraph.
     * @param para concatenated strings, its length is less than line wrapping length.
     * @param lineLen line wrapping length.
     */
    public static void centreAlign(int j, String[] words, StringBuilder para, int lineLen) {
        if ((j != words.length - 1) && ((para.length() + words[j + 1].length()) > lineLen)) {
            //Introduced repeat function over regular iteration to add empty spaces before the line for centre alignment.
            System.out.println(AlignText.EMPTY_SPACE.repeat((int) Math.ceil((double) (lineLen - para.toString().trim().length()) / 2)) + para.toString().trim());
            para.setLength(0);
        } else if ((j == words.length - 1)) {
            System.out.println(AlignText.EMPTY_SPACE.repeat((int) Math.ceil((double) (lineLen - para.toString().trim().length()) / 2)) + para.toString().trim());
        }
    }
}
