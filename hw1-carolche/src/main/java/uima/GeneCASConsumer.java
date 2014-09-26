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

import Type.output.Gene;

public class GeneCASConsumer extends CasConsumer_ImplBase {
  /**
   * Name of configuration parameter that must be set to the path of a directory into which the
   * output files will be written.
   */
  public static final String PARAM_OUTPUTDIR = "Output";
  private File fOutput;
  private FileWriter fw;
  private BufferedWriter bw;
   
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
  
  @Override
  public void processCas(CAS aCAS) throws ResourceProcessException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }
    FSIterator<Annotation> iter = jcas.getAnnotationIndex(Gene.type).iterator();
    while (iter.hasNext()) {
      Gene g = (Gene) iter.next();
      String s = g.getId() + "|" + g.getBegin() +  " " + g.getEnd() + "|" + g.getName();
      try {
        bw.write(s);
        bw.newLine();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
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
