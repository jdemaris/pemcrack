package com.pemcrack

/**
 * Given an alphabet (a list of characters), a prefix (our subsection to work on),
 * and a depth (how many characters should be appended to the prefix), we can
 * calculate the number of combinations in this unit of work, split it out into
 * sub-sets or iterate through all of the combinations.
 *
 * We only know one way of splitting the work into sub-work and that's by creating
 * one subset for each character in the alphabet and decrementing the depth
 */
class PasswordGenerator(alphabet: String, prefix: String, depth: Int) extends Iterable[String] {
  /**
   * @return # of passwords in this unit of work
   */
  def combinations : BigDecimal = BigDecimal(Math.pow(alphabet.length, depth))

  /**
   * Generates sub units of work
   *
   * @return List of PasswordGenerator objects each containing a subset of the work
   */
  def split : List[PasswordGenerator] = {
    if ( depth == 0 )
      List()
    else
      alphabet.toCharArray.toList.map(_.toString).map(
        a => new PasswordGenerator(alphabet, prefix + a, depth - 1)
      )
  }

  /**
   * Allows the consumer to iterate over all of the combinations, one at a time. Useful
   * for the operator actually doing the work and not splitting it further.
   */
  override def iterator: Iterator[String] = new Iterator[String] {
    var initialized = false
    var usedLast = false

    /**
     * Stores a list of indexes of where we are in iterating through the combinations.
     *
     * This index is stored in reverse. The first entry reflects the end of the produced
     * string. For example, a List(1, 2, 3) would generate a string of "cba".
     */
    var state: List[Int] = List[Int]()

    override def hasNext: Boolean = {
      if ( !initialized ) {
        initialized = true
        for ( i <- Range(0, depth) ) {
          state = 0 :: state
        }
      }
      !usedLast
    }

    override def next(): String = {
      val suffix = new String(state.reverse.map(alphabet.charAt(_)).toArray)
      if ( !state
        .map(_ >= alphabet.length - 1)
        .reduceOption((a, b) => a && b)
        .getOrElse(true)
      ) {
        state = increment(state, alphabet.length)
      } else {
        usedLast = true
      }
      prefix + suffix
    }

    /**
     * @param state
     * @param max The max value (non-inclusive) allowed for an individual index entry
     * @return
     */
    def increment(state: List[Int], max: Int) : List[Int] = {
      if ( state.length > 0 ) {
        val least = state.head + 1
        if (least < max)
          least :: state.tail
        else
          0 :: increment(state.tail, max)
      } else {
        throw new ArrayIndexOutOfBoundsException
      }
    }
  }
}
