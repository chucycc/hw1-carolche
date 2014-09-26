package Type.output;

public class Span {
  private int start;
  private int end;
  public Span(int st, int ed){
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
  public Span ignoreSpace(String text){
    int startCount = 0;
    int endCount = 0;
    for (int i = 0; i < end; i++) {
       if (i < start) {
        if (text.charAt(i) != ' ') {
          startCount++;
          endCount++;
        }
      }
      else {
        if (text.charAt(i) != ' ') {
          endCount++;
        }
      }
    }
    return new Span(startCount, endCount-1);
  }
}