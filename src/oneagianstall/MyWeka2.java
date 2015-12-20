/*
 * MyWekaClass.java
 * 
 * Created on 2-Nov-2007, 4:02:04 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
/**
 *
 * @author Shary
 */
package oneagianstall;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

import weka.classifiers.meta.Vote;
//import weka.classifiers.misc.monotone.EnumerationIterator;
import weka.classifiers.trees.RandomForest;
import weka.core.AttributeStats;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.SelectedTag;
import weka.core.Tag;
import weka.core.converters.ConverterUtils.DataSink;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;


public class MyWeka2 {
   private Instances test;
   private Instances data;
   private String dataPathName;
   private TreeMap<Double,Double> []bestCaseCodeReviewVsExecutions;//= new TreeMap<Double, Double>();
   private TreeMap<Double,Double> []worstCaseCodeReviewVsExecutions;//= new TreeMap<Double, Double>();
  private double totalFunction;
   private int avgTrackerCounter;
  private Integer totalNumberofDatabases;
 public boolean ISSETONLYBESTCASE=false;
  public ArrayList<Double> repTP;
  private ConfusionTable confusionTable=null;
  /**
   * creates an array for maximum repetitions
   * @param reps 
   */
  public void initializeRepetitionsArray(){
      repTP= new ArrayList<Double>();
     
  }
  
  /**
   * 
   * @return 
   */
  public ConfusionTable getConfusionTable(){
      return this.confusionTable;
  }
  /**
   * 
   * @param pathName
   * @param testPathName
   * @throws FileNotFoundException
   * @throws IOException
   * @throws Exception 
   */
   public void LoadArffFile(String pathName,String testPathName) throws FileNotFoundException, IOException, Exception
    {
        this.dataPathName=pathName;
        data =new Instances(new BufferedReader( new FileReader(pathName)));
       
        String[] options = new String[2];
        options[0] = "-R";                                    // "range"
              options[1]=Integer.toString(data.attribute("PMR#").index()+1)+","+Integer.toString(data.attribute("compIntensity").index()+1)+
                   ","+Integer.toString(data.attribute("functionIntensity").index()+1); 
        //options[2]=Integer.toString(data.attribute("compIntensity").index()+1);            
        //options[3]=Integer.toString(data.attribute("functionIntensity").index()+1); 
        Remove remove = new Remove();                         // new instance of filter
        remove.setOptions(options) ;                           // set options
        remove.setInputFormat(data);                          // inform filter about dataset AFTER setting options
        data = Filter.useFilter(data, remove);
        data.setClassIndex(data.numAttributes()-1);//*****  Class Index -- set it for test also
        
        test =new Instances(new BufferedReader( new FileReader(testPathName)));
        options[0] = "-R";                                    // "range"
//        options[1] = "1,13192-13194,13196-3199,13201";                                     // first attribute
          options[1]=Integer.toString(test.attribute("PMR#").index()+1)+","+Integer.toString(test.attribute("compIntensity").index()+1)+
                   ","+Integer.toString(test.attribute("functionIntensity").index()+1); 
        Remove removeTest = new Remove();                         // new instance of filter
        removeTest.setOptions(options);
        removeTest.setInputFormat(test);
        test = Filter.useFilter(test, removeTest);
        test.setClassIndex(test.numAttributes()-1);
        
    }
   
