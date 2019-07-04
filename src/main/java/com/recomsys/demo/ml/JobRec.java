package com.recomsys.demo.ml;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Model;
import scala.Tuple2;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JobRec {

    static SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("JobRec");
    static JavaSparkContext sc = new JavaSparkContext(conf);
//    static String model_addr = "/home/ds/IdeaProjects/DecisionTreeTest/src/main/resources/jobRecModel.dat";
    static String model_addr = "/home/hadoop/IdeaProjects/RecomSysdemo/module_save/jobRecModel.dat";
    private String data_addr = "/home/ds/JobProgram/together_result.txt";        //address of training data

    //private JavaPairRDD<String, List<String>> model;          //==null  ;    should write to hard memory

    public String getData_addr() {
        return data_addr;
    }

    public void setData_addr(String data_addr) {
        this.data_addr = data_addr;
    }

    public boolean chgTrainingData(String addr) {          //change data content & addr altogether
        setData_addr(addr);
        return chgTrainingData();
    }

    public boolean chgTrainingData() {                  //change data content, same addr
        return clrModel();
    }

    private boolean clrModel() {
        File file = new File(model_addr);
        try {
            if (!file.exists()) {
                file.createNewFile();
                return true;
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private JavaPairRDD<String, List<String>> train() {
        JavaRDD<String> data = sc.textFile(data_addr);
        JavaRDD<List<String>> dataClean = data.map(x -> Arrays.asList(x.split(" "))).filter(x -> x.size() >= 2);    //asList  new ArrayList()
        return dataClean.mapToPair(x -> new Tuple2<>(x.get(0), new ArrayList<>(x.subList(1, x.size()))));
    }

    public List<String> jobRecs(String[] skills) throws IOException {

        List<String> skillList = Arrays.asList(skills);

        File file = new File(model_addr);                  //relative data_address
//        if (false == file.isFile()) {
//            System.out.println("file");
//            return null;                 //
//        }

        JavaPairRDD<String, List<String>> model = null;

        try {

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));

            model = (JavaPairRDD<String, List<String>>) in.readObject();

            model = listToRDD((List<List<String>>) in.readObject());

            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {             //EOF, ClassNotFound
            model = train();

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file, false));  //
            out.writeObject(rddToList(model));
        }

        //if(model == null)


        JavaPairRDD<String, Tuple2<Integer, Integer>> counts = model.mapToPair(x -> {
            List<String> list = new ArrayList<>(x._2);
            int len = list.size();
            list.retainAll(skillList);
            int len2 = list.size();
            return new Tuple2<>(x._1, new Tuple2<>(len2, len));   //len2:skills matched num;  len: all num
        });

        JavaPairRDD<String, Tuple2<Integer, Integer>> countsByJob = counts.reduceByKey((x, y) -> new Tuple2<>(x._1 + y._1, x._2 + y._2));
        JavaPairRDD<Double, String> weightAndJob = countsByJob.mapToPair(x -> new Tuple2<>(x._2._1 / (double) x._2._2, x._1));
        return weightAndJob.sortByKey(false).map(x -> x._2).take(3);     //recommend 3

    }

    private JavaPairRDD<String, List<String>> listToRDD(List<List<String>> item) {
        JavaRDD<List<String>> tmp = sc.parallelize(item);
        JavaPairRDD<String, List<String>> res = tmp.mapToPair(x -> new Tuple2<>(x.get(0), x.subList(1, x.size())));
        return res;
    }

    private List<List<String>> rddToList(JavaPairRDD<String, List<String>> rdd) {
        return rdd.map(x -> {
            List<String> res = new ArrayList<>();
            res.add(x._1);
            res.addAll(x._2);
            return res;
        }).collect();
    }

//    public static void main(String[] args) throws IOException {
//        JobRec jobrec = new JobRec();
//        List<String> res = jobrec.jobRecs(new String[]{"java", "matlab", "spring"});
//        System.out.println(res);
//
//        jobrec.chgTrainingData("/home/ds/JobProgram/AI_result.txt");
//        //System.out.println(jobrec.jobRecs(new String[]{"java", "matlab", "spring"}));
//
//    }
}
