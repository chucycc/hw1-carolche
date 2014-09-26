package uima;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import Type.input.Sentence;
import Type.output.Gene;
import Type.output.Span;



public class GeneAnnotator extends JCasAnnotator_ImplBase {
  LingPipeRunChucker lingPipe = null;

  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    String modelFile = (String) getContext().getConfigParameterValue("modelFile");
    lingPipe = new LingPipeRunChucker(modelFile);
    Iterator iter = jcas.getAnnotationIndex(Sentence.type).iterator();
    while (iter.hasNext()) {
      Sentence s = (Sentence) iter.next();
      String text = s.getText();
      Map<Integer, Integer> result = lingPipe.getResult(text);
      Iterator<Map.Entry<Integer, Integer>> Iter = result.entrySet().iterator();
      while (Iter.hasNext()) {
        Map.Entry<Integer, Integer> span = Iter.next();
        Gene newGene = new Gene(jcas);
        Span oriSpan = new Span(span.getKey(), span.getValue());
        Span newSpan = oriSpan.ignoreSpace(text);
        newGene.setId(s.getId());
        newGene.setBegin(newSpan.getStart());
        newGene.setEnd(newSpan.getEnd());
        newGene.setName(text.substring(span.getKey(), span.getValue()));
        newGene.addToIndexes();
      }
    }
  }
  
  

}
