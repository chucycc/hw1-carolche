package Type.output;

/**
 * Span class is used to save a start and end point of a gene name. It also provides a method to
 * calculate a new span which ignores white spaces of the sentence.
 * 
 * @author Carol Cheng
 * 
 */
public class Span {
  private int start;

  private int end;

  /**
   * @param st
   *          Start point of the gene
   * @param ed
   *          End point of the gene
   */
  public Span(int st, int ed) {
    start = st;
    end = ed;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getEnd() {
    return end;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  /**
   * @param text
   *          The original sentence without identifier
   * @return Span Return a new span which ignores white spaces of the sentence
   */
  public Span ignoreSpace(String text) {
    int startCount = 0;
    int endCount = 0;
    for (int i = 0; i < end; i++) {
      if (i < start) {
        if (text.charAt(i) != ' ') {
          startCount++;
          endCount++;
        }
      } else {
        if (text.charAt(i) != ' ') {
          endCount++;
        }
      }
    }
    return new Span(startCount, endCount - 1);
  }
}