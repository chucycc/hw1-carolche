package uima;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import Type.input.Sentence;
import Type.output.Gene;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.util.AbstractExternalizable;

public class GeneAnnotator extends JCasAnnotator_ImplBase {
  
  Chunker chunker = null;

  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    File modelFile = new File("src/main/resources/ne-en-bio-genetag.HmmChunker");
    try {
      chunker = (Chunker) AbstractExternalizable.readObject(modelFile);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    Iterator iter = jcas.getAnnotationIndex(Sentence.type).iterator();
    while (iter.hasNext()) {
      Sentence s = (Sentence) iter.next();
      String text = s.getText();
      Chunking chunking = chunker.chunk(text);
      Set<Chunk> chunkResult = chunking.chunkSet();
      Iterator chunkIter = chunkResult.iterator();
      while (chunkIter.hasNext()) {
        Chunk c = (Chunk) chunkIter.next();
        if (c.type().equals("GENE")) {
          Gene newGene = new Gene(jcas);
          
          Span span = new Span(c.start(), c.end());
          Span CorrectSpan = span.ignoreSpace(text);
          
          newGene.setId(s.getId());
          newGene.setBegin(CorrectSpan.getStart());
          newGene.setEnd(CorrectSpan.getEnd());
          newGene.setName(text.substring(c.start(), c.end()));
          newGene.addToIndexes();
        }
      }
    }
  }
  
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

}
