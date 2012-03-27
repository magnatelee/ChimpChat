package edu.berkeley.wtchoi.cc;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/26/12
 * Time: 8:46 PM
 * To change this template use File | Settings | File Templates.
 */
class MonkeyControlOption{
    private String mainActivity;
    private String applicationPackage;
    private long timeout = 5000;
    private String adb;


    //to check whether all basic informations are there
    public boolean isComplete(){
        if(mainActivity == null) return false;
        if(applicationPackage == null) return false;
        if(adb == null) return false;
        return true;
    }

    public String getApplicationPackage(){
        return applicationPackage;
    }

    public String getRunComponent(){
        return applicationPackage + '/' + mainActivity;
    }

    public String getADB(){
        return adb;
    }

    public long getTimeout(){
        return timeout;
    }

    public void setMainActivity(String s){
        mainActivity = s;
    }

    public void setApplicationPackage(String s){
        applicationPackage = s;
    }

    public void setTimeout(long t){
        timeout = t;
    }

    public void setADB(String s){
        adb = s;
    }
}