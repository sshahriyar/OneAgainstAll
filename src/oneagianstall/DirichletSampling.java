/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oneagianstall;

import cc.mallet.types.Alphabet;
import cc.mallet.types.Dirichlet;
import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import weka.core.AttributeStats;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author shary
 */
public class DirichletSampling {
    
    /**
     * Adjust the distribution of those classes which are in minority by resampling with replacement
     * @param arffFileName
     * @return
     * @throws Exception 
     */
    public String resampleMinorityClasses(String arffFileName) throws Exception{
        
            MyWeka2 weka=new MyWeka2();
            
            Instances data=weka.loadDataSets(arffFileName)[0];
            
            if (!data.classAttribute().isNominal())
                 throw new Exception ("No class found");
                
           // int numOfClasses=data.numClasses()-1;//subtract -1 for others classes 
            //int numOfRecsToGenerate=data.numInstances();
            
            AttributeStats stats=data.attributeStats(data.classIndex());
           int []totalSamples=stats.nominalCounts;
           int totalRec=stats.totalCount;
           double threshold=0.10;
           // returns samples for 15 classes (dimesnions), equivalent to nmofRecs in quantity
            //int [] totalSamples=d.drawObservation(numOfRecsToGenerate);//s(numOfRecsToGenerate);
            //double []totalPercentage=new double[totalSamples.length];    
               
           // System.out.println("\nSample Size");
            for (int i=0;i<totalSamples.length;i++){
                    
                     double percent=(double)totalSamples[i]/((double)totalRec);
                     if (percent<threshold){
                         double newPerc=Math.round(threshold *totalRec);
                         totalSamples[i]=(int)newPerc;
                     }
            }
           // System.out.println();
            //
            //sampling from dataset
            //
          return sampleDataset(totalSamples, data, arffFileName);
            
          
    
    
    }
   
    
    
    
    /**
     * Returns the sampled file by using Dirichlet
     * @param arffFileName Takes arff file
     * @return 
     */
    
    public String sampleDataByDirichlet(  String arffFileName) throws Exception{
        
            MyWeka2 weka=new MyWeka2();
            
            Instances data  =weka.loadDataSets(arffFileName)[0];
            
            if (!data.classAttribute().isNominal())
                 throw new Exception ("No class found");
                
            int numOfClasses=data.numClasses()-1;//subtract -1 for others classes 
            int numOfRecsToGenerate=data.numInstances();
            
            
           // Dirichlet sampling
           // int []totalSamples= new int[numOfClasses];
           // Arrays.fill(totalSamples,1); // make sure that there is at least one instance of every class
            // Randomly select alpha parameter for each of the classes
            Random r=new Random();
            double []alpha = new double[numOfClasses];
           // double p=r.nextDouble();
            for (int alp=0;alp<alpha.length;alp++)
                alpha[alp]=r.nextDouble();
                
            
            Dirichlet d=new Dirichlet(alpha);
            
           // returns samples for 15 classes (dimesnions), equivalent to nmofRecs in quantity
            int [] totalSamples=d.drawObservation(numOfRecsToGenerate);//s(numOfRecsToGenerate);
                
               
           // System.out.println("\nSample Size");
            for (int i=0;i<totalSamples.length;i++){
                    if (totalSamples[i]==0)
                        totalSamples[i]=1;
            //        System.out.print( "Class "+i+":" +totalSamples[i] + " ");
            }
           // System.out.println();
            //
            //sampling from dataset
            //
          return sampleDataset(totalSamples, data, arffFileName);
            
          
    
    
    }
   
    /***
     * Creates the dataset based on a given distribution of classes in int[] array
     * @param totalSamples Distribution array
     * @param data Weka Instances
     * @param arffFileName
     * @return
     * @throws Exception 
     */
    
   private String sampleDataset(int []totalSamples, Instances data, String arffFileName) throws Exception{
        //
            //sampling from dataset
            //
            Instances finalData= new Instances(data,0,0);
            
            int startIdx=0;
            for (int j=0; j<totalSamples.length;j++){
                int classJthIns=totalSamples[j];
                
                //System.out.println(startIdx);
                String previousClass="";
                if (startIdx<data.numInstances())
                   previousClass=data.instance(startIdx).stringValue(data.classIndex());
                
                int idx;
                for (idx=startIdx;idx<data.numInstances();idx++){
                
                    // Instances tempData=data;
                    String classVal= data.instance(idx).stringValue(data.classIndex());
                    
                    //if random instances are more than the instances, copy them again                    
                   if (!classVal.equalsIgnoreCase(previousClass) && (classJthIns > 0))
                            idx=startIdx; //idx-classJthIns;
                   else if (!classVal.equalsIgnoreCase(previousClass) && (classJthIns <=0)){
                    
                       break;
                   } 
                   else if (classJthIns>0 && idx==(data.numInstances()-1))
                       idx=startIdx;
                   
                   if (classJthIns > 0){
                        finalData.add(data.instance(idx));
                        classJthIns--;
                    }
                   
                }
                
                 startIdx=idx;
                
            }// end of sampling
            
            data.delete();
            data=null;
            
            String fileName=arffFileName.replace(".arff", "_dirichlet.arff");
            ConverterUtils.DataSink.write(fileName, finalData);
     
            return fileName;
          
   }
    
    
    public static void main(String []args){
        DirichletSampling dS=new DirichletSampling();
        //dS.generateRandomDataset();
        // some sample code somefunction() {
            //      double [] p={0.55,0.200,0.10,0.50,0.5,0.5};
            //Dirichlet d1 =new Dirichlet(p);
            //System.out.println("Sample 1");
            /* System.out.println("------------------------");
            
            for (int rep=0; rep<50;rep++){
            int [] sample=d1.drawObservation(15);
            for (int i=0;i<sample.length;i++)
            System.out.print(sample[i] + " ");
            System.out.println();
            }
            double sum=0.0;
            
            double [] dist=d.nextDistribution();
            for (int i=0;i<dist.length;i++){
            System.out.print(dist[i] + " ");
            sum+=dist[i];
            }
            
            System.out.print("\n sum:"+sum);// this should be 1.0
            }
            */
            
    }
}
