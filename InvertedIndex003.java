/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg322.pkg2022;

/**
 *
 * @author ehab
 */
/*
 * InvertedIndex - Given a set of text files, implement a program to create an 
 * inverted index. Also create a user interface to do a search using that inverted 
 * index which returns a list of files that contain the query term / terms.
 * The search index can be in memory. 
 * 

 */
import java.io.*;
import java.util.*;

import javax.lang.model.util.ElementScanner14;

//=====================================================================
class DictEntry3 {

    public int doc_freq = 0; // number of documents that contain the term
    public int term_freq = 0; //number of times the term is mentioned in the collection
    public HashSet<Integer> postingList;

    DictEntry3() {
        postingList = new HashSet<Integer>();
    }
}

//=====================================================================
class Index3 {

    //--------------------------------------------
    Map<Integer, String> sources;  // store the doc_id and the file name
    HashMap<String, DictEntry3> index; // THe inverted index
    //--------------------------------------------

    Index3() {
        sources = new HashMap<Integer, String>();
        index = new HashMap<String, DictEntry3>();
    }

    //---------------------------------------------
    public void printPostingList(HashSet<Integer> hset) {
        Iterator<Integer> it2 = hset.iterator();
        while (it2.hasNext()) {
            System.out.print(it2.next() + ", ");
        }
        System.out.println("");
    }
    
    public void printDictionary() {
        Iterator it = index.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry) it.next();
//            DictEntry3 dd = (DictEntry3) pair.getValue();
//            System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "] <" + dd.term_freq + "> =--> ");
//            //it.remove(); // avoids a ConcurrentModificationException
//             printPostingList(dd.postingList);
//        }
        System.out.println("------------------------------------------------------");
        System.out.println("*****    Number of terms = " + index.size());
        System.out.println("------------------------------------------------------");

    }

    //-----------------------------------------------
    public void buildIndex(String[] files) {
        int i = 0;
        for (String fileName : files) {
            try ( BufferedReader file = new BufferedReader(new FileReader(fileName))) {
                sources.put(i, fileName);
                String ln;
                while ((ln = file.readLine()) != null) {
                    String[] words = ln.split("\\W+");
                    for (String word : words) {
                        word = word.toLowerCase();
                        // check to see if the word is not in the dictionary
                        if (!index.containsKey(word)) {
                            index.put(word, new DictEntry3());
                        }
                        // add document id to the posting list
                        if (!index.get(word).postingList.contains(i)) {
                            index.get(word).doc_freq += 1; //set doc freq to the number of doc that contain the term 
                            index.get(word).postingList.add(i); // add the posting to the posting:ist
                        }
                        //set the term_fteq in the collection
                        index.get(word).term_freq += 1;
                    }
                }

            } catch (IOException e) {
                System.out.println("File " + fileName + " not found. Skip it");
            }
            i++;
        }
         printDictionary();
    }

    //--------------------------------------------------------------------------
    // query inverted index
    // takes a string of terms as an argument
    public String find(String phrase) {
        String result = "";
        String[] words = phrase.split("\\W+");
        HashSet<Integer> res = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        for (String word : words) {
            res.retainAll(index.get(word).postingList);
        }
        if (res.size() == 0) {
            System.out.println("Not found");
            return "";
        }
       // String result = "Found in: \n";
        for (int num : res) {
            result += "\t" + sources.get(num) + "\n";
        }
        return result;
    }

    //----------------------------------------------------------------------------  
    HashSet<Integer> intersect(HashSet<Integer> pL1, HashSet<Integer> pL2) {
        HashSet<Integer> answer = new HashSet<Integer>();;
        Iterator<Integer> itP1 = pL1.iterator();
        Iterator<Integer> itP2 = pL2.iterator();
        int docId1 = 0, docId2 = 0;

        if (itP1.hasNext()) 
            docId1 = itP1.next();        
        if (itP2.hasNext()) 
            docId2 = itP2.next();
        
        while (itP1 != null && itP2 != null) {
            if (docId1 == docId2) {
                answer.add(docId1);
                if(!itP1.hasNext() || !itP2.hasNext()) break;
                else{
                    docId1 = itP1.next();
                    docId2 = itP2.next();
                }
            }
            else if(docId1 < docId2) {
                if(itP1.hasNext()) docId1 = itP1.next();
                else break;
            }
            else{
                if(itP2.hasNext()) docId2 = itP2.next();
                else break;
            }
        }
        return answer;
    }

    HashSet<Integer> union(HashSet<Integer> pL1, HashSet<Integer> pL2) {
        HashSet<Integer> answer = new HashSet<Integer>();;
        Iterator<Integer> itP1 = pL1.iterator();
        Iterator<Integer> itP2 = pL2.iterator();
        int docId1 = 0, docId2 = 0;
        if (itP1.hasNext()){
            docId1 = itP1.next();
            answer.add(docId1);
        }

        if (itP2.hasNext()){
            docId2 = itP2.next();
            answer.add(docId2);
        }

        while (itP1.hasNext()) {
            docId1 = itP1.next();
            answer.add(docId1);
        }
        while (itP2.hasNext()) {
            docId2 = itP2.next();
            answer.add(docId2);
        }
            return answer;
        }

        HashSet<Integer> not(String word) {
        ArrayList<Integer> all = new ArrayList<Integer>();
        for (Map.Entry<Integer,String> entry : sources.entrySet())
            {
                all.add(entry.getKey());
            }
        HashSet<Integer> not = new HashSet<Integer>(index.get(word.toLowerCase()).postingList);
        HashSet<Integer> result = new HashSet<Integer>();
        Iterator<Integer> it = not.iterator();
        while (it.hasNext()) {
            all.remove(it.next());
        }
        for(int i : all) {
            result.add(i);
        }
        return result;
    }

    //-----------------------------------------------------------------------   

    public String find_01(String phrase) { // 2 term phrase  2 postingsLists
        String result = "";
        String[] words = phrase.split("\\W+");
        // 1- get first posting list
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
      //  printPostingList(pL1);
        // 2- get second posting list
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
    //    printPostingList(pL2);

        // 3- apply the algorithm
        HashSet<Integer> answer = intersect(pL1, pL2);

        for (int num : answer) {
            //System.out.println("\t" + sources.get(num));
           result += "\t" + sources.get(num) + "\n";
        }
        return result;
    }
