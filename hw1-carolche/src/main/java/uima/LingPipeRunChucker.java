package uima;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.util.AbstractExternalizable;

import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;

public class LingPipeRunChucker {
  File modelFile = new File("src/main/resources/ne-en-bio-genetag.HmmChunker");
  
  public LingPipeRunChucker() {
    
  }

}
