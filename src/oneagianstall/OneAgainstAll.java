/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oneagianstall;




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;



    ////////////////////
    
  
    


/*
 * Main.java
 * 
 * Created on 13-Oct-2007, 6:39:51 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//import com.mysql.jdbc.ResultSet;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;

//import java.util.Collection;
//import java.util.Comparator;
//import java.util.Dictionary;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Scanner;
//import java.util.Set;
//import java.util.SortedMap;
//import java.util.TreeMap;

/**
 *
 * @author Shary
 */
public class OneAgainstAll {
  //  public static final String PROGRAMDIR="I:\\Users\\shary\\software_infrastructure_repository\\trace_results\\";

    

static String tmpVer="v1";



    public static void main(String[] args){
        BufferedWriter outputBuildTime=null;
        long startTime=0;
        OneAgainstAll m= new OneAgainstAll();
       
        if (args.length < 4){
            System.out. println ("The correct format to run the code is: \n"
                    + "java -jar oneagainstall \"path to file\" \"num of repetitions\" \"num of stratas\" \"no of classes\"");
            System.out.println("java -jar oneagianstall \"\\mydr\\smh.arff\" 10 10 15");
            System.exit(-1);
        }
            
        String inputFile=args[0];
        Integer maxRepetitions=1;
        Integer numOfStratas=2;
        Integer totalClassesInput=15;
        Boolean isDirichletSampling=false;
        String finalOutputFile="finalOutputVA.txt";
        try {
        maxRepetitions=Integer.parseInt(args[1]);
        numOfStratas=Integer.parseInt(args[2]);
        totalClassesInput=Integer.parseInt(args[3]);
        if (args[4].equalsIgnoreCase("t"))
            isDirichletSampling=true;
        
        }catch (NumberFormatException ex){
            System.out.println ("Please only type integer numbers for repetitions and stratas");
        }
       
        
            
        
       
       //String inputFile="/home/shary/sharywork/ryerson/experiments/arff/MDSFinal.arff";
       
              

      // boolean isMergeTwoDataSets=false;
       boolean isResampling=false;
       //Integer singleReleaseNumParts=4;
       String datasetName="smh";      
       MyWeka2 objWekaGraphEval=null;
    
       try{
       
       String [] train_Test_Arff= {"",""};
       String progDir=inputFile.substring(0,inputFile.lastIndexOf(File.separator));
       objWekaGraphEval=new MyWeka2();
       objWekaGraphEval.ISSETONLYBESTCASE=true;
       objWekaGraphEval.initializeRepetitionsArray();
       String sourceDir=progDir+File.separator+"pairwise_"+datasetName ;
       finalOutputFile=sourceDir+File.separator+finalOutputFile;
                        
       
       Double []sensitivity=new Double[totalClassesInput];//totalclassess is equal to total ranks
       Double []specificity=new Double[totalClassesInput];
       Double []PCCC=new Double[totalClassesInput];
       Double []csmfAccuracy=new Double[totalClassesInput];
       Map<String,Double> sortedClassesForRank1=new TreeMap();
       
       Arrays.fill(sensitivity,0.0);
       Arrays.fill(specificity,0.0);
       Arrays.fill(PCCC,0.0);
       Arrays.fill(csmfAccuracy,0.0);
       
   
       for (int repCount=0; repCount<maxRepetitions; repCount++){
                        
                        File f=new File(sourceDir);
                        //FileUtils.deleteDirectory(f);
                        m.recursiveDelete(f);    
           
                           //DirichletSampling dSampl= new DirichletSampling();
                          // inputFile=dSampl.sampleDataByDirichlet(inputFile);
                           
                         //select a partition (one of the folds) for testing randomly
                        
                        if (isDirichletSampling==false){
                            int testFold=(repCount%numOfStratas);
                            objWekaGraphEval.createTestTrainData(inputFile, numOfStratas, testFold, false,sourceDir);
                        }else{
                            numOfStratas=4; // divide data into four parts, keep 3 (75%) for training and reamining (25%)for testing 
                            int testFold=(repCount%numOfStratas);
                            objWekaGraphEval.createTestTrainData(inputFile, numOfStratas, testFold, false,sourceDir);
                        }
                            
                        
                        String trainFile =sourceDir+File.separator+"trainset.arff";
                        String testFile=sourceDir+File.separator+"testset.arff";
                        
                        if (isDirichletSampling==true){
                           DirichletSampling
                                   dSample= new DirichletSampling();
                           
                          trainFile=dSample.resampleMinorityClasses(trainFile);
                          testFile=dSample.sampleDataByDirichlet(testFile);
                          
                        }

                             // clean up and create directories
                            File folder = new File (sourceDir+File.separator+"graphs");
                            if (!folder.isDirectory())
                                        folder.mkdirs();

                            File cleanupFiles = new File (sourceDir+File.separator+"*.arff");
                            cleanupFiles.delete();
                            /// cleanup ends


                             train_Test_Arff[1]=testFile;
                             train_Test_Arff[0]=trainFile;

                              // if (isMergeTwoDataSets==true)
                                //   m.mergeDatasetsPartiallyandClassify(dataBase,train_Test_Arff[1],train_Test_Arff[0],numberofParts,funcCount,
                                 //		  objWekaGraphEval,singleReleaseNumParts,isResampling);
                               //else
                                    m.graphicalEvaluation( train_Test_Arff, datasetName,totalClassesInput,objWekaGraphEval,
                                            isResampling, sourceDir);
                                 //end: weka format conversion
                 
                              ConfusionTable ct= objWekaGraphEval.getConfusionTable();
                              ct.calculateMeasures();    
                              //ct.print();
                              java.util.Map<String,Double> sortedClassByPerc=ct.calculateMeasuresPerClass();
                              //measures per Rank in each array (i.e., rank = total Classes)
                              Double []lSensitivity=ct.getSensitivity();
                              Double []lSpecificity=ct.getSpecificity();
                              Double []lPCCC=ct.getPCCC();
                              Double []lCsmfAccuracy=ct.getCsmfAccuracy();
                              
                              for (int j=0; j<totalClassesInput; j++){// saving sensitivitiies sum for multiple repetitions
                                  sensitivity[j]+=lSensitivity[j];
                                  specificity[j]+=lSpecificity[j];
                                  PCCC[j]+=lPCCC[j];
                                  csmfAccuracy[j]+=lCsmfAccuracy[j];
                                  
                              }
                              
                              //code for classes and their percentages in rank 1
                              for (Map.Entry<String,Double> it:sortedClassByPerc.entrySet()){
                                  String key=it.getKey();
                                  if (!sortedClassesForRank1.containsKey(key))
                                        //taking care of new classes
                                        // could be found in different iterations
                                      sortedClassesForRank1.put(key,it.getValue());
                                  else{
                                     Double val=sortedClassesForRank1.get(key);
                                     val+=it.getValue();
                                     sortedClassesForRank1.put(key,val);
                                     
                                    }
                              }
         
       }// end of repetitions
        
       BufferedWriter outFile=new BufferedWriter(new FileWriter(finalOutputFile));
       String text="";
       if (isDirichletSampling)
           text="According to Resampling of Test Set by Dirichlet Distribution";
       else
           text="According to Cross Validation On Original Distribution of Classes";
       
        System.out.println("*****************************************************************");
        System.out.println("*****************************Final Output************************");
        System.out.println(text);
        System.out.println("*****************************************************************");
        System.out.println("Total repetitions "+ maxRepetitions);
       
        outFile.write("*****************************************************************");
        outFile.newLine();
        outFile.write("*****************************Final Output************************");
        outFile.newLine();
        outFile.write(text);
        outFile.newLine();
        outFile.write("*****************************************************************");
        outFile.newLine();
        outFile.write("Total repetitions "+ maxRepetitions);
        outFile.newLine();
       
        for (int rank=0; rank<totalClassesInput;rank++){
            sensitivity[rank]=sensitivity[rank]/maxRepetitions;
            specificity[rank]=specificity[rank]/maxRepetitions;
            PCCC[rank]=PCCC[rank]/maxRepetitions;
            csmfAccuracy[rank]=csmfAccuracy[rank]/maxRepetitions;

            System.out.println("Rank "+ (rank+1) +": sensitivity= "+sensitivity[rank]
                      + ", specificity= "+specificity[rank]+ ", PCCC= "+PCCC[rank]+ ", csmf accuracy= "+ csmfAccuracy[rank] );
 
            outFile.write("Rank "+ (rank+1) +": sensitivity= "+sensitivity[rank]
                      + ", specificity= "+specificity[rank]+ ", PCCC= "+PCCC[rank]+ ", csmf accuracy= "+ csmfAccuracy[rank] );
            outFile.newLine();
       }
   
         //Code for classes and their percentages in rank 1
        System.out.println();
        System.out.println();
        outFile.newLine();
        outFile.newLine();
        
        System.out.println("Top classes for Rank 1 sorted by their sensitivities are:");
        outFile.write("Top classes in Rank 1 sorted by their sensitivities are:");
        outFile.newLine();
        
        java.util.SortedSet<Map.Entry<String, Double>> sortedSetByVal= new 
                                     java.util.TreeSet<>(new ValueComparator());
        
         for (Map.Entry<String,Double> it:sortedClassesForRank1.entrySet()){
                    String key=it.getKey();
                    Double val= it.getValue();
                    val=(val/maxRepetitions)*100;
                    sortedClassesForRank1.put(key,val);
                        
            }
         sortedSetByVal.addAll(sortedClassesForRank1.entrySet());
         System.out.println(sortedSetByVal);
         outFile.write(sortedSetByVal.toString());
         outFile.newLine();
       
         outFile.flush();
         outFile.close();
        /*int totalRanks=objWekaGraphEval.repTP.size();
        for (int cnt=0; cnt<totalRanks;cnt++){
            Double tp=objWekaGraphEval.repTP.get(cnt);
            Double tpRate=(tp/(double)maxRepetitions)/100;
           
           // Calculating PCCC=(TP-(k/N))/(1-(k/N))
            // k = top 1 , 2 ,..n and N= 15/classes
            double KoverN= (double)(cnt+1)/(double)totalClassesInput;
            double OneMinusKoverN= 1-KoverN;
            double PCCC= (tpRate-KoverN)/OneMinusKoverN;
            //sensitivity=tp/total records
            //specificity=tn/(fp+tn)
            System.out.println("On top "+ (cnt+1)+ ", Sensitivity= "+tpRate + ", PCCC= "+PCCC ); 
            //calcualte CSMF for each rank
            
        
        }*/
       // ConfusionTable ct= objWekaGraphEval.getConfusionTable();
        //ct.calculateMeasures();
       // ct.print();

        } catch (Exception ex) {
           // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            try{
                m.endTimeMeasure(startTime,outputBuildTime,"In Catch")   ;
            }
            catch (Exception exCatch){
                System.out.println(exCatch.getMessage());
            }
        }
        finally {
                  
                        
        }
    }
    

   

