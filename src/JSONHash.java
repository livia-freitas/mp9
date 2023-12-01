package src;
import java.io.PrintWriter;
import java.util.*;

/**
 * JSON hashes/objects.
 *
 * @author Joyce Gill
 * @author Christina Vu
 * @author Livia Stein Freitas
 */
public class JSONHash implements JSONValue {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+
  /* Starting size of JSONHash */
  public static final int DEFAULT_CAPACITY = 16;
  public static final double LOAD_FACTOR = 0.75;

  /* Hash Table */
  public KVPair<JSONString, JSONValue>[] table;

  /* Number of key/value pairs */
  public int size;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  @SuppressWarnings("unchecked")
  public JSONHash() {
    this.table = new KVPair[DEFAULT_CAPACITY];
    this.size = 0;
  } // JSONHash()

  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    StringBuilder builder = new StringBuilder("{");
    boolean first = true;

    // Iterate through key value pairs
    for (KVPair<JSONString, JSONValue> entry : table) {
      if (entry != null) {
        if (!first) {
          builder.append(", ");
        } // if

        // Append the key and value to the string
        builder.append(entry.key()).append(": ").append(entry.value());
        first = false;
      } // if
    } // for
    builder.append("}");
    return builder.toString();
  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    // Check if they're the same
    if (this == other) {
      return true;
    } // if
    if (other == null || getClass() != other.getClass()) {
      return false;
    } // if

    JSONHash jsonHash = (JSONHash) other;
   
    // Compare the size and content of the hash tables for equality
    return size == jsonHash.size && table.equals(jsonHash.table);
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    return table.hashCode();          
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) {
    pen.print(toString());
    pen.flush();
  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public Iterator<KVPair<JSONString,JSONValue>> getValue() {
    return this.iterator();
  } // getValue()

  /**
   * Returns the hash of the key
   */
  public int hash(JSONString key) {
    return key.hashCode() % table.length;
  } // hash (JSONString)

  /**
   * Updates the size of the table
   */
  @SuppressWarnings("unchecked")
  public void resize() {
    int newCapacity = table.length * 2;

     // Create a new table with the updated capacity
    KVPair<JSONString, JSONValue>[] newTable = new KVPair[newCapacity];

     // Iterate through the existing entries and rehash them into the new table
    for (KVPair<JSONString, JSONValue> entry : table) {
        if (entry != null) {
            int index = hash(entry.key());

            // Find the next available slot in the new table
            while (newTable[index] != null) {
                index = (index + 1) % newCapacity;
            } // while
            newTable[index] = entry;
        } // if
    } // for
    table = newTable;
} // resize()

  // +-------------------+-------------------------------------------
  // | Hashtable methods |
  // +-------------------+

  /**
   * Get the value associated with a key.
   */
  public JSONValue get(JSONString key) {
    int index = hash(key);
    // Iterate until the corresponding key or empty space is found
    while (table[index] != null && !table[index].key().equals(key)) {
      index = (index + 1) % table.length;
    } // while

    // Return value associated with the key or null accordingly
    return (table[index] != null) ? table[index].value() : null;
  } // get(JSONString)

  /**
   * Get all of the key/value pairs.
   */
  public Iterator<KVPair<JSONString,JSONValue>> iterator() {
    return new HashTableIterator();
  } // iterator()

  /**
   * Set the value associated with a key.
   */
  public void set(JSONString key, JSONValue value) {
    // Resize if necessary
    if ((double) size / table.length >= LOAD_FACTOR) {
      resize();
    } // if

    int index = hash(key);
    // Iterate until the corresponding key or empty space is found
    while (table[index] != null && !table[index].key().equals(key)) {
      index = (index + 1) % table.length;
    } // while

    // Create a new key value pair if the slot is empty
    if (table[index] == null) {
      table[index] = new KVPair<>(key, value);
      size++;
    } // if

    // Otherwise, update the existing value
    else {
      table[index].setValue(value);
    } // else
} // set(JSONString, JSONValue)

  /**
   * Find out how many key/value pairs are in the hash table.
   */
  public int size() {
    return size;        
  } // size()

  /**
   * Creates a Hash Table Iterator
   */
  public class HashTableIterator implements Iterator<KVPair<JSONString, JSONValue>> {
    // Index to keep track of current position
    public int currentIndex = 0;

    /**
     * Checks if there is a next element in the hash table
     */
    @Override
    public boolean hasNext() {
      // While there exists a key/value pair and it is less than the table's length
      while (currentIndex < table.length && table[currentIndex] == null) {
        currentIndex++;
      } // while
      return currentIndex < table.length;
    } // hasNext()

    /**
     * Retrieves the next key-value pair in the hash table
     */
    @Override
    public KVPair<JSONString, JSONValue> next() {
      if (!hasNext()) {
        throw new IllegalStateException("No more elements in the hash table");
      } // if
      // Returns the current key/value pair and moves the index to the next positino
      return table[currentIndex++];
    } // next()
  } // class HashTableIterator
 
} // class JSONHash
