package com.recomsys.demo.ml;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.apache.spark.mllib.fpm.FPGrowth;
import scala.Tuple2;

import java.io.*;
import java.util.*;

import static java.lang.System.exit;

public class SkillRec {

    static SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("SkillRec");
    static JavaSparkContext sc = new JavaSparkContext(conf);

    private String data_addr = "/home/ds/JobProgram/together_result.txt";            //
//    static String model_path = "/home/ds/IdeaProjects/DecisionTreeTest/src/main/resources/skillRecModels";
    static String model_path = "/home/hadoop/IdeaProjects/RecomSysdemo/module_save/skillRecModels";
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
        return delModelDir(model_path);
    }

    private boolean delModelDir(String path) {
        File file = new File(path);
        if (!file.exists()) {//判断是否待删除目录是否存在
            System.err.println("The dir are not exists!");
            return false;
        }

        String[] content = file.list();//取得当前目录下所有文件和文件夹
        for (String name : content) {
            File temp = new File(path, name);
            if (temp.isDirectory()) {//判断是否是目录
                delModelDir(temp.getAbsolutePath());//递归调用，删除目录里的内容
                temp.delete();//删除空目录
            } else {
                if (!temp.delete()) {//直接删除文件
                    System.err.println("Failed to delete " + name);
                    return false;
                }
            }
        }
        return true;
    }

    public FPGrowthModel<String> train(String job) {

        int numPartitions = 1;
        double minSupport = 0.01;

        JavaRDD<List<String>> pre_transactions = sc.textFile(data_addr).map(x -> Arrays.asList(x.split(" "))).map(x -> new HashSet<>(x)).map(x -> new ArrayList<>(x));
        JavaRDD<List<String>> mid_transactions = pre_transactions;
        if (job != null && job.length() != 0)         //has job prefer
            mid_transactions = pre_transactions.filter(x -> x.size() > 1 && x.get(0).contains(job));
        JavaRDD<List<String>> final_transactions = mid_transactions.map(x -> {
            List<String> res = new ArrayList<>();
            res.addAll(x.subList(1, x.size()));
            return res;
        });
        //System.out.println(final_transactions.collect());

        FPGrowthModel<String> model = new FPGrowth().setMinSupport(minSupport).setNumPartitions(numPartitions).run(final_transactions);

        return model;
    }

    public List<String> skillRec(final String[] skills) throws IOException {
        return skillRec(null, skills);
    }

    public List<String> skillRec(String job, final String[] skills) throws IOException {      //job==null or empty means no job prefer

        if (job == null)
            job = "";

        //String path = model_path+"/"+job;

        FPGrowthModel model;            //<String>
        File filePath = new File(model_path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        File file = new File(filePath, job);
        if (!file.exists()) {
            file.mkdirs();
            //exit(5);
        }
        File[] listFiles = file.listFiles();
        if (listFiles.length <= 0) {                //no file
            model = train(job);
            if (model.freqItemsets().isEmpty()) {           //threshold for FP too high
                return new ArrayList<String>();                    //no job suitable, return empty
            }
            //System.out.println(model.freqItemsets().toJavaRDD().collect());

            model.save(sc.sc(), model_path + "/" + job);
        } else {
            System.out.println(listFiles.length);
            model = FPGrowthModel.load(sc.sc(), model_path + "/" + job);
        }


        final List<String> skillList = new ArrayList<>(Arrays.asList(skills));
        JavaRDD<FPGrowth.FreqItemset<String>> freqItemsets = model.freqItemsets().toJavaRDD();   //null
        System.out.println(freqItemsets.collect());

        JavaPairRDD<Double, List<String>> sortedSets = freqItemsets.mapToPair(x -> {
            List<String> list = new ArrayList<>(x.javaItems());
            int len = list.size();
            list.removeAll(skillList);
            int len2 = list.size();

            double weight = (len - len2 + 1) * Math.sqrt(x.freq()) - len;           //core

            return new Tuple2<>(weight, list);
        }).sortByKey(false);

        System.out.println(sortedSets.collect());

        List<String> res1 = sortedSets.map(x -> x._2).reduce((x, y) -> {
            List<String> list = new ArrayList<>(x);
            list.addAll(y);
            return list;
        });
        JavaRDD<String> res2 = sc.parallelize(res1).distinct();
        System.out.println(res2.collect());
        //System.out.println(res2.take(3));     //recommend 3 skills

        return res2.take(3);       //don't like them? reRecommend?
    }

//    public static void main(String[] args) throws IOException {
//        SkillRec jobrec = new SkillRec();
//        List<String> res = jobrec.skillRec("AI算法工程师",new String[]{"java", "matlab", "spring"});
//        System.out.println(res);
//
//        jobrec.chgTrainingData("/home/ds/JobProgram/AI_result.txt");
//        System.out.println(jobrec.skillRec("AI算法工程师",new String[]{"java", "matlab", "spring"}));
//        System.out.println(jobrec.skillRec("AI软件工程师",new String[]{"java", "matlab", "spring"}));
//      }
}