/**
 *
 * @param arff_Files
 * @param program
 * @throws Exception
 */




    public void graphicalEvaluation(String [] arff_Files,String program,Integer funcCount,MyWeka2 objWeka,
            Boolean isResampling, String sourceDir) throws Exception{
            
          
            
            
            String rankingfile=sourceDir+File.separator+"multipleClassifireRankings_"+program+"_rank.txt";
            String chartFile="";
            Double functionCounts=Double.parseDouble(funcCount.toString());
       


        
        //if (objWeka==null){
            // if (arff_Files[1].isEmpty() || arff_Files[0].contains("MergedDatasets"))
          //        objWeka= new MyWekaClass();
             //else
               //     objWeka=new  MyWekaIdIdentifier("PMR#");  //MyWekaClass();

             objWeka.cumulativeNonCumulative(rankingfile,1);
        //}
        

          //objWeka.cumulativeNonCumulative(fileName,numberOfDB);
         
          String training=arff_Files[0];
          String db1= training.substring(training.lastIndexOf(File.separator)+1,training.length());
                 db1= db1.replace("Episodes_", "").replace("_BOTH_1.arff","").replace("_apar_2","")
                         .replace("TestDatasets_","").replace("MergedDatasets_", "");

          String chartTitle=  "";
          String db2="";
          String testFile=null;

         /* if (arff_Files[1].isEmpty()){


              objWeka.createTestTrainData(training,singleReleaseNumParts, 1,false);
              training=sourceDir+File.separator+"trainset.arff";
              testFile=sourceDir+File.separator+"testset.arff";
              chartFile=sourceDir+File.separator+"graphs"+File.separator+db1+".png";
              //String tmp=obfuscateList.get(db1);
              //if (tmp!=null) db1=tmp;
              chartTitle="F007 on "+program+" with 25% train-set and 75% test-set--if isnvert=false";



             }
          else {*/
               testFile=arff_Files[1];
              /* db2= testFile.substring(testFile.lastIndexOf(File.separator)+1,testFile.length());
               db2= db2.replace("Episodes_", "").replace("_BOTH_1.arff","").replace("_apar_2","")
                       .replace("TestDatasets_","").replace("MergedDatasets_", "");
               chartFile=sourceDir+File.separator+"graphs"+File.separator+db1+"_to_"+db2+".png";
               String tmp=obfuscateList.get(db1);
               if (tmp!=null)db1=tmp;
               tmp=obfuscateList.get(db2);
               if (tmp!=null)db2=tmp;
               if (testFile.contains("TestDatasets"))
                    chartTitle=  "Using "+db1 + " and 10% of "+db2+ " to identify faulty functions in "+db2;
               else*/
                //    chartTitle=  "Using  "+db1 + " to identify faulty functions in "+db2;
                chartTitle=  "Using training file to identify faulty functions in test file";

          
         // }

          

          

           //File f=new File(sourceDir +File.separator+"pairwise");
           //recursiveDelete(f);
                  
            // do resampling only for the training file
                   objWeka.generate_1aginstall_files ( training, sourceDir +File.separator+"pairwise",isResampling);
      
                   
                   // filess

                  String executedPercentFile=sourceDir+File.separator+"graphs"+File.separator+"Results_"+program+".html"; //open same html file
                           //"\\graphs\\allGraphPercentages.txt";
                   BufferedWriter outExecFile=new BufferedWriter(new FileWriter(executedPercentFile,false));
                   outExecFile.write("<p>////////////////////////////////////////////////////////");
                   outExecFile.newLine();
                   outExecFile.write("<b> Using "+db1+" to identify faults in "+db2 +" </b>");
                   outExecFile.newLine();
                   outExecFile.write("////////////////////////////////////////////////////////</p>");
                 //  outExecFile.write ("<style type=\"text/css\"> div.ex { width:600px; padding:10px;"+
                   //        "border:5px solid gray; margin:0px; }</style>");
                   outExecFile.newLine();
                   outExecFile.close();
                   /// files
                   
                   objWeka.multipleClassifiers(sourceDir+File.separator+"pairwise",testFile,functionCounts,
                           //program +" and Functions "+ functionCounts,rankingfile,"",null,chartFile,executedPercentFile);
                   chartTitle,rankingfile,"",null,chartFile,executedPercentFile);


              /// objWeka.voting(PROGRAMDIR+"\\pairwise_"+program+"_1\\pairwise",
                                         //objWeka.voting("C:\\shary\\Traces\\Trace_Results\\pairwisefiles",
                 //           "C:\\shary\\Traces\\Trace_Results\\pairwise_"+program+"\\testset.arff",functionCounts[cnt],
                   //         program +" and Functions "+ functionCounts[cnt],fileName);









    }

    /**
   * Recursive delete files.
   */
  void recursiveDelete (File dirPath) throws Exception {
    System.gc();
    String [] ls = dirPath.list ();
    
    if (ls!=null)
        for (int idx = 0; idx < ls.length; idx++) {
            File file = new File (dirPath, ls [idx]);
            if (file.isDirectory ())
                recursiveDelete (file);
            //System.out.println ("deleting..."+file.getName());
            if (!file.delete ())
                throw new FileNotFoundException("unable to delete"+file);
            file=null;
        }
    
  }

    
     
public long startTimeMeasure(){
    // amrk the start time
    long startTime = System.currentTimeMillis();
    return startTime;
}

public void endTimeMeasure(long startTime,BufferedWriter outputBuildTime, String msg) throws IOException{
    //**** measure elapsed time 
           long endTime = System.currentTimeMillis();
           long elapsedTime = endTime - startTime;
           double seconds = elapsedTime / 1.0E03;
           double minutes= seconds/60;
           
           outputBuildTime.write(msg +  " Elapsed Time is "+ seconds +" seconds and "+ minutes+" minutes" );
           outputBuildTime.newLine();
           outputBuildTime.flush();
          
           
}


    ////////////////////////
}