package swiftanalysis.analyzers.util;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import swiftanalysis.common.Location;

/**
 * Class with utility functions that are used by listeners.
 */
public final class ListenerUtil {

    public static Location getTokenLocation(Token token) {
        return new Location(token.getLine(), token.getCharPositionInLine() + 1);
    }

    public static Location getTokenEndLocation(Token token) {
        return new Location(token.getLine(), getLastCharPositionInLine(token) + 2);
    }

    public static int getLastCharPositionInLine(Token token) {
        return token.getCharPositionInLine() + (token.getStopIndex() - token.getStartIndex());
    }

    public static Location getContextStartLocation(ParserRuleContext ctx) {
        return new Location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine() + 1);
    }

    public static Location getContextStopLocation(ParserRuleContext ctx) {
        return new Location(ctx.getStop().getLine(), ctx.getStop().getCharPositionInLine() + 1);
    }

    public static Location getLocationOfChildToken(ParserRuleContext ctx, int childNumber) {
        Token token = ((TerminalNodeImpl) ctx.getChild(childNumber)).getSymbol();
        return ListenerUtil.getTokenLocation(token);
    }

    public static Location getParseTreeStartLocation(ParseTree parseTree) {
        return (parseTree instanceof TerminalNodeImpl) ? getTokenLocation(((TerminalNodeImpl) parseTree).getSymbol())
                                                       : getContextStartLocation((ParserRuleContext) parseTree);
    }

    public static Location getParseTreeStopLocation(ParseTree parseTree) {
        return (parseTree instanceof TerminalNodeImpl) ? getTokenLocation(((TerminalNodeImpl) parseTree).getSymbol())
                       : getContextStopLocation((ParserRuleContext) parseTree);
    }

    public static boolean isComment(Token token) {
        return isSingleLineComment(token) || isMultilineComment(token);
    }

    public static boolean isSingleLineComment(Token token) {
        String text = token.getText();
        return text.startsWith("//");
    }

    public static boolean isMultilineComment(Token token) {
        String text = token.getText();
        return text.startsWith("/*");
    }

    public static boolean isNewline(Token token) {
        return token.getText().equals("\n");
    }


    /**
     * Get the line number on which the token ends.
     * @param token A token
     * @return The line number on which the token ends
     */
    public static int getEndLineOfToken(Token token) {
        String tokenText = token.getText();
        int numNewLine = 0;

        for (int i = 0; i < tokenText.length(); i++) {
            char ch = tokenText.charAt(i);
            if (ch == '\n') {
                numNewLine += 1;
            }
        }

        if (tokenText.charAt(tokenText.length() - 1) == '\n' ) {
            return token.getLine() + numNewLine - 1;
        } else {
            return token.getLine() + numNewLine;
        }
    }

    /**
     * Returns location of the end multiline comment symbol.
     *
     * @param comment A token representing a comment
     * @return Location of the end symbol
     */
    public static Location getEndOfMultilineComment(Token comment) {
        String commentText = comment.getText();
        if (commentText.charAt(commentText.length() - 1) == '\n') {
            commentText = commentText.substring(0, commentText.length() - 1);
        }
        int numNewlines = 0;
        int lastNewlineIndex = -1;
        for (int i = 0; i < commentText.length(); i++) {
            if (commentText.charAt(i) == '\n') {
                lastNewlineIndex = i;
                numNewlines += 1;
            }
        }
        String lastLine = commentText.substring(lastNewlineIndex + 1);
        return new Location(comment.getLine() + numNewlines,
            numNewlines == 0 ? comment.getCharPositionInLine() + lastLine.length() - 1 : lastLine.length() - 1);
    }
}
