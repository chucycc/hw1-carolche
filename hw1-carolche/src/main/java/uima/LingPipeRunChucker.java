package uima;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.util.AbstractExternalizable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class LingPipeRunChucker {
  File modelFile;
  Chunker chunker = null;
  Map<Integer, Integer> result = new HashMap<Integer, Integer>();
  
  public LingPipeRunChucker(String filePath) {
    modelFile = new File(filePath);
    try {
      chunker = (Chunker) AbstractExternalizable.readObject(modelFile);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public Map<Integer, Integer> getResult(String input) {
    Chunking chunking = chunker.chunk(input);
    Set<Chunk> chunkResult = chunking.chunkSet();
    Iterator<Chunk> chunkIter = chunkResult.iterator();
    
    while (chunkIter.hasNext()) {
      Chunk c = (Chunk) chunkIter.next();
      if (c.type().equals("GENE")) {
        result.put(c.start(), c.end());
      }
    }
    return result;
  }
  
}
