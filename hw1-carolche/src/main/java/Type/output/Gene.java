

/* First created by JCasGen Thu Sep 25 21:30:55 EDT 2014 */
package Type.output;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** The gene product mentions in text.
 * Updated by JCasGen Thu Sep 25 21:43:20 EDT 2014
 * XML source: /home/carol/git/hw1-carolche/hw1-carolche/src/main/resources/descriptor/collectionReaderDescriptor.xml
 * @generated */
public class Gene extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Gene.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Gene() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Gene(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Gene(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Gene(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: id

  /** getter for id - gets The sentence identifier of input data. Different genes from the same sentence will have the same identifier.
   * @generated
   * @return value of the feature 
   */
  public String getId() {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "Type.output.Gene");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Gene_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets The sentence identifier of input data. Different genes from the same sentence will have the same identifier. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(String v) {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "Type.output.Gene");
    jcasType.ll_cas.ll_setStringValue(addr, ((Gene_Type)jcasType).casFeatCode_id, v);}    
   
    
  //*--------------*
  //* Feature: name

  /** getter for name - gets The identified gene names from the input sentences.
   * @generated
   * @return value of the feature 
   */
  public String getName() {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "Type.output.Gene");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Gene_Type)jcasType).casFeatCode_name);}
    
  /** setter for name - sets The identified gene names from the input sentences. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setName(String v) {
    if (Gene_Type.featOkTst && ((Gene_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "Type.output.Gene");
    jcasType.ll_cas.ll_setStringValue(addr, ((Gene_Type)jcasType).casFeatCode_name, v);}    
  }

    