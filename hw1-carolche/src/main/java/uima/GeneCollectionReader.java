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
 * A simple collection reader that reads documents from a directory in the filesystem. It can be
 * configured with the following parameters:
 * <ul>
 * <li><code>InputDirectory</code> - path to directory containing files</li>
 * </ul>
 * 
 * 
 */
public class GeneCollectionReader extends CollectionReader_ImplBase {
  /**
   * Name of configuration parameter that must be set to the path of a directory containing input
   * files.
   */
  public static final String PARAM_INPUTDIR = "InputFile";
  private File file;
  private ArrayList<String> sentences = new ArrayList<String>();
  private int currentLine;
  
  public void initialize() throws ResourceInitializationException {
    file = new File(((String) getConfigParameterValue(PARAM_INPUTDIR)).trim());
    
    try {
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      String line;
      while((line = br.readLine())!=null){
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

    // Also store location of source document in CAS. This information is critical
    // if CAS Consumers will need to know where the original document contents are located.
    // For example, the Semantic Search CAS Indexer writes this information into the
    // search index that it creates, which allows applications that use the search index to
    // locate the documents that satisfy their semantic queries.
    Sentence sentence = new Sentence(jcas);
    String[] words = temp.split(" ");
    int idLength = words[0].length();
    sentence.setId(words[0]);
    sentence.setText(temp.substring(idLength+1, temp.length()));
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
