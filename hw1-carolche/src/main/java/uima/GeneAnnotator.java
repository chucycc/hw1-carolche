package uima;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;

import Type.output.Gene;

/**
 * GeneAnnotator is the analysis engine of this framework. It takes sentences and uses LingPipe and
 * pre-trained model file to build the annotations of genes. The annotated Gene type objects were
 * the output of this annotator.
 * 
 * @author Carol Cheng
 * 
 */
public class GeneAnnotator extends JCasAnnotator_ImplBase {
  LingPipeRunChucker lingPipe = null;

  URI uri;

  /**
   * The method initializes the annotator by get the URI of model file needed by LingPipe.
   * 
   * @throws ResourceInitializationException
   * 
   */
  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    // Read file path of trained model file from resource.
    try {
      uri = getContext().getResourceURI("modelFile");
      System.out.println(uri);
    } catch (ResourceAccessException e) {
      e.printStackTrace();
    }
    // Use the model file to build a LingPipeRunChucker.
    // See LinPipeRunChucker.java.
    lingPipe = new LingPipeRunChucker(uri, 2);
    System.out.println("GeneAnnotator starts processing...");
  }

  /**
   * The method processes the sentences and build gene annotations as output .
   * 
   * @throws AnalysisEngineProcessException
   * 
   */
  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    // Get the sentences from CAS which is processed by collection reader.
    String s = jcas.getDocumentText();
    String text = s.substring(15);
    // Use LinePipe to figure out the start and end points of genes in the given sentence
    // and use map to restore the results.
    Map<Integer, Integer> result = lingPipe.getResult(text);
    Iterator<Map.Entry<Integer, Integer>> Iter = result.entrySet().iterator();
    while (Iter.hasNext()) {
      Map.Entry<Integer, Integer> span = Iter.next();
      Gene newGene = new Gene(jcas);
      int start = span.getKey();
      int end = span.getValue();
      newGene.setId(s.substring(0, 14));
      newGene.setBegin(start);
      newGene.setEnd(end);
      newGene.setName(text.substring(start, end));
      newGene.addToIndexes();
    }
  }

}