//-----------------------------------------------------------------------         

    public String find_02(String phrase) { // 3 lists
 
        String result = "";
        String[] words = phrase.split("\\W+");
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        //printPostingList(pL1);
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
        //printPostingList(pL2);
        HashSet<Integer> answer1 = intersect(pL1, pL2);
        //printPostingList(answer1);
        HashSet<Integer> pL3 = new HashSet<Integer>(index.get(words[2].toLowerCase()).postingList);        
        //printPostingList(pL3);
        HashSet<Integer> answer2 = intersect(pL3, answer1);
               // printPostingList(answer2);

//        result = "Found in: \n";
        for (int num : answer2) {
            //System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }        
        return result;

    }
    //-----------------------------------------------------------------------         

    public String find_03(String phrase) { // any mumber of terms non-optimized search 
        String result = "";
        String[] words = phrase.split("\\W+");
        int len = words.length;
        HashSet<Integer> res = new HashSet<Integer>();
        if(!words[0].equals("not"))
        {
            res = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        }
        int i = 1;
        while (i < len) { 
            if(words[0].equals("not"))
            {
                res = not(words[i]);
                i++;
                words[0] = "";
            }
            else if(words[i].equals("and") && words[i+1].equals("not"))
            {
                res = intersect(res, not(words[i+2]));
                i += 3;
            }
            else if(words[i].equals("or") && words[i+1].equals("not"))
            {
                res = union(res, not(words[i+2]));
                i += 3;
            }
            else if(words[i].equals("or")){
                res = union(res, index.get(words[i+1].toLowerCase()).postingList);
                i += 2;
            }
            else if(words[i].equals("and")){
                res = intersect(res, index.get(words[i+1].toLowerCase()).postingList);
                i += 2;
            }    
        }
        for (int num : res) {
            //System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }
        return result;

    }
   public void Jac(String query)
    {
        int numberOfDocs = sources.size();
        String tok [] = query.split(" ");
        ArrayList<String> tokens = new ArrayList<String>();
        //ArrayList<Float> jacSim = new ArrayList<Float>();
        HashMap<Integer, Float> jac = new HashMap<Integer, Float>();
        int arr[];
        arr = new int[2];
        arr[0] = 0; arr[1] = 0;
        for(String s : tok)
        {
            tokens.add(s.toLowerCase());
        }
        for(int i = 0; i < numberOfDocs; i++)
        {
            int idx = i;
            index.keySet().stream().forEach(key -> {
                if(index.get(key).postingList.contains(idx))
                {
                    if(tokens.contains(key.toLowerCase())) arr[0] += 1;
                    arr[1] += 1;
                }
            }
            );
            int union = tokens.size() + arr[1] - arr[0];
            //jacSim.add( (float)arr[0] / (float)union);
            jac.put(idx, (float)arr[0] / (float)union);
            arr[0] = arr[1] = 0;
        }
        LinkedHashMap<Integer, Float> reverseSortedMap = new LinkedHashMap<>();
        jac.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
        .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
        Iterator itr=reverseSortedMap.keySet().iterator();
        while(itr.hasNext())    
        {    
            int key=(int)itr.next();
            System.out.println(sources.get(key).split("\\\\")[1].split("\\.")[0] +" -> Jaccard Similarity -> "+jac.get(key));  
        } 
    }
    
        //-----------------------------------------------------------------------         
    String[] rearrange(String[] words, int[] freq, int len) {
        boolean sorted = false;
        int temp;
        String sTmp;
        for (int i = 0; i < len - 1; i++) {
            freq[i] = index.get(words[i].toLowerCase()).doc_freq;
        }
        //-------------------------------------------------------
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < len - 1; i++) {
                if (freq[i] > freq[i + 1]) {
                    temp = freq[i];
                    sTmp = words[i];
                    freq[i] = freq[i + 1];
                    words[i] = words[i + 1];
                    freq[i + 1] = temp;
                    words[i + 1] = sTmp;
                    sorted = false;
                }
            }
        }
        return words;
    }

    //-----------------------------------------------------------------------         
    public String find_04(String phrase) { // any mumber of terms optimized search 
        String result = "";
        String[] words = phrase.split("\\W+");
        int len = words.length;
        //int [] freq = new int[len];
        words = rearrange(words, new int[len], len);
        HashSet<Integer> res = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        int i = 1;
        while (i < len) {
            res = intersect(res, index.get(words[i].toLowerCase()).postingList);
            i++;
        }
        for (int num : res) {
            //System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }
        return result;
    }
    //-----------------------------------------------------------------------         

    public void compare(String phrase) { // optimized search 
        long iterations = 5000000;
        String result = "";
        long startTime = System.currentTimeMillis();
        for (long i = 1; i < iterations; i++) {
            result = find(phrase);
        }
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(" (*) elapsed = " + estimatedTime + " ms.");
        //-----------------------------      
        System.out.println(" result = " + result);
        startTime = System.currentTimeMillis();
        for (long i = 1; i < iterations; i++) {
            result = find_03(phrase);
        }
        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(" (*) Find_03 non-optimized intersect  elapsed = " + estimatedTime + " ms.");
        System.out.println(" result = " + result);
//        //-----------------------------       
        startTime = System.currentTimeMillis();
        for (long i = 1; i < iterations; i++) {
            result = find_04(phrase);
        }
        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(" (*) Find_04 optimized intersect elapsed = " + estimatedTime + " ms.");
        System.out.println(" result = " + result);
    }
}