   public void BuildClassifier() throws  Exception
    {
        //String[] options=new String[1];
        //options[0]="-U";
       
        
        //objNav.buildClassifier(data);
        //NaiveBayesSimple objNav= new NaiveBayesSimple();
        //objNav.buildClassifier(data);
        // J48 objNav= new J48() ;
        // objNav.setOptions(options);
       // objNav.buildClassifier(data);
     
     /* Instance insNew=new Instance(5);
   insNew.setDataset(data.firstInstance().dataset());    
      insNew.setValue(0, "sunny");
      insNew.setValue(1, "cool");
      insNew.setValue(2, "high");
      insNew.setValue(3, "TRUE");*/
    //  insNew.setValue(4, "no");
    
       
       // for (int k=0;k<1;k++)
        //{
           
            //System.out.println(data.instance(k).dataset().toString());
            System.out.println("Data Number classes "+  data.numClasses()+"   Num attrbutes" + data.numAttributes())  ; 
            System.out.println("*********"+data.instance(0).toString());         
           // System.out.println("*********"+data.instance(21).toString());
            //double [] dist=objNav.distributionForInstance(insNew);
            
            
          System.out.println("**********"+data.attribute(data.classIndex())+"************");
          System.out.println("*******Component Names and Probabilities******");
            
         

        /*   System.out.println("  Predicted   Probability    Actual ");
           for (Integer tstCnt=0;tstCnt<test.numInstances();tstCnt++){
                    double [] dist=objNav.distributionForInstance(test.instance(tstCnt));
                    double maxDist=0;String attValue="";int i=0;
                    for ( Enumeration en=data.attribute(data.classIndex()).enumerateValues();en.hasMoreElements();i++){
                        if (dist[i]>maxDist){
                            maxDist=dist[i];
                            attValue=en.nextElement().toString();
                        }
                        else
                            en.nextElement();
                    //    System.out.print(i+"-->"+en.nextElement()+ " "+dist[i]);
                    }
                     
                    
                    System.out.println(" "+attValue+ " "+maxDist+" "+
                            test.instance(tstCnt).stringValue(test.classIndex()));
            }
           
            */
            
           // test.add(data.instance(k));
       // }
          
          //System.out.println(objNav.graphType());
          //System.out.println(objNav.toSource("objNav"));
     //  System.out.println(objNav.toString());
        //weka.datagenerators.classifiers.classification =new weka.datagenerators.classifiers.classification.
//        weka.gui.SaveBuffer sav=new weka.gui.SaveBuffer();
       // , arg14
       // objNav.s
            
       
       
     // System.out.println("Test*********"+test.instance(2).toString());
     // System.out.println("Test**********"+test.attribute(test.classIndex())+"************");
      Instance ins;
     Double totalCorrect=0.0, totalInCorrect=0.0;
     // NaiveBayes objNav= new NaiveBayes();
      weka.classifiers.trees.J48 objNav= new J48();
    //weka.classifiers.functions.SMO objNav = new weka.classifiers.functions.SMO();
     // weka.classifiers.functions.MultilayerPerceptron objNav = new weka.classifiers.functions.MultilayerPerceptron();
     // weka.classifiers.functions.RBFNetwork rbf = new weka.classifiers.functions.RBFNetwork();
      
   //  objNav.buildClassifier
  //  weka.classifiers.functions.supportVector.PolyKernel kernel=new weka.classifiers.functions.supportVector.PolyKernel();
   
    //kernel.setExponent(1);
     //objNav.
   // objNav.setKernel(kernel);
   Random r=new Random();
       System.out.println("  Predicted   Probability    Actual "); 
   //for (int i=0;i<test.numInstances();i++){    
     for (int i=test.numInstances()-1;i>=0;i--){
         
         int ind=i;//;r.nextInt(57);
         //System.out.println("Index= "+ind);
         ins=test.instance(ind);
          
        //   System.out.println(test.instance(ind).stringValue(data.classIndex())+" "+test.instance(ind).stringValue(data.classIndex()+1));
         // System.out.println(data.instance(ind).stringValue(data.classIndex())+" "+data.instance(ind).stringValue(data.classIndex()-1));
       // System.out.println(data.numInstances());
         data.delete(ind);
        //System.out.println(data.numInstances());
         // System.out.println(ins.stringValue(data.classIndex()) +" "+ data.instance(ind).stringValue(data.classIndex()));
        //System.out.println(ins.classIndex()+ " "+ ins.classValue()+""+data.classIndex());
//         System.out.println(ins.stringValue(data.classIndex())+" "+ins.stringValue(data.classIndex()+1));
         objNav.buildClassifier(data);
         Evaluation eval=new Evaluation(data);
         
   //    eval.evaluateModel(objNav,test );
      
    
       //eval.crossValidateModel(objNav, data, 2, new Random());
       eval.evaluateModelOnce(objNav,ins );
       
        if (eval.correct()==1.00){
            totalCorrect++;
            System.out.println(ins.stringValue(data.classIndex()) +" "+ data.instance(ind).stringValue(data.classIndex()));  
     
          /* 
            //Find out the predicted hypotheses --class-- attribute value ********************************8
         double [] dist=objNav.distributionForInstance(ins);
                    double maxDist=0; String attValue="";int j=0;
                    
             for ( Enumeration en=data.attribute(data.classIndex()).enumerateValues();en.hasMoreElements();j++){
                        if (dist[j]>maxDist){
                            maxDist=dist[j];
                            attValue=en.nextElement().toString();
                        }
                        else
                            en.nextElement();
                    //    System.out.print(i+"-->"+en.nextElement()+ " "+dist[i]);
                    }
                     
                    
                    System.out.println(" "+attValue+ " "+maxDist+" "+ ins.stringValue(data.classIndex()));
    //   int testNum=test.numInstances();
     //  int dataNum=data.numInstances();
     //  double pcttest=(testNum/(testNum+dataNum));
     //  double pctData=(testNum/(testNum+dataNum));
       // System.out.println(eval.toSummaryString("\nResults\n=======\n", false));
        //System.out.println("correctly Classified Instances"+eval.correct()+" "+eval.pctCorrect()+"%" );
        //System.out.println("Incorrectly Classified Instances"+eval.incorrect()+" "+ eval.pctIncorrect()+"%");
      */
        }
        else{
            totalInCorrect++;
        }
       // System.out.println("Total Number of Test instances"+test.numInstances() + " " + pcttest+ "%");
      //  System.out.println("Total Number of Data instances"+data.numInstances());
        data.add(test.instance(ind));
                // .add(ins);
      }
        //System.out.println("Number of True Positive"+eval.numTruePositives(data.classIndex()));
        //System.out.println("Number of True Negative"+eval.numTrueNegatives(data.classIndex()));
        //System.out.println("Number of False Positive"+eval.numFalsePositives(data.classIndex()));
        //System.out.println("Number of False Negative"+eval.numFalseNegatives(data.classIndex()));
       // System.out.println(eval.toMatrixString("Confusion Matrix"));
    

      System.out.println("Total Correct"+totalCorrect + "  "+ (totalCorrect/(totalCorrect+totalInCorrect)));
      System.out.println("Total Incorrect"+totalInCorrect + "  "+ (totalInCorrect/(totalCorrect+totalInCorrect)));
        //System.out.println("True Negative Rate"+eval.trueNegativeRate(data.classIndex()));
        
      /* double [][] confMatrix= eval.confusionMatrix() ;
       for ( i=0;i<confMatrix.length;i++){
           for (int j=0;j<confMatrix[i].length;j++)
                System.out.print(confMatrix[i][j]+"  ");
       System.out.println();
       }*/
       

    //   
      //  System.out.println(eval.toSummaryString("Results",false));
    }
    public void BuildClassifierCrossValidate(String pathName,BufferedWriter outFile) throws  Exception
    {
         //{
         //System.out.println(data.instance(k).dataset().toString());
       // this.dataPathName=pathName;
        
        data =new Instances(new BufferedReader( new FileReader(pathName)));
       
        String[] options = new String[2];
        options[0] = "-R";              // "range"
        //String removeList= Integer.toString(data.attribute("PMR#").index()+1);
        options[1]=Integer.toString(data.attribute("PMR#").index()+1)+","+Integer.toString(data.attribute("compIntensity").index()+1)+
                   ","+Integer.toString(data.attribute("functionIntensity").index()+1); 
        //options[2]=Integer.toString(data.attribute("compIntensity").index()+1);            
        //options[3]=Integer.toString(data.attribute("functionIntensity").index()+1); 
        Remove remove = new Remove();                         // new instance of filter
        remove.setOptions(options) ;                           // set options
        remove.setInputFormat(data);                          // inform filter about dataset AFTER setting options
        data = Filter.useFilter(data, remove);
        data.setClassIndex(data.numAttributes()-1);//*****  Class Index -- set it for test also
       
           
        outFile.write("Data Number classes "+  data.numClasses()+"   Num attrbutes" + data.numAttributes()+" instances"+data.numInstances())  ; 
        outFile.newLine();
         //   System.out.println("*********"+data.instance(0).toString());         
           // System.out.println("*********"+data.instance(21).toString());
            //double [] dist=objNav.distributionForInstance(insNew);
            
            
        outFile.write("**********"+data.attribute(data.classIndex())+"************");
        outFile.newLine();
            
         

     // System.out.println("Test*********"+test.instance(2).toString());
     // System.out.println("Test**********"+test.attribute(test.classIndex())+"************");
  //    Instance ins;
  //   Double totalCorrect=0.0, totalInCorrect=0.0;
   
      // System.out.println("  Predicted   Probability    Actual "); 
      // for (int i=0;i<data.numInstances();i++){    
      //for (int i=data.numInstances()-1;i>=0;i--){
         //Random r=new Random();
       //  int ind=i;//r.nextInt(57);
        // System.out.println(ind);
         //ins=data.instance(ind);
         //data.delete(ind);
         //NaiveBayes objNav= new NaiveBayes();
          weka.classifiers.trees.J48 objNav= new J48();
         //data.setClassIndex(data.numAttributes()-3);
         //objNav.buildClassifier(data);
        //DirectEvaluation(objNav);
                 
          Evaluation eval=new Evaluation(data);
          //String []option=objNav.getOptions();
         eval.crossValidateModel(objNav, data, 3, new Random());
         // eval.crossValidateModel(objNav.getClass().toString(), data, 10,option, new Random());
     //  int testNum=test.numInstances();
     //  int dataNum=data.numInstances();
     //  double pcttest=(testNum/(testNum+dataNum));
     //  double pctData=(testNum/(testNum+dataNum));
        outFile.write(eval.toSummaryString("\nResults\n=======\n", false));
        outFile.newLine();
        outFile.flush();
        System.out.println("finished croos validation");
       // outFile.close();
       //System.out.println("correctly Classified Instances"+eval.correct()+" "+eval.pctCorrect()+"%" );
        //System.out.println("Incorrectly Classified Instances"+eval.incorrect()+" "+ eval.pctIncorrect()+"%");
        //System.out.println("Kappa Stats:"+eval.kappa());
       
          //  totalInCorrect++;
       // }
       // System.out.println("Total Number of Test instances"+test.numInstances() + " " + pcttest+ "%");
      //  System.out.println("Total Number of Data instances"+data.numInstances());
       // data.add(ins);
    //  }
        //System.out.println("Number of True Positive"+eval.numTruePositives(data.classIndex()));
        //System.out.println("Number of True Negative"+eval.numTrueNegatives(data.classIndex()));
        //System.out.println("Number of False Positive"+eval.numFalsePositives(data.classIndex()));
        //System.out.println("Number of False Negative"+eval.numFalseNegatives(data.classIndex()));
        //System.out.println(eval.toMatrixString("Confusion Matrix"));
    

     // System.out.println("Total Correct"+totalCorrect + "  "+ (totalCorrect/(totalCorrect+totalInCorrect)));
     // System.out.println("Total Incorrect"+totalInCorrect + "  "+ (totalInCorrect/(totalCorrect+totalInCorrect)));
        //System.out.println("True Negative Rate"+eval.trueNegativeRate(data.classIndex()));
        
      /* double [][] confMatrix= eval.confusionMatrix() ;
       for ( i=0;i<confMatrix.length;i++){
           for (int j=0;j<confMatrix[i].length;j++)
                System.out.print(confMatrix[i][j]+"  ");
       System.out.println();
       }*/
       

    }
    /**
     * Build Classifier via cross validation, but doesn't store prediction file
     * @param pathName
     * @throws java.lang.Exception
     */
     
     
    public void BuildClassifierCrossValidate(String pathName) throws  Exception
    {
        
        data =new Instances(new BufferedReader( new FileReader(pathName)));
       
        String[] options = new String[2];
        options[0] = "-R";              // "range"
        //String removeList= Integer.toString(data.attribute("PMR#").index()+1);
        //options[1]=Integer.toString(data.attribute("PMR#").index()+1)+","+Integer.toString(data.attribute("compIntensity").index()+1)+
          //         ","+Integer.toString(data.attribute("functionIntensity").index()+1); 
        
        options[1]=Integer.toString(data.attribute("PMR#").index()+1)+
                ","+  Integer.toString(data.attribute("compIntensity").index()+1)+
                   ","+Integer.toString(data.attribute("functionIntensity").index()+1); 
        
        Remove remove = new Remove();                         // new instance of filter
        remove.setOptions(options) ;                           // set options
        remove.setInputFormat(data);                          // inform filter about dataset AFTER setting options
        data = Filter.useFilter(data, remove);
        data.setClassIndex(data.numAttributes()-1);//*****  Class Index -- set it for test also
       
          
        System.out.println("Data Number classes "+  data.numClasses()+"   Num attrbutes" + data.numAttributes()+" instances"+data.numInstances())  ; 
        System.out.println();
         //   System.out.println("*********"+data.instance(0).toString());         
           // System.out.println("*********"+data.instance(21).toString());
            //double [] dist=objNav.distributionForInstance(insNew);
            
            
        System.out.println("**********"+data.attribute(data.classIndex())+"************");
        
            
         //NaiveBayes objNav= new NaiveBayes();
          weka.classifiers.trees.J48 objNav= new J48();
          objNav.setConfidenceFactor((float)0.01);
                 
          Evaluation eval=new Evaluation(data);
          //String []option=objNav.getOptions();
         eval.crossValidateModel(objNav, data, 10, new Random(1));
           
         BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\shary\\Traces\\Trace_Results\\wekains.arff"));
        
        
         FastVector predVect= eval.predictions();
         Enumeration en=  predVect.elements();  
         while (en.hasMoreElements()){
            writer.write(en.nextElement().toString());
            writer.newLine();
         }
          writer.flush();
          writer.close();
         
         
        System.out.println(eval.toSummaryString("\nResults\n=======\n", false));
        System.out.println("Attributes "+ data.numAttributes());
        System.out.println("Number of faults "+ data.numClasses());
        System.out.println(eval.toMatrixString("Confusion Matrix"));
    
         System.out.println("Finished cross validation");
       

    }

    
    
    
    
