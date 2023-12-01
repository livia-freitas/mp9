package src;

import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;

/*
 * Authors: Christina Vu, Joyce Gill, Livia Stein Freitas
 * An implementation of a JSON parser. Utilities for our simple implementation of JSON.
 */
public class JSON {
    // +---------------+-----------------------------------------------
    // | Static fields |
    // +---------------+

    /**
     * The current position in the input.
     */

    static int pos;
    // +----------------+----------------------------------------------
    // | Static methods |
    // +----------------+

    /**
     * Parse a string into JSON.
     */
    public static JSONValue parse(String source) throws ParseException, IOException {
        return parse(new StringReader(source));
    }// parse(String)

    /**
     * Parse a file into JSON.
     */
    public static JSONValue parseFile(String filename) throws ParseException, IOException {
        FileReader reader = new FileReader(filename);
        JSONValue result = parse(reader);
        reader.close();
        return result;
    } // parseFile(String)

    /**
     * Parse JSON from a reader.
     */
    public static JSONValue parse(Reader source) throws ParseException, IOException {
        pos = 0;
        JSONValue result = parseKernel((Reader) source);
        if (-1 != skipWhitespace((PushbackReader) source)) {
            throw new ParseException("Characters remain at end", pos);
        }
        return result;
    } // parse(Reader)
    // +---------------+-----------------------------------------------
    // | Local helpers |
    // +---------------+

    /**
     * Parse JSON from a reader, keeping track of the current position
     */


    public static JSONValue parseKernel(Reader source) throws ParseException, IOException {
        int ch = skipWhitespace(source);

        if (-1 == ch) {
            throw new ParseException("Unexpected end of file", pos);
        }

        if (ch == '"') {
            return parseString(source);
        } else if (ch == '{') {
            return parseObject(source);
        } else if (ch == '[') {
            return parseArray(source);
        } else if (Character.isDigit((char) ch) || ch == '-') {
            return parseNumber(source);
        } else if (ch == 't' || ch == 'f' || ch == 'n') {
            return parseConstant(source);
        } else {
            throw new ParseException("Unexpected character: " + (char) ch, pos);
        }
    }

    public static JSONString parseString(Reader source) throws ParseException, IOException {
        StringBuilder builder = new StringBuilder();
        int ch;
        while ((ch = source.read()) != -1 && ch != '"') {
            builder.append((char) ch);
        }
        if (ch == -1) {
            throw new ParseException("Unterminated string", pos);
        }
        return new JSONString(builder.toString());
    }

    public static JSONHash parseObject(Reader source) throws ParseException, IOException {
        JSONHash jsonObject = new JSONHash();
        int ch;
        while ((ch = skipWhitespace(source)) != -1 && ch != '}') {
            JSONString key = parseString(source);
            if (skipWhitespace(source) != ':') {
                throw new ParseException("Expected ':' after key", pos);
            }
            JSONValue value = parseKernel(source);
            jsonObject.set(key, value);
            ch = skipWhitespace(source);
            if (ch == '}') {
                break;
            } else if (ch != ',') {
                throw new ParseException("Expected ',' or '}'", pos);
            }
        }
        if (ch == -1) {
            throw new ParseException("Unterminated object", pos);
        }
        return jsonObject;
    }

    public static JSONArray parseArray(Reader source) throws ParseException, IOException {
        JSONArray jsonArray = new JSONArray();
        int ch;
        while ((ch = skipWhitespace(source)) != -1 && ch != ']') {
            JSONValue value = parseKernel(source);
            jsonArray.add(value);
            ch = skipWhitespace(source);
            if (ch == ']') {
                break;
            } else if (ch != ',') {
                throw new ParseException("Expected ',' or ']'", pos);
            }
        }
        if (ch == -1) {
            throw new ParseException("Unterminated array", pos);
        }
        return jsonArray;
    }

    public static JSONValue parseNumber(Reader source) throws ParseException, IOException {
        StringBuilder builder = new StringBuilder();
        int ch;
        while ((ch = source.read()) != -1
                && (Character.isDigit(ch) || ch == '.' || ch == 'e' || ch == 'E' || ch == '-')) {
            builder.append((char) ch);
        }
        if (ch != -1) {
            ((PushbackReader) source).unread(ch);
        }
        String numberStr = builder.toString();
        if (numberStr.contains(".") || numberStr.toLowerCase().contains("e")) {
            return new JSONReal(numberStr);
        } else {
            return new JSONInteger(numberStr);
        }
    }

    public static JSONValue parseConstant(Reader source) throws ParseException, IOException {
        StringBuilder builder = new StringBuilder();
        int ch;
        while ((ch = source.read()) != -1 && Character.isLetter(ch)) {
            builder.append((char) ch);
        }
        if (ch != -1) {
            ((PushbackReader) source).unread(ch);
        }
        String constantStr = builder.toString();
        switch (constantStr) {
            case "true":
                return JSONConstant.TRUE;
            case "false":
                return JSONConstant.FALSE;
            case "null":
                return JSONConstant.NULL;
            default:
                throw new ParseException("Invalid constant: " + constantStr, pos);
        }
    }

    /**
     * Get the next character from source, skipping over whitespace.
     */
    public static int skipWhitespace(Reader source) throws IOException {
        int ch;
        do {
            ch = source.read();
            ++pos;
        } while (isWhitespace(ch));
        return ch;
    }// skipWhitespace(Reader)

    /**
     * Determine if a character is JSON whitespace (newline, carriage return, space, or tab).
     */

    public static boolean isWhitespace(int ch) {
        return (' ' == ch) || ('\n' == ch) || ('\r' == ch) || ('\t' == ch);
    } // isWhiteSpace(int)
}
