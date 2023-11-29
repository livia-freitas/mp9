package src;

import java.io.PrintWriter;
import java.util.*;

/**
 * JSON strings.
 *
 * @author Joyce Gill
 */
public class JSONString implements JSONValue{

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The underlying string.
   */
  String value;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Build a new JSON string for a particular string.
   */
  public JSONString(String value) {
    this.value = value;
  } // JSONString(String)

  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    if (value == null) {
      return "null";
      } else {
        return this.value.toString();
      } // else
    } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    JSONString js = (JSONString) other;
    if (this.getClass() != other.getClass() || other == null) {
      return false;
    } // if
    // Compare fields of both
    return Objects.equals(value, js.value);
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    if (this.value == null)
      return 0;
    else
      return this.value.hashCode();        
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) {
    pen.write("\"\\" + this.value + "\"");
  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public String getValue() {
    return this.value;
  } // getValue()

} // class JSONString
