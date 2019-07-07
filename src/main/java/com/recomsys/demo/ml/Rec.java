package com.recomsys.demo.ml;

import com.recomsys.demo.JavaConf;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.mllib.clustering.KMeans;
//import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.rdd.RDD;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.mortbay.util.IO;
import scala.Tuple2;
import scala.Tuple3;

import java.io.*;
import java.util.*;

public class Rec {

    static SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("Rec");
    static JavaSparkContext sc = new JavaSparkContext(conf);
    static String FP_addr = JavaConf.FPPath;                //each groupName: longest freqItem
    private String data_addr;        //address of training data

    //private KMeansModel kMeansModel;

//    public Rec() {
//        this("module_input/trainingdata2.txt");
//    }            //default data_addr

    public Rec() { }

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

    private boolean chgTrainingData() {                  //change data content, same addr
         return iniModel();
    }

    private boolean iniModel() {
        return FP_train();
    }

    private boolean FP_train() {

        double minSupport = 0.2;
        int numPartitions = 1;

        List<Tuple2<String, List<List<String>>>> tmp = groups().collect();

        Map<String, List<String>> toFile = new HashMap<>();

        for(int i=0;i<tmp.size();i++) {

            JavaRDD<List<String>> dataForFP = sc.parallelize(tmp.get(i)._2);
            FPGrowthModel<String> model = new FPGrowth().setMinSupport(minSupport).setNumPartitions(numPartitions).run(dataForFP);

            FPGrowth.FreqItemset<String> record = model.freqItemsets().toJavaRDD().mapToPair(x -> new Tuple2<>(x.javaItems().size(), x)).sortByKey(false).take(1).get(0)._2;
            toFile.put(tmp.get(i)._1, record.javaItems());
        }

        File file = new File(FP_addr);
        if(!file.isFile()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file, false));
            out.writeObject(toFile);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    
    private JavaPairRDD<String, List<String>> preProcess() {
        JavaRDD<List<String>> data1 = sc.textFile(data_addr).map(x -> Arrays.asList(x.split(" "))).filter(x -> x.size() > 1);
        return data1.mapToPair(x -> new Tuple2<>(x.get(0), new ArrayList<>(new HashSet<>(x.subList(1, x.size())))));      //disdinct
    }

    private JavaPairRDD<String, List<List<String>>> groups() {

        JavaPairRDD<String, List<List<String>>> tmp = preProcess().mapToPair(x -> {
            List<List<String>> x2 = new ArrayList<>();
            x2.add(x._2);
            return new Tuple2<>(x._1, x2);
        });

        return tmp.reduceByKey((x, y) -> {
            List<List<String>> res = new ArrayList<>(x);
            res.addAll(y);
            return res;
        });
    }


    public List<String> jobRec(List<String> skills) {
        Map<String, List<String>> records = loadRecords();
        List<Tuple2<String, List<String>>> list = new ArrayList<>();
        Iterator iter = records.entrySet().iterator();
        while (iter.hasNext()) {
                Map.Entry<String, List<String>> entry = (Map.Entry) iter.next();
                list.add(new Tuple2<>(entry.getKey(), entry.getValue()));
        }
        JavaPairRDD<String, List<String>> tmp1 = sc.parallelize(list).mapToPair(x -> x);
        JavaPairRDD<Double, String> weightAndName = tmp1.mapToPair(x -> {
            int len1 = x._2.size();
            int len2 = skills.size();
            List<String> v1_cpy = new ArrayList<>(x._2);
            v1_cpy.retainAll(x._2);
            int match = v1_cpy.size();
            return new Tuple2<>(match/Math.sqrt(len1*len2), x._1);
        }).sortByKey(false);
        return weightAndName.map(x -> x._2).take(4);
    }

    private Map<String, List<String>> loadRecords() {
        File file = new File(FP_addr);

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            return (Map<String, List<String>>) in.readObject();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {    //???????
            iniModel();
        }
        return null;

    }

    private List<String> loadRecord(String job) {
        return loadRecords().get(job);
    }

    public List<String> skillRec(String job, List<String> skills) {       //jinengbianhao
        if(job == null || job.length()==0) {
                return skillRec(skills);
        }

        List<String> items = new ArrayList<>(loadRecord(job));

        items.removeAll(skills);
        return items;
    }

    private List<String> skillRec(List<String> skills) {
        return skillRec(jobRec(skills).get(0), skills);
    }

    public boolean writeJobList(){

        try{
            FileWriter writer = new FileWriter(JavaConf.jobListPath);

            JavaRDD<String> jobrdd = preProcess().keys().distinct();
            int num = Integer.valueOf(String.valueOf(jobrdd.count()));

            List<String> lists = jobrdd.take(num);


                lists.forEach((x) -> {
                    try {
                        writer.write(x + "\n");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });

            writer.close();
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

//    public static void main(String[] args) {
//        Rec rec = new Rec("/home/ds/JobProgram/result.txt");

//        List<String> skills = Arrays.asList(new String[]{"spring", "mysql", "oracle"});
//        System.out.println(rec.skillRec(skills));
//        System.out.println(rec.jobRec(skills));
//
//        rec.chgTrainingData("/home/ds/JobProgram/result.txt");
//        System.out.println(rec.skillRec(skills));
//        System.out.println(rec.skillRec("测试java中级工程师", skills));
//        System.out.println(rec.jobRec(skills));
//    }
}