//=====================================================================
public class InvertedIndex003 {

    public static void main(String args[]) throws IOException {
        Index3 index = new Index3();
 /**/ 
        index.buildIndex(new String[]{
            "docs\\Doc1.txt", // change it to your path e.g. "c:\\tmp\\100.txt"
            "docs\\Doc2.txt",
            "docs\\Doc3.txt",
            /*"D:\\IR\\docs\\503.txt",
            "D:\\IR\\docs\\504.txt",            
            "D:\\IR\\docs\\100.txt", // change it to your path e.g. "c:\\tmp\\100.txt"
            "D:\\IR\\docs\\101.txt",
            "D:\\IR\\docs\\102.txt",
            "D:\\IR\\docs\\103.txt",
            "D:\\IR\\docs\\104.txt",
            "D:\\IR\\docs\\105.txt",
            "D:\\IR\\docs\\106.txt",
            "D:\\IR\\docs\\107.txt",
            "D:\\IR\\docs\\108.txt",
            "D:\\IR\\docs\\109.txt"      */        
        });  
  /**/        

  index.index.keySet().stream().forEach(key -> {
    System.out.println(key + ": " + index.index.get(key).postingList);
}
);
System.out.println("***************************************Jaccard Similarity***************************************");
index.Jac("idea of March is amazing");
System.out.println("************************************************************************************************");
//System.out.println(index.find_03("not staff or not seif and sherif or is and bassel"));
}

    }