    /**
     * 
     * @param arffFile
     * @param totalFolds
     * @param foldNumber test set fold number
     * @param isInvert
     * @throws java.lang.Exception
     */
    
    public void createTestTrainData(String arffFile,int totalFolds, int foldNumber,boolean isInvert, String sourceDir) throws Exception{
        
    Random rand = new Random(1);
    Instances randData  =loadDataSets(arffFile)[0];
    
    randData.randomize(rand);
    if (randData.classAttribute().isNominal())
      randData.stratify(totalFolds);

    // perform cross-validation and add predictions
    Instances predictedData = null;
   // Evaluation eval = new Evaluation(randData);
   // int n=0;
    //for (int n = 0; n < folds; n++) {
    System.out.println("Generating Training and Testing data for"+arffFile);
          Instances train,test;
          
          if (isInvert==false){// assign 1 fold to test  rest to training
            train = randData.trainCV(totalFolds, foldNumber);
            test = randData.testCV(totalFolds, foldNumber);
          }
          else {// assign one fold to training, rest to testing
             test = randData.trainCV(totalFolds, foldNumber);
             train = randData.testCV(totalFolds, foldNumber);
          }
          
          
         // String subjectDir=arffFile.substring(0,arffFile.lastIndexOf(File.separator));
                DataSink.write(sourceDir+File.separator+"trainset.arff" , train);
               DataSink.write(sourceDir+File.separator +"testset.arff" , test);
                
                //DataSink.write(arffFile.replaceAll("[\\w]*[.]arff$", "testset.arff") , test);
      
    }
    /**
     * 
     * @param pathName
     * @param predictionFile
     * @param rankFile
     * @param databaseName
     * @param strLengthForRanking
     * @param strMultipleLengths
     * @param freqOrConf
     * @param tableName
     * @param result
     * @param resultFile
     * @throws java.lang.Exception
     */
    public void generate_1aginstall_files(String pathName,  String folderNameForPairWiseFiles, boolean isResampling) throws  Exception
    {
        data=loadDataSets(pathName)[0];
       // Instances tests = loadDataSets(testFile)[0];
         // check compatibility of test set
        // if (!data.equalHeaders(tests)) 
        //           throw new IllegalStateException("Test set not compatible!"); 
        // else
          //   System.out.println (" Train and test sets are comaptible");
        
        //tests=null;
//        data =new Instances(new BufferedReader( new FileReader(pathName)));
//       
//        String[] options = new String[2];
//        options[0] = "-R";              // "range"
//        //String removeList= Integer.toString(data.attribute("PMR#").index()+1);
//        //options[1]=Integer.toString(data.attribute("PMR#").index()+1)+","+Integer.toString(data.attribute("compIntensity").index()+1)+
//          //         ","+Integer.toString(data.attribute("functionIntensity").index()+1); 
//        
//        if (data.attribute("compIntensity")!=null){
//                options[1]=Integer.toString(data.attribute("compIntensity").index()+1)+
//                           ","+Integer.toString(data.attribute("functionIntensity").index()+1); 
//
//                Remove remove = new Remove();                         // new instance of filter
//                remove.setOptions(options) ;                           // set options
//                remove.setInputFormat(data);                          // inform filter about dataset AFTER setting options
//                data = Filter.useFilter(data, remove);
//              }
//        
//        data.setClassIndex(data.numAttributes()-1);//*****  Class Index -- set it for test also
//        
          
        for (int numClasses=0;numClasses<data.numClasses();numClasses++){
                
                  Instances tempData= new Instances(data);
           // Instances tempData=data;
                  String classVal=tempData.classAttribute().value(numClasses);
                  //System.out.println(tempData.attributeStats(tempData.classAttribute().index()));
                  Integer classInstances=tempData.attributeStats(tempData.classAttribute().index()).nominalCounts[numClasses]*1;
                  
                  String otherVal= "others";
                  System.out.println("Generating file for "+classVal);
                  
                      
                  //  }
                  if (!classVal.equalsIgnoreCase("others")) {
                      int otherCount=0;
                      ArrayList<Integer> idxToRem= new ArrayList<Integer>();
                        for (int numInstances=0;numInstances<tempData.numInstances();numInstances++){
                            
                                  if (!tempData.instance(numInstances).stringValue(tempData.classIndex()).equalsIgnoreCase(classVal)){   
                                           // if (otherCount<=classInstances){
                                                tempData.instance(numInstances).setClassValue(otherVal);
                                                otherCount++;
                                           // } else {
                                           //     System.out.println(  tempData.instance(numInstances).classValue());
                                                
                                                //System.out.println(tempData.instance(numInstances).stringValue(tempData.classIndex()));
                                          //      tempData.remove(numInstances);
                                              //  System.out.println(tempData.instance(numInstances).stringValue(tempData.classIndex()));
                                               // numInstances--;
                                                 
                                                //idxToRem.add(numInstances);
                     
                                          //  }
                                            
                                  }
                            
                           }
                          
                     
                      
                        if ( isResampling==true){
                            weka.filters.supervised.instance.Resample reSamp= new weka.filters.supervised.instance.Resample();
                            reSamp.setInputFormat(tempData);
                            String Fliteroptions="-B 1.0";
                		    reSamp.setOptions(weka.core.Utils.splitOptions(Fliteroptions));
                		    reSamp.setRandomSeed((int)System.currentTimeMillis());
                            tempData=weka.filters.supervised.instance.Resample.useFilter(tempData, reSamp);
                        }
                        //folderNameForPairWiseFiles += "\\" +classVal+ ".arff";
                       String fileName=folderNameForPairWiseFiles+File.separator +classVal.replace(":", "_")+ ".arff";
                       
                        DataSink.write(fileName, tempData);
                        
                      
                  }

          }

        //System.out.println("Data Number classes "+  data.numClasses()+"   Num attrbutes" + data.numAttributes()+" instances"+data.numInstances())  ; 
       // System.out.println();
//voting();
    }
    
    public Instances[] loadDataSets(String folderPath) throws FileNotFoundException, IOException, Exception{
       
        File folders = new File (folderPath);
        File  f[]= folders.listFiles();
        Instances []dataSets=null;
        int fileCount;
        
        if (f!= null){ // if it is a folder
             dataSets=new  Instances[f.length];
             fileCount=f.length;
        }
        else // if it is not a folder
        {
          dataSets=new  Instances[1];
           fileCount=1;
           f= new File[1];
           f[0]=folders;
        
        }
        
        for (int fCnt=0;fCnt<fileCount;fCnt++){
                    // System.out.println("Loading data for the file"+f[fCnt].getPath());
                     dataSets[fCnt] =new Instances(new BufferedReader(new FileReader(f[fCnt].getPath())));

                    String[] options = new String[2];
                    options[0] = "-R";              // "range"
                    //String removeList= Integer.toString(data.attribute("PMR#").index()+1);
                    //options[1]=Integer.toString(data.attribute("PMR#").index()+1)+","+Integer.toString(data.attribute("compIntensity").index()+1)+
                      //         ","+Integer.toString(data.attribute("functionIntensity").index()+1); 

                    if (dataSets[fCnt].attribute("compIntensity")!=null){
                            options[1]=Integer.toString(dataSets[fCnt].attribute("PMR#").index()+1)+","+
                                    Integer.toString(dataSets[fCnt].attribute("compIntensity").index()+1)+
                                       ","+Integer.toString(dataSets[fCnt].attribute("functionIntensity").index()+1); 

                            Remove remove = new Remove();                         // new instance of filter
                            remove.setOptions(options) ;                           // set options
                            remove.setInputFormat(dataSets[fCnt]);                          // inform filter about dataset AFTER setting options
                            dataSets[fCnt] = Filter.useFilter(dataSets[fCnt], remove);
                          }

                    dataSets[fCnt].setClassIndex(dataSets[fCnt].numAttributes()-1);//*****  Class Index -- set it for test also
                    //data=dataSets[fCnt] ;
                    //costMatrix("C:\\shary\\Traces\\Trace_Results\\costmatrix.cost");
        }
       return dataSets; 
    }
    
