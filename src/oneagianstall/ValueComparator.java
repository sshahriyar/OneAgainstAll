/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oneagianstall;

import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author shary
 */
public class ValueComparator implements Comparator <Map.Entry<String,Double>>{
        
        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        @Override
        public int compare(Map.Entry<String,Double> a, Map.Entry<String,Double> b) {
            int val= b.getValue().compareTo(a.getValue());
            return val!=0? val:1;
          }
    }
   
