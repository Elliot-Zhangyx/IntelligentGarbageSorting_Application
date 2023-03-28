//package com.example.garbagesorting;
//
//import android.os.Handler;
//
//class MyThread extends Thread{
//    Handler   mHandler;
//    Boolean  boo;
//    public MyThread(Handler handler){
//        mHandler = handler;
//    }
//    public void setBoo(boolean b) {boo = b; }
//
//    @Override
//    public void run() {
//        super.run();
//        if(boo){
//            mHandler.post(new Runnable() {
//                public void run() {
//                  //  setWeather();//更新UI
//                });//更新UI
//                boo = true;
//            }
//        }
//    }
//}