    /**
     * 
     * @param folderPath
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     * @throws java.lang.Exception
     */
    
     protected Classifier[] loadClassifiers(String folderPath) throws FileNotFoundException, IOException, Exception{
       
        File folders = new File (folderPath);
        File  f[]= folders.listFiles();
        ///Instances []dataSets=null;
        Classifier[] cls=null;
        int fileCount;
        
        if (f!= null){ // if it is a folder
            //Classifier cls = (Classifier) weka.core.SerializationHelper.read("/some/where/j48.model");
             //dataSets=new  Instances[f.length];
             cls= new Classifier[f.length];
             fileCount=f.length;
        }
        else  if (folders.isFile()) // if it is a file
        {
           
          cls= new Classifier[1];
           //dataSets=new  Instances[1];
           fileCount=1;
           f= new File[1];
           f[0]=folders;
        
        }
        else 
           return null;
        
        for (int fCnt=0;fCnt<fileCount;fCnt++){
                       //f[fCnt].getName()
                     System.out.println("Loading Classifier"+f[fCnt].getPath());
                     cls[fCnt] = (Classifier) weka.core.SerializationHelper.read(f[fCnt].getPath());
                   
        }
       return cls; 
    }
/**
 * Voting: 1-against-all, 1-against-1
 * @param folderTrainData
 * @param folderTestData
 */
    

public void voting(String folderTrainData, String folderTestData,Double totalFunctions,
        String programName, String rankingFile) throws Exception{
       
System.out.println("Loading data");
avgTrackerCounter++; 
Instances[] datasets =loadDataSets(folderTrainData);
        
        
        // from somewhere 
// test whether datasets are all compatible 
// NB: you're not allowed to violate Weka's underlying assumption, 
// that all classifiers got trained on the same data. Hence the 
// structure of the datasets must be exactly the same. The data 
// itself can differ though. 
for (int i = 1; i < datasets.length; i++) { 
  if (!datasets[0].equalHeaders(datasets[i])) 
    throw new IllegalStateException("Training sets not compatible!"); 
} 

// train classifiers 
System.out.println("Building Classifier");
Classifier[] classifiers = new Classifier[datasets.length]; 
for (int i = 0; i < datasets.length; i++) { 
  classifiers[i] = new J48(); 
  
  classifiers[i].buildClassifier(datasets[i]); 
} 


// setup Vote 
System.out.println("Setting up vote");
Vote vote = new Vote(); 
//vote.getCombinationRule().getTags()[0].

//SelectedTag rule= new SelectedTag(Vote.MAJORITY_VOTING_RULE, Vote.TAGS_RULES);
   String []options=new String[2];
   //options[0]="-B";
   //options[1]="weka.classifiers.trees.J48";
   //options[2]="-D";
   //options[3]="False";
   options[0]="-R";
   options[1]= "AVG";//.PRODUCT_RULE;   //"MAX";
   
//vote.setCombinationRule(rule);
   
   vote.setOptions(options);
   vote.setClassifiers(classifiers); 
   

// output predictions on test set 
System.out.println("Testing data");
Instances test = loadDataSets(folderTestData)[0];

if (!datasets[0].equalHeaders(test)) 
  throw new IllegalStateException("Test set not compatible!"); 



int totCorrect=0;
Integer correctPositionCount[]= new Integer[test.numClasses()];

BufferedWriter outputStream= new BufferedWriter(new FileWriter(rankingFile,true));

for (int i = 0; i < test.numInstances(); i++) { 
    
       // String correctClass=test.instance(i).stringValue(test.classIndex());
       // System.out.println( "Correct class" +correctClass );

          TreeMap<Double,String> sortedVals= new TreeMap<Double,String>();
          for ( int clCnt=0;clCnt<classifiers.length ;clCnt++) {

                        double pred = classifiers[clCnt].classifyInstance(test.instance(i));  
                        double dist[]= classifiers[clCnt].distributionForInstance( test.instance(i));  

                       // String predictedClass=test.classAttribute().value((int) pred);
                     //   System.out.println("Predicted Class"+predictedClass);   
                        //System.out.println("Distribution");     
                        

                        for (int u=0;u <dist.length; u++)
                            if (!test.classAttribute().value(u).equalsIgnoreCase("others") && dist[u]>0){
                              //System.out.println(test.classAttribute().value(u)+ " : "+ dist[u]);
                                      String cls=test.classAttribute().value(u);
                                      
                                     
                                      
                                      String classVals= sortedVals.get( dist[u]);

                                          if (classVals==null){
                                              sortedVals.put(dist[u], cls);
                                              
                                             
                                          }
                                          else {
                                                 classVals=classVals+" , "+ cls;
                                                 sortedVals.put( dist[u],classVals);
                                          }

                            }


          }

//////////////////////////////
        String correctClass=test.instance(i).stringValue(test.classIndex());
        System.out.println( "Correct class" +correctClass );
        
        
       // outputStream.newLine();
        //outputStream.write(test.instance(i).stringValue(0));
       // MySqlData objSql=new MySqlData();
         // objSql.openConnection(GlobalVariables.host, Main.testDBforfuncStatements, GlobalVariables.userid, GlobalVariables.password);

           printCompareRanks(sortedVals, correctClass, outputStream,totalFunctions,avgTrackerCounter);
         // objSql.closeConnection();
                     
        
        }
       
        



        calculatePercentage(bestCaseCodeReviewVsExecutions[avgTrackerCounter]);
        calculatePercentage(worstCaseCodeReviewVsExecutions[avgTrackerCounter]);
        //totalFunction/=7;
        this.codeReviewExecutions("Multiple Classifier", "\\"+"multipleClassifierRanking.png", outputStream,
                programName,totalFunction,avgTrackerCounter,"");

outputStream.flush();
outputStream.close();

/*
for (int i = 0; i < test.numInstances(); i++) { 
  double pred = vote.classifyInstance(test.instance(i)); 
  //double dist[]= vote.distributionForInstance(test.instance(i));
 
System.out.println((i+1) + ". " + pred); 

String correctClass=test.instance(i).stringValue(test.classIndex());
System.out.print( "Correct class" +correctClass );

//test.instance(i).setClassValue(pred);
 String predictedClass=test.classAttribute().value((int) pred);
System.out.print("Predicted Class"+predictedClass);

if (correctClass.equalsIgnoreCase(predictedClass))
    totCorrect++;


//for (int k=0; k<dist.length; k++){
//      if (test.classAttribute().value(k).equalsIgnoreCase(correctClass)  )        {
//          if (correctPositionCount[k]== null)
//              correctPositionCount[k]=new Integer(1);
//          else
//              correctPositionCount[k]++;
    // }
          
  //}


} 


double correctPercent=((double)totCorrect/(double)test.numInstances() )*100;
System.out.println();
System.out.println("Correct : " + totCorrect +  " Total: "+ test.numInstances()); 
System.out.println("Total Correct "+correctPercent);


  for (int k=0; k<correctPositionCount.length; k++){
               System.out.println("Number of functions: " + k + " Traces Reviewed " + correctPositionCount[k]);
               
  }
   // End of voting    
 */ 
}


public void cumulativeNonCumulative(String fileName, Integer numberofDatabases) throws IOException{
 totalNumberofDatabases=numberofDatabases;
  bestCaseCodeReviewVsExecutions= new TreeMap[numberofDatabases];
worstCaseCodeReviewVsExecutions= new TreeMap[numberofDatabases];        
for (Integer i=0;i<numberofDatabases;i++){
    bestCaseCodeReviewVsExecutions[i]=new TreeMap<Double,Double>();
    worstCaseCodeReviewVsExecutions[i]=new TreeMap<Double,Double>();
}
    
Integer []a= new Integer[4];
totalFunction=0;
avgTrackerCounter=-1;
BufferedWriter outputStream= new BufferedWriter(new FileWriter(fileName,false));
            outputStream.close();
            
}

/**
 * 
 * @param folderTrainData
 * @param folderTestData
 * @throws java.lang.Exception
 */

public void multipleClassifiers(String folderTrainData, String testFile,Double totalFunctions,
        String programName, String rankingFile, String modelFile,StringBuilder prediction,
        String chartFile, String execPerFile) throws Exception{
       

    
    
    
Classifier[] classifiers=null;
System.out.println("Loading Classifiers");
System.out.println("Don't forget training and test set compatibility is checked in one against all");
if (!modelFile.isEmpty()){
    System.out.println("Test set compatibility is not checked when loading classifiers directly");
    classifiers=loadClassifiers(modelFile);    
}
// output predictions on test set 
System.out.println("Loading Test Data");
Instances test = loadDataSets(testFile)[0];
///////////////////////////////
//Getting total classes and their names to initalizes confusion table
Integer totalClasses=test.classAttribute().numValues();
Integer totalRecords=test.numInstances();
Enumeration e= test.classAttribute().enumerateValues();
String [] classes= new String[totalClasses];
int cnt=0;
while (e.hasMoreElements()){
    classes[cnt++]=e.nextElement().toString();
}
confusionTable=new ConfusionTable(totalClasses, classes,totalRecords);
//////
///
/////
if (classifiers==null){
                    System.out.println("Loading data");
                    Instances[] datasets =loadDataSets(folderTrainData);

                    avgTrackerCounter++; 
                    totalFunction=totalFunction+ totalFunctions;   
                    //totalFunction=totalFunction/(double)avgTrackerCounter;  
                            // from somewhere 
                    // test whether datasets are all compatible 
                    // NB: you're not allowed to violate Weka's underlying assumption, 
                    // that all classifiers got trained on the same data. Hence the 
                    // structure of the datasets must be exactly the same. The data 
                    // itself can differ though. 
                    for (int i = 1; i < datasets.length; i++) { 
                      if (!datasets[0].equalHeaders(datasets[i])) 
                        throw new IllegalStateException("Training sets not compatible!"); 
                    } 

                    /// check compatibility of test set
                //    if (!datasets[0].equalHeaders(test)) 
                      //   throw new IllegalStateException("Test set not compatible!"); 

                    
                    // train classifiers 
                    System.out.println("Building Classifier");
                     classifiers = new Classifier[datasets.length]; 
                     
                     
                    for (int i = 0; i < datasets.length; i++) { 
                      //if (i%2==0)
                        //datasets.
                    //	classifiers[i] = new weka.classifiers.functions.MultilayerPerceptron();
                       // classifiers[i] = new weka.classifiers.trees.J48();
                         classifiers[i] = new weka.classifiers.bayes.NaiveBayes();
                    	classifiers[i] = new weka.classifiers.bayes.NaiveBayesMultinomial();
                        //((SMO)classifiers[i]).setFilterType(n)
                    	//classifiers[i] = new weka.classifiers.bayes.net.BayesNetGenerator();
                        //((MultilayerPerceptron)classifiers[i]).setHiddenLayers("0");
                        
                        
                        	//new J48(); 
                 //  classifiers[i]  = new CostSensitiveClassifier();
                  // ((CostSensitiveClassifier)classifiers[i]).setClassifier(new J48());
                   // CostMatrix cm = new CostMatrix(2);
                   // cm.initialize();
                   // cm.setElement(0,1,20);
                   // ((CostSensitiveClassifier)classifiers[i]).setCostMatrix(cm);

                        
                        
                        
                      //classifiers[i] = new RandomForest(); 
                      //else 
                       // classifiers[i] = new  MultilayerPerceptron();

                      classifiers[i].buildClassifier(datasets[i]); 
                      //classifiers[i].
                      
                    if (!modelFile.isEmpty())
                      weka.core.SerializationHelper.write(modelFile, classifiers[i]);
                          //BufferedWriter outputmodel= new BufferedWriter(new FileWriter( folderTrainData+"\\"+ classifiers[i].getClass().getName(),false));
                          //OutputStream os = new FileOutputStream(folderTrainData+"\\"+ classifiers[i].getClass().getName());
                          //ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
                          //objectOutputStream.writeObject(classifiers[i]);



                    } 


}
   

System.out.println("Testing data");
/////////////////////////////////////////////////////////////// testing

//MySqlData objSql=new MySqlData();
//objSql.openConnection(GlobalVariables.host, Main.testDBforfuncStatements, GlobalVariables.userid, GlobalVariables.password);


int totCorrect=0;
Integer correctPositionCount[]= new Integer[test.numClasses()];

BufferedWriter outputStream= new BufferedWriter(new FileWriter(rankingFile,false));

for (int i = 0; i < test.numInstances(); i++) { 
    
       // String correctClass=test.instance(i).stringValue(test.classIndex());
       // System.out.println( test.instance(i) );

          TreeMap<Double,String> sortedVals= new TreeMap<Double,String>();
          for ( int clCnt=0;clCnt<classifiers.length ;clCnt++) {
//System.out.println(clCnt);
                        double pred = classifiers[clCnt].classifyInstance(test.instance(i));  
                        double dist[]= classifiers[clCnt].distributionForInstance( test.instance(i));  

                      //  String predictedClass=test.classAttribute().value((int) pred);
                        //System.out.println("Predicted Class"+predictedClass);   
                        //System.out.println("Distribution");     
                        

                        for (int u=0;u <dist.length; u++)
                            if (!test.classAttribute().value(u).equalsIgnoreCase("others") && dist[u]>0){
                               // if ( dist[u]>0){
                              //System.out.println(test.classAttribute().value(u)+ " : "+ dist[u]);
                                      String cls=test.classAttribute().value(u)+" ";
                                      
                                     
                                      
                                      String classVals= sortedVals.get( dist[u]);

                                          if (classVals==null){
                                              sortedVals.put(dist[u], cls);
                                              
                                             
                                          }
                                          else {
                                                 classVals=classVals+" , "+ cls;
                                                 sortedVals.put( dist[u],classVals);
                                          }

                            }


          }

//////////////////////////////
        String correctClass=test.instance(i).stringValue(test.classIndex());
       // System.out.println( "Correct class" +correctClass );
           
        
       // outputStream.newLine();
        //outputStream.write(test.instance(i).stringValue(0));
        if (prediction==null)
           printCompareRanks(sortedVals, correctClass, outputStream,totalFunctions,avgTrackerCounter);
        else{
             // if only one trace prediction then store it and return
            //;// get the 
            for (Map.Entry<Double,String> s:sortedVals.descendingMap().entrySet()){
               // System.out.println(s.getKey() + " _________________ "+s.getValue());
                prediction.append(s.getValue().split(",")[0]); // get the first prediction 
                break;
            }
            
                     
            prediction.append(";"+ correctClass);
        }
       
        
}

if (prediction == null ){ // if only one prediction is needed we don't want graph
       // calculatePercentage(bestCaseCodeReviewVsExecutions[avgTrackerCounter]);
        //calculatePercentage(worstCaseCodeReviewVsExecutions[avgTrackerCounter]);
        //totalFunction/=7;
    //String chartFile=Main.PROGRAMDIR+"multipleClassifierRanking.png";
        this.codeReviewExecutions("", chartFile, outputStream,
                programName,totalFunction,avgTrackerCounter,execPerFile);
}
outputStream.flush();
outputStream.close();
//objSql.closeConnection();
}

/**
 *  Calculate function ranks
 * @param sortValue
 * @param correctClassVal
 * @param outputStream
 * @param totalFunctions
 * @param avgTracker
 * @throws IOException
 */
 public void printCompareRanks(TreeMap<Double,String> sortValue, String correctClassVal,
         BufferedWriter outputStream, double totalFunctions, Integer avgTracker ) throws IOException, Exception{
           //double totalFunctions=23.0;
    
    
           outputStream.newLine();
            
          String []components= correctClassVal.split(":"); // if there are more than one compoonents/functions
          
         
            int rankCounter=0;// Rank counter: To find out the ranking of real faulty function
            Double worstCasefunctionCounter=0.0;
            Double bestCasefunctionCounter=0.0;
          boolean isFaulty=false;
       
          
        for (Map.Entry<Double,String> itEpRanks: sortValue.descendingMap().entrySet() )  {  
         //for (Map.Entry<Double,String> itEpRanks: sortValue.entrySet() )  {  
           
                 rankCounter++;
                 outputStream.write(itEpRanks.getKey().toString() + " = " + itEpRanks.getValue() + " ; ");// write to file rank and episode

                 int fLength=0;
                // if (Main.testDBforfuncStatements.isEmpty())
                  fLength=itEpRanks.getValue().split(",").length;
                

                 //functionCounter+=fLength;
                
                 //compare the episode with the faulty function/compnent name
                 isFaulty=false;
                 Integer foundCompSize=0;
                // if ( itEpRanks.getValue().contains("grepbuf"))
                     //{ String hell = ""; }
                String rankPrediction=itEpRanks.getValue();
                
                 for (int j=0; j<components.length;j++) // if there re multiple functions/components
                         // compare each

                     if ( rankPrediction.contains(components[j]+" ")){
                         // || components[j].contains(rankVal.trim())){
                  //   .matches("\\b"+components[j]+"\\b" )){

                         isFaulty=true;// break;     
                           // stop when u find the first faulty function/component
                      // if (!Main.testDBforfuncStatements.isEmpty())
                        //       foundCompSize=findFunctionSizes(components[j],objSql);
                       break;
                      }
                 // If a faulty function has been found then break out of the episode rank loop
                 if (isFaulty==true){
                     // for best case  
                   // if (isBestCase==false)
                      worstCasefunctionCounter+=fLength;
                      //if (Main.testDBforfuncStatements.isEmpty())
                      bestCasefunctionCounter++;
                      confusionTable.updateCorrectPrediction(rankCounter, correctClassVal);
                      //else
                        //  bestCasefunctionCounter+=foundCompSize;
                     
                     break;
                 }
                 else{
                     worstCasefunctionCounter+=fLength;
                     bestCasefunctionCounter+=fLength;
                      confusionTable.updateWrongPrediction(rankCounter, correctClassVal, rankPrediction);
                 }
                 
            } 
           
         // if faulty function is not found in the list
         if (isFaulty==false){
             worstCasefunctionCounter=totalFunctions;
             bestCasefunctionCounter=totalFunctions;
           //  confusionTable.updateWrongPrediction(rankCounter, correctClassVal, rankPrediction);
         }
             
            //functionCounter--;
            // Uncomment this and comment the following
            double worstCaseCodeReview=(worstCasefunctionCounter/totalFunctions) * 100;
            double bestCaseCodereview=(bestCasefunctionCounter/totalFunctions) * 100;
            // comment this and unomment the  upper one
           // double worstCaseCodeReview=(worstCasefunctionCounter);
            //double bestCaseCodereview=(bestCasefunctionCounter);
           // System.out.println(worstCaseCodeReview + " best: " + bestCaseCodereview);
            outputStream.newLine();
            outputStream.write( " Rank  " + rankCounter +  " : WC F Cnt :  " + (worstCasefunctionCounter) +
                    "BC F Cnt"+ (bestCasefunctionCounter) + " ERR Func:" + correctClassVal+
                    " Code-Review " + (worstCaseCodeReview) +" & " +  (bestCaseCodereview) + " total functions "+ totalFunctions);
            outputStream.newLine();
            outputStream.flush();
            
         // Mapping for number of functions required to examined vs total number of traces that required same number of function to examine
           //worstCaseCodeReview= Math.floor(worstCaseCodeReview);
           //bestCaseCodereview=Math.floor(bestCaseCodereview);
            Double numberofExecWorst= worstCaseCodeReviewVsExecutions[avgTracker].get(worstCaseCodeReview);
            
            if (numberofExecWorst==null)
                worstCaseCodeReviewVsExecutions[avgTracker].put(worstCaseCodeReview,1.0);
            else
                worstCaseCodeReviewVsExecutions[avgTracker].put(worstCaseCodeReview,(++numberofExecWorst));

           // System.out.println("worstCaseCodeReview "+ worstCaseCodeReview + "  " +numberofExecWorst);
            Double numberofExecBest= bestCaseCodeReviewVsExecutions[avgTracker].get(bestCaseCodereview);
           if (numberofExecBest==null)
                bestCaseCodeReviewVsExecutions[avgTracker].put(bestCaseCodereview,1.0);
            else
                bestCaseCodeReviewVsExecutions[avgTracker].put(bestCaseCodereview,(++numberofExecBest));
               // System.out.println("bestCaseCodereview"+ bestCaseCodereview+ " "+ numberofExecBest);
 }
 /**
  * Caluulate percentage of traces
  * @param codeReviewVsExecutions
  */
 public void calculatePercentage( TreeMap <Double, Double> codeReviewVsExecutions){
     Double cumulativeTraces=0.0; 
     for (Map.Entry<Double,Double> itfE: codeReviewVsExecutions.entrySet() ){                   
                                        cumulativeTraces+=itfE.getValue();
     }
     Double cumulativeExecution=0.0;
       for (Map.Entry<Double,Double> itfE: codeReviewVsExecutions.entrySet() ){                   
          //Double funcPer= (itfE.getValue()/cumulativeTraces) *100;
          cumulativeExecution= itfE.getValue();
       //   cumulativeExecution+= itfE.getValue();
          Double funcPer= (cumulativeExecution/cumulativeTraces) *100;
          itfE.setValue(funcPer);
       }
 }

