package uima;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;

import Type.input.Sentence;
import Type.output.Gene;
import Type.output.Span;

/**
 * The CASconsumer prints out the gene annotations to a file.
 * 
 * @author Carol Cheng
 * 
 */
public class GeneCASConsumer extends CasConsumer_ImplBase {
  // Name of configuration parameter that must be set to the path of a file which will be written.
  public static final String PARAM_OUTPUTDIR = "Output";

  private File fOutput;

  private FileWriter fw;

  private BufferedWriter bw;

  /**
   * This method initialize the file which will be written.
   */
  public void initialize() throws ResourceInitializationException {
    fOutput = new File(((String) getUimaContext().getConfigParameterValue(PARAM_OUTPUTDIR)).trim());
    try {
      if (!fOutput.exists()) {
        fOutput.createNewFile();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      fw = new FileWriter(fOutput);
      bw = new BufferedWriter(fw);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method extract gene name entity from CAS and print them to output file in a specified
   * format.
   */
  @Override
  public void processCas(CAS aCAS) throws ResourceProcessException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }
    String id = null;
    String text = null;
    // Get the original sentence from jcas to use Span object to calculate a new span.
    FSIterator<Annotation> stIter = jcas.getAnnotationIndex(Sentence.type).iterator();
    if (stIter.hasNext()) {
      Sentence s = (Sentence) stIter.next();
      id = s.getId();
      text = s.getText();
    }
    // Get the gene annotations from jcas
    FSIterator<Annotation> iter = jcas.getAnnotationIndex(Gene.type).iterator();
    while (iter.hasNext()) {
      Gene g = (Gene) iter.next();
      // Span objects save the start and end point of a gene.
      Span oriSpan = new Span(g.getBegin(), g.getEnd());
      // The method ignoreSpace calculate the span of the gene without white spaces.
      Span newSpan = oriSpan.ignoreSpace(text);
      String s = g.getId() + "|" + newSpan.getStart() + " " + newSpan.getEnd() + "|" + g.getName();
      try {
        bw.write(s);
        bw.newLine();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * This method close the file to release memory.
   */
  @Override
  public void destroy() {
    super.destroy();
    try {
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
