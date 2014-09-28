package uima;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import Type.input.Sentence;

/**
 * A simple collection reader that reads documents from a file. It can be configured with the
 * following parameters:
 * <ul>
 * <li><code>InputFile</code> - path to the input file</li>
 * </ul>
 * 
 * 
 */
public class GeneCollectionReader extends CollectionReader_ImplBase {
  public static final String PARAM_INPUTDIR = "InputFile";

  private File file;

  private ArrayList<String> sentences = new ArrayList<String>();

  private int currentLine;

  public void initialize() throws ResourceInitializationException {
    file = new File(((String) getConfigParameterValue(PARAM_INPUTDIR)).trim());
    // Read input sentences line by line and save them to a arrayList.
    try {
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      String line;
      while ((line = br.readLine()) != null) {
        sentences.add(line);
      }
      fr.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    currentLine = 0;
  }

  @Override
  public void getNext(CAS aCAS) throws IOException, CollectionException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new CollectionException(e);
    }

    String temp = sentences.get(currentLine++);
    // put document in CAS
    jcas.setDocumentText(temp);
    // Parse the sentence and build Sentence type object.
    Sentence sentence = new Sentence(jcas);
    String[] words = temp.split(" ");
    int idLength = words[0].length();
    sentence.setId(words[0]);
    sentence.setText(temp.substring(idLength + 1));
    sentence.addToIndexes();
  }

  @Override
  public void close() throws IOException {
  }

  @Override
  public Progress[] getProgress() {
    return new Progress[] { new ProgressImpl(currentLine, sentences.size(), Progress.ENTITIES) };
  }

  @Override
  public boolean hasNext() throws IOException, CollectionException {
    return currentLine < sentences.size();
  }

}