  /**
  * 
  */
 public void codeReviewExecutions(String seriesName,String chartFile, BufferedWriter outputStream,String programName,
         Double totalFunctions, Integer avgTracker, String allExecutedPercentFile) throws Exception{
    //ChartXYSeries objChart=new ChartXYSeries("TOFU");
     TreeMap <Double, Double> codeReviewVsExecutions=new TreeMap<Double,Double>();
     // best case worse loop
   avgTracker++;
   ////// second file for execute percentage reuslts
   BufferedWriter outExecPer=null;
   if (!allExecutedPercentFile.isEmpty())
        outExecPer=new BufferedWriter(new FileWriter(allExecutedPercentFile, true));
//////
   //seriesName="Best case "+ seriesName;
     // seriesName="TOFU on Space";
//   objChart.initializeSeries(seriesName);
   //Double cumulativeExecutions=0.0;
// for (int trkCnt=0;trkCnt<avgTracker;trkCnt++)
//{
   int classCount=0;
   int totalCases=2;
   if (ISSETONLYBESTCASE==true)
      totalCases=1;
   
     for (int i=0;i<totalCases;i++){// 2 for best and worst case
         
         if (i==0){     
                         seriesName="Best case"+ seriesName;
                   //       objChart.initializeSeries(seriesName);
                         System.out.println("Best case");
                         //seriesName="TOFU on Space";
                       if (this.totalNumberofDatabases==1)
                              codeReviewVsExecutions=bestCaseCodeReviewVsExecutions[0];
                       else
                          calculateAvgGraphs (avgTracker,bestCaseCodeReviewVsExecutions,codeReviewVsExecutions);
                        // codeReviewVsExecutions= bestCaseCodeReviewVsExecutions[trkCnt];
                       //  calculateAvgGraphs2 (avgTracker,bestCaseCodeReviewVsExecutions,codeReviewVsExecutions);
     
              }
             
             else {
                        System.out.println("Worst case");
                        seriesName= seriesName.replace("Best", "Worst");
                 //       objChart.initializeSeries(seriesName);
                        
                        codeReviewVsExecutions.clear();
                       // codeReviewVsExecutions= worstCaseCodeReviewVsExecutions[trkCnt];
                      if (this.totalNumberofDatabases==1)
                        //
                          codeReviewVsExecutions=worstCaseCodeReviewVsExecutions[0];
                      else
                       calculateAvgGraphs(avgTracker,worstCaseCodeReviewVsExecutions,codeReviewVsExecutions);
                        // calculateAvgGraphs2 (avgTracker,worstCaseCodeReviewVsExecutions,codeReviewVsExecutions);
         }

          if (outExecPer!=null){
              //outExecPer.write("  <div class=\"ex\">");
              outExecPer.write("initializeSeries(\""+seriesName+"\") ;<br />");
              outExecPer.newLine();
          }
         
                      Double cumulativeExecutions=0.0;
                                Double cumulativeTotalTraces=0.0;
                                Double cumulativeTotalFunctions=0.0;
                                
                               // for (int j=0;j<avgTracker;j++){
                              //      cumulativeTotalFunctions+= (double) Main.functionCounts[j];
                               // }
                                   // cumulativeTotalFunctions/=avgTracker;
                               //Start: Get Total Traces
                               for (Map.Entry<Double,Double> itfE: codeReviewVsExecutions.entrySet() )                   
                                      cumulativeTotalTraces+=itfE.getValue();
//                                        
//                                
                               // End Get Total Traces


                               //objChart.initializeSeries(seriesName);
                               
                               for (Map.Entry<Double,Double> itfE: codeReviewVsExecutions.entrySet() ){                   
                                        Double funcPercentage=itfE.getKey();
                                        Double actualFunctions=(itfE.getKey()*(totalFunctions/avgTracker)) /100;
                                       //System.out.println("tot trace"+cumulativeTotalFunctions);
                                      // Double funcPercentage=(itfE.getKey()/cumulativeTotalFunctions)*100;
                                       //Double actualFunctions=itfE.getKey();//*(totalFunctions/avgTracker)) /100;
                                        
                                       cumulativeExecutions=cumulativeExecutions+itfE.getValue();
                                       Double executionPercentage=(cumulativeExecutions/cumulativeTotalTraces)*100;
                                   //   Double executionPercentage=  itfE.getValue();
                                        //System.out.println("Cumulatie Exec"+cumulativeExecutions ); 
                                         
                                         
                                         
                                         //System.out.println("Average Tracker"+ avgTracker);
               //                        objChart.AddSeriesData(funcPercentage,( executionPercentage));
                                       //  objChart.AddSeriesData(actualFunctions,( executionPercentage));
                                        outputStream.write( funcPercentage + " ("+ actualFunctions +") = " + executionPercentage );
                                      //----  System.out.println("TP on top "+actualFunctions + "= " + executionPercentage ); 
                                         if (i==0){// stores the Tp rate for all repetitions
                                             boolean flag=false;
                                             try {
                                                 this.repTP.get(classCount);
                                             }catch (IndexOutOfBoundsException ex){
                                                 this.repTP.add(executionPercentage);
                                                 flag=true;
                                             }
                                             finally{
                                                if (flag==false){
                                                    Double val=this.repTP.get(classCount);
                                                    this.repTP.set(classCount,(val+executionPercentage));
                                                }

                                                classCount++;
                                             }
                                         }
                                        
                                        outputStream.newLine();

                                        if (outExecPer!=null){
                                            outExecPer.write( " AddSeriesData("+funcPercentage +", " + executionPercentage+"); <br />");
                                            outExecPer.newLine();
                                         }
                                    }
                             outputStream.write("Total test records"+cumulativeTotalTraces);
                             System.out.println("Total test records"+cumulativeTotalTraces);
                             outputStream.newLine();
                             outputStream.flush();

                             if (outExecPer!=null){
                                 outExecPer.write("// Cumulative Traces"+cumulativeTotalTraces +"<br />");
                                 outExecPer.newLine();
                                 outExecPer.flush();
                                }
                            //objChart.saveSeries();
                           //  objChart.Draw(programName, "% of program to be examined", "Cumulative % of traces",chartFile);
             //objChart.saveSeries();
             
             if (outExecPer!=null){
                 outExecPer.write(" saveSeries(); <br />");
                 outExecPer.newLine();
                }
        }
   //}
//// Uncomment this line    
    // objChart.Draw(programName, "% of program to be examined", "Cumulative % of traces",chartFile);

     if (outExecPer!=null){
       //  outExecPer.write(" Draw(\""+programName+"\",\" % of program to be examined\", "
         //        + "\"Cumulative % of traces\",\""+chartFile.replace("\\","\\\\")+"\"); <br /> ");

         outExecPer.flush();
         outExecPer.close();
     }
   
 }
 /**
  * 
  * @param avgTracker
  * @param bestWorstCaseCodeReviewVsExecutions
  * @param codeReviewVsExecutions
  */
  public  void calculateAvgGraphs(Integer avgTracker ,TreeMap<Double,Double>[] bestWorstCaseCodeReviewVsExecutions
         ,TreeMap<Double,Double> codeReviewVsExecutions){
     
    
                        Integer largestSize=0;
                         java.util.Iterator  []it= new java.util.Iterator[avgTracker];
                          for (int trckCnt=0;trckCnt<avgTracker;trckCnt++){
                              java.util.Iterator<Entry<Double,Double>> its= bestWorstCaseCodeReviewVsExecutions[trckCnt].entrySet().iterator();
                              it[trckCnt]=its;
                              if (bestWorstCaseCodeReviewVsExecutions[trckCnt].size()>largestSize)
                                largestSize=bestWorstCaseCodeReviewVsExecutions[trckCnt].size();
                              
                             }
                         double hundrethVal=0.0;
                         Double val[]={0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
                         Double tmpVal[]={0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
                         //Double keys[]={6.0,12.0,15.0,20.0,25.0,30.0,35.0,40.0,45.0,50.0,55.0,60.0,65.0,
                           //        70.0,75.0,80.0,85.0,90.0,95.0,100.0};
                         
                         //Double keys[]={6.0,15.0,20.0,30.0,40.0,50.0,60.0,70.0,80.0,90.0,100.0};
                         Double keys[]={1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0,20.0,30.0,40.0,50.0,60.0,70.0,80.0,90.0,100.0};
                         Double howMany[]={0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
                                    0.0,0.0,0.0,0.0};
                        // for (int maps=0;maps<largestSize;maps++){
                             Integer key=0;
                             
                            
                              for (int trckCnt=0;trckCnt<avgTracker;trckCnt++){
                                  int howManTimes=0;
                                  //same 
                                 // boolean isSameTrack=false;
                                  while (it[trckCnt].hasNext()){
                                    //    isSameTrack=true;
                                       Entry<Double,Double> kv  =(Entry<Double,Double>)it[trckCnt].next();
                                    //  if (kv.getKey()!=100.0){
                                           howManTimes++;    
                                           // key=key+(kv.getKey());
                                           double curVal= (kv.getValue());
                                          
                                            if (kv.getKey()<=1.0)
                                            { System.out.print(curVal+" "); key=0; tmpVal[0]+=curVal;howMany[0]++;}
                                             if (kv.getKey()>1.0 && kv.getKey()<=2.0)
                                            { key=1;tmpVal[1]+=curVal;howMany[1]++;}
                                            else if (kv.getKey()>2.0 && kv.getKey()<=3.0)
                                           {   key=2; tmpVal[2]+=curVal;howMany[2]++;}
                                            else if (kv.getKey()>3.0 && kv.getKey()<=4.0)
                                            {key=3;tmpVal[3]+=curVal;howMany[3]++;}
                                            else if (kv.getKey()>4.0 && kv.getKey()<=5.0)
                                            {key=4;tmpVal[4]+=curVal;howMany[4]++;}
                                            else if (kv.getKey()>5.0 && kv.getKey()<=6.0)
                                            {key=5;tmpVal[5]+=curVal;howMany[5]++;}
                                           else if (kv.getKey()>6.0 && kv.getKey()<=7.0)
                                           {    key=6;tmpVal[6]+=curVal;howMany[6]++;}
                                           else if (kv.getKey()>7.0 && kv.getKey()<=8.0)
                                           {key=7;tmpVal[7]+=curVal;howMany[7]++;}
                                           else if (kv.getKey()>8.0 && kv.getKey()<=9.0)
                                           {key=8;tmpVal[8]+=curVal;howMany[8]++;}
                                           else if (kv.getKey()>9.0 && kv.getKey()<=10.0)
                                           {key=9;tmpVal[9]+=curVal;howMany[9]++;}
                                           else if (kv.getKey()>10.0 && kv.getKey()<=20.0)
                                           { key=10;tmpVal[10]+=curVal;howMany[10]++;}
                                           else if (kv.getKey()>20.0 && kv.getKey()<=30.0)
                                           { key=11;tmpVal[11]+=curVal;howMany[11]++;}
                                           else if (kv.getKey()>30.0 && kv.getKey()<=40.0)
                                           { key=12;tmpVal[12]+=curVal;howMany[12]++;}
                                           else if (kv.getKey()>40.0 && kv.getKey()<=50.0)
                                          { key=13;tmpVal[13]+=curVal;howMany[13]++;}
                                           else if (kv.getKey()>50.0 && kv.getKey()<=60.0)
                                           { key=14;tmpVal[14]+=curVal;howMany[14]++;}
                                           else if (kv.getKey()>60.0 && kv.getKey()<=70.0)
                                           { key=15;tmpVal[15]+=curVal;howMany[15]++;}
                                            else if (kv.getKey()>70.0 && kv.getKey()<=80.0)
                                           { key=16;tmpVal[16]+=curVal;howMany[16]++;}
                                            else if (kv.getKey()>80.0 && kv.getKey()<=90.0)
                                           { key=17;tmpVal[17]+=curVal;howMany[17]++;}
                                            else if (kv.getKey()>90.0 && kv.getKey()<=100.0)
                                           { key=18;tmpVal[18]+=curVal;howMany[18]++;}
                                           /* else if (kv.getKey()>95.0 && kv.getKey()<=100.0)
                                           { key=keys[19];val[19]+=curVal;howMany[19]++;}
                                          */ 
                                           
                               //            System.out.println (key +" val "+curVal); 
                                       //}
                                       //else
                                         //  hundrethVal+=kv.getValue();
                                       
                                  }
                                  double prevVal=0.0, nextVal=0.0;
                                  for (int i=0;i<val.length;i++){

                                    //  if (tmpVal[i+1]!=null )
                                      //    nextVal=tmpVal[i+1];

                                    //  if (tmpVal[i]==0.0){
                                         
                                      //       val[i]+=prevVal;
                                        
                                    //  }
                                     //  else{
                                          val[i]+=tmpVal[i];
                                      //    prevVal=tmpVal[i];
                                   //   }
                                        
                                        tmpVal[i]=0.0;
                                        /////////
                                      // prevVal=0.0;
                                  }
                               // for (int j=key;j<val.length;j++){
                                 //   val[j]+=100.0;
                                   // tmpVal[j]=0.0;
                                 /// }
                                  // key=0;
                              }
//                            if (key!=0){
//                                    System.out.println("how "+howManTimes);
//                                    key=key;///howManTimes;
//                                    val=val/howManTimes;
//                                   // System.out.println("key "+key+ "vsl "+val);
//                                    //if (key!=0.0)
//                                     codeReviewVsExecutions.put(key, val);
//                             }
                        //}
                         //double total=0.0;
                        // for (int i=0;i<val.length;i++)
                          //   total+=val[i];
                         
                        for (int k=0; k<val.length;k++) {
                           // if (howMany[k]>avgTracker)
                             // howMany[k]=(double)(avgTracker*1.0);
                           if (val[k]!=0){// && howMany[k]>0){
                             
                               double finalVal=val[k];//avgTracker; //howMany[k] ;
                               //if (finalVal>100.0)
                                 //  finalVal=100.0;
                               System.out.println(keys[k]+ " " + val[k] + " "+ howMany[k]);
                               codeReviewVsExecutions.put(keys[k], (finalVal));
                           }
                           //  codeReviewVsExecutions.put(keys[k], ((val[k]/7)));
                        }
 }
/**
 * 
 * @param avgTracker
 * @param bestWorstCaseCodeReviewVsExecutions
 * @param codeReviewVsExecutions
 */
 public  void calculateAvgGraphs2(Integer avgTracker ,TreeMap<Double,Double>[] bestWorstCaseCodeReviewVsExecutions
         ,TreeMap<Double,Double> codeReviewVsExecutions){
     
                        
                        Integer largestSize=0;
                         java.util.Iterator  []it= new java.util.Iterator[avgTracker];
                          for (int trckCnt=0;trckCnt<avgTracker;trckCnt++){
                              java.util.Iterator<Entry<Double,Double>> its= bestWorstCaseCodeReviewVsExecutions[trckCnt].entrySet().iterator();
                              it[trckCnt]=its;
                              if (bestWorstCaseCodeReviewVsExecutions[trckCnt].size()>largestSize)
                                largestSize=bestWorstCaseCodeReviewVsExecutions[trckCnt].size();
                              
                             }
                         
                       double hundrethVal=0.0;
                       double prevVal=0.0;
                         for (int maps=0;maps<largestSize;maps++){
                             Double key=0.0,val=0.0;
                             int howManTimes=0;
                            
                              for (int trckCnt=0;trckCnt<avgTracker;trckCnt++){
                                  //same 
                                 // boolean isSameTrack=false;
                                  if (it[trckCnt].hasNext()){
                                    //    isSameTrack=true;
                                       Entry<Double,Double> kv  =(Entry<Double,Double>)it[trckCnt].next();
                                     // if (kv.getKey()!=100.0){
                                           howManTimes++;    
                                             
                                           key=key+(kv.getKey());
                                           val+= (kv.getValue());
                                           
                                      /// }
                                      // else
                                        //   hundrethVal+=kv.getValue();
                                       
                                  }
                                  else {
                                    key=key+100;
                                    howManTimes++;
                                  }
                              }
                             
                            
                            if (key!=0.0){
                                    System.out.println("how "+howManTimes);
                                    key=key/  howManTimes;
                                    val=val/  howManTimes;

                                   // System.out.println("key "+key+ "vsl "+val);
                                   // if (key!=0.0)
                                    val=((val));
                                     codeReviewVsExecutions.put(key, val);
                                     prevVal=val;
                             }
                        }
//                         double total=0.0;
//                         for (int i=0;i<val.length;i++)
//                             total+=val[i];
//                         
//                        for (int k=0; k<val.length;k++) {
//                            if (howMany[k]>avgTracker)
//                              howMany[k]=(double)(avgTracker*1.0);
//                            if (val[k]!=0)
//                               codeReviewVsExecutions.put(keys[k], ((val[k]/howMany[k])));
//                        }
 }
//public void setMapValues(Map<T,T> m, T key,T val ){
//        String val= sortValue.get( itfE.getValue().variance);
//            
//              if (episode==null){
//                  sortValue.put( itfE.getValue().variance,itfE.getKey());
//              }
//              else {
//                     episode=episode+","+ itfE.getKey();
//                     sortValue.put( itfE.getValue().variance,episode);
//              }
//          
//}
/**
     * Cost matrrix
     * @throws java.io.IOException
     */
    public void costMatrix(String fileName) throws IOException{
        //weka.core.AttributeStats as=data.attributeStats(data.classIndex()-1).nominalCounts ;
        int []valueCounts=data.attributeStats(data.classIndex()).nominalCounts;
        int k=0;
       // Enumeration en= data.classAttribute().enumerateValues();
        // while (en.hasMoreElements()){
                    //System.out.println(en.nextElement()+" "+ valueCounts[k++]);
         //}
        BufferedWriter output =  new BufferedWriter(new FileWriter(
                fileName,false)); 
    
    output.write("% Rows Columns");
    output.newLine();
    output.write(valueCounts.length + " "+valueCounts.length );
    output.newLine();
    output.write("% Matrix Element");
    output.newLine();


   // outputBuildTime.write(result);
        // creating cost matrix
        System.out.println();
        for (k=0;k<valueCounts.length;k++){
            // C(L1/L2)=sqrt(m1/m2)
            //System.out.print("0");
            for (int i=0;i<k;i++){
               // System.out.println (valueCounts[i]+" "+valueCounts[k]);
                // first calculate cost according to above formula before the value of k, where k is iterating through all the class values
                double cost=  Math.sqrt((double)valueCounts[i]/(double)valueCounts[k]);
               output.write( " " +cost);
            }
            for (int j=k;j<valueCounts.length;j++){
                // first calculate cost according to above formula after (and including) the value of k, where k is iterating through all the class values
                if (j!=k){
                    double cost=  Math.sqrt((double)valueCounts[j]/(double)valueCounts[k]);
                    output.write( " " +cost);
                }
                else
                    output.write( " 0" );
            }
           output.newLine();
        }
       
        
       
       output.flush();
       output.close();
       System.out.println("Cost calculated") ;
        //        a= data.classAttribute();
        
        //for (int k=0;k<valueCounts.length;k++)
        
            //System.out.println (valueCounts[k]);
        //}
    }
    /**
     *  Direct Evaluation
     * @param objNav
     * @throws java.lang.Exception
     */
   public void DirectEvaluation(Classifier objNav) throws Exception{
       String []options=new String[9];
        options[0]="-t";
        options[1]=this.dataPathName;
        options[2]="-c";
        options[3]= Integer.toString(data.classIndex()+11);
        options[4]="-no-cv";
     //   options[7]="-preserve-order";
       // options[8]="-d";
        //options[9]="C:\\shary\\Traces\\Trace_Results\\dis.xml";
        options[5]="-split-percentage";
        options[6]="80";
    //  
        options[7]="-o";
        options[8]="-v";
        //options[8]="-i";
       // options[8]="-p";
      //  options[9]="0";//Integer.toString(data.numAttributes()-1);
       // options[14]="-r";
      //  options[10]="-distribution";
      System.out.println(Evaluation.evaluateModel(objNav, options)); 
   }



public String singleClassifier(String trainFile, String testFile, Double totalFunctions,
        String programName, String rankingFile, String modelFile, StringBuilder prediction,
        String chartFile, String execPerFile, String costFolder) throws Exception
    { return "";// this is added for overriding iin derived calss }
    }

}