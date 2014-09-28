package uima;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.util.AbstractExternalizable;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * LingPipeRunChucker reads the given model to do name entity recognition.
 * 
 * @author Carol Cheng
 * 
 */
public class LingPipeRunChucker {
  File modelFile;

  // This field records which type of Chunker will be used.
  int whichChunker;

  Chunker chunker = null;

  ConfidenceChunker cchunker = null;

  // This field is used by ConfidenceChunker.
  // It decides how many of n-best chunks will be returned.
  static final int MAX_N_BEST_CHUNKS = 8;

  /**
   * The constructor uses the model file taken from the file path to build the specified Chunker.
   * 
   * @param filePath
   *          The file path of the pre-trained model file. Here it uses the model file provided by
   *          LingPipe.
   * @param whichChunker
   *          The parameter specifies which type of Chunker will be used. The two Chunker are used
   *          for: 1) First-Best Named Entity Chunking 2) Confidence Named Entity Chunking
   */
  public LingPipeRunChucker(URI filePath, int whichChunker) {
    modelFile = new File(filePath);
    this.whichChunker = whichChunker;
    if (whichChunker == 1) {
      try {
        this.chunker = (Chunker) AbstractExternalizable.readObject(modelFile);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      try {
        cchunker = (ConfidenceChunker) AbstractExternalizable.readObject(modelFile);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * This method uses model to find out gene names and save the start and end points of the gene.
   * 
   * @param input
   *          string of input sentences
   * @return Map<Integer, Integer> start and end point of the gene annotation
   */
  public Map<Integer, Integer> getResult(String input) {
    // The map is used to save the start and end point of the identified gene.
    Map<Integer, Integer> result = new HashMap<Integer, Integer>();
    if (whichChunker == 1) {
      // First-Best Named Entity Chunking
      Chunking chunking = chunker.chunk(input);
      Set<Chunk> chunkResult = chunking.chunkSet();
      Iterator<Chunk> chunkIter = chunkResult.iterator();

      while (chunkIter.hasNext()) {
        Chunk c = (Chunk) chunkIter.next();
        if (c.type().equals("GENE")) {
          result.put(c.start(), c.end());
        }
      }
    } else {
      // Confidence Named Entity Chunking
      char[] cs = input.toCharArray();
      Iterator<Chunk> it = cchunker.nBestChunks(cs, 0, cs.length, MAX_N_BEST_CHUNKS);
      while (it.hasNext()) {
        Chunk chunk = it.next();
        // Compute the confidence by exponentiating the log (base 2) value
        double conf = Math.pow(2.0, chunk.score());
        int start = chunk.start();
        int end = chunk.end();
        // When the chunk's confidence is larger than 0.65 and the chunk is not a single letter,
        // put the chunk into result map.
        if (conf > 0.65 && end - start > 1) {
          result.put(start, end);
        }
      }
    }
    return result;
  }

}
