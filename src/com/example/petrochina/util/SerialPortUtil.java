package com.example.petrochina.util;

import java.io.BufferedWriter;  
import java.io.File;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.io.OutputStreamWriter;  
import java.io.PrintWriter;  

import com.example.petrochina.serial.SerialPort;
  
/** 
 * 串口操作类 
 *  
 * @author Jerome 
 *  
 */  
public class SerialPortUtil {  
	private String TAG = SerialPortUtil.class.getSimpleName();
    private SerialPort mSerialPortOne;
    private SerialPort mSerialPortTwo;
    private SerialPort mSerialPortThree;
    private OutputStream mOutputStreamOne;
    private OutputStream mOutputStreamTwo;
    private OutputStream mOutputStreamThree;
    private InputStream mInputStreamOne;
    private InputStream mInputStreamTwo;
    private InputStream mInputStreamThree;
    private ReadThreadOne mReadThreadOne;
    private ReadThreadTwo mReadThreadTwo;
    private ReadThreadThree mReadThreadThree;
    private String path_one = "/dev/ttymxc1";
    private String path_two = "/dev/ttymxc2";
    private String path_three = "/dev/ttymxc3";
    private int baudrate = 9600;
    private static SerialPortUtil portUtil;
    private OnDataReceiveListener onDataReceiveListener = null;
    private boolean isStopOne = false;
    private boolean isStopTwo = false;
    private boolean isStopThree = false;

    public interface OnDataReceiveListener {
        public void onDataReceive(byte[] buffer, int size);
    }

    public void setOnDataReceiveListener(
            OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }

    public static SerialPortUtil getInstance() {
        if (null == portUtil) {
            portUtil = new SerialPortUtil();
            portUtil.onCreate();
        }
        return portUtil;
    }

    /**
     * 初始化串口通信
     */
    private void onCreate() {
        try {
        	//serial_one
            mSerialPortOne = new SerialPort(new File(path_one), baudrate, 0);
            mOutputStreamOne = mSerialPortOne.getOutputStream();
            mInputStreamOne = mSerialPortOne.getInputStream();
            mReadThreadOne = new ReadThreadOne();
                       
            //serial_two
            mSerialPortTwo = new SerialPort(new File(path_two), baudrate, 0);
            mOutputStreamTwo = mSerialPortTwo.getOutputStream();
            mInputStreamTwo = mSerialPortTwo.getInputStream();
            mReadThreadTwo = new ReadThreadTwo();
            
            //serial_three
            mSerialPortThree = new SerialPort(new File(path_three), baudrate, 0);
            mOutputStreamThree = mSerialPortThree.getOutputStream();
            mInputStreamThree = mSerialPortThree.getInputStream();
            mReadThreadThree = new ReadThreadThree();
            isStopOne = false;
            isStopTwo = false;
            isStopThree = false;
            //start serial
            mReadThreadOne.start();
            mReadThreadTwo.start();
            mReadThreadThree.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送指令到串口
     * 
     * @param cmd
     * @return
     */
    public boolean sendCmdsOne(String cmd) {
        boolean result = true;
        byte[] mBuffer = cmd.getBytes();
        try {
            if (mOutputStreamOne != null) {
            	mOutputStreamOne.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    
    public boolean sendCmdsTwo(String cmd) {
        boolean result = true;
        byte[] mBuffer = cmd.getBytes();
        try {
            if (mOutputStreamTwo != null) {
            	mOutputStreamTwo.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    
    public boolean sendCmdsThree(String cmd) {
        boolean result = true;
        byte[] mBuffer = cmd.getBytes();
        try {
            if (mOutputStreamThree != null) {
            	mOutputStreamThree.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean sendBufferOne(byte[] mBuffer) {
        boolean result = true;
        String tail = "";
        byte[] tailBuffer = tail.getBytes();
        byte[] mBufferTemp = new byte[mBuffer.length+tailBuffer.length];
        System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
        System.arraycopy(tailBuffer, 0, mBufferTemp, mBuffer.length, tailBuffer.length);
        try {
            if (mOutputStreamOne != null) {
                mOutputStreamOne.write(mBufferTemp);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    
    public boolean sendBufferTwo(byte[] mBuffer) {
        boolean result = true;
        String tail = "";
        byte[] tailBuffer = tail.getBytes();
        byte[] mBufferTemp = new byte[mBuffer.length+tailBuffer.length];
        System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
        System.arraycopy(tailBuffer, 0, mBufferTemp, mBuffer.length, tailBuffer.length);
        try {
            if (mOutputStreamTwo != null) {
                mOutputStreamTwo.write(mBufferTemp);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    
    public boolean sendBufferThree(byte[] mBuffer) {
        boolean result = true;
        String tail = "";
        byte[] tailBuffer = tail.getBytes();
        byte[] mBufferTemp = new byte[mBuffer.length+tailBuffer.length];
        System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
        System.arraycopy(tailBuffer, 0, mBufferTemp, mBuffer.length, tailBuffer.length);
        try {
            if (mOutputStreamThree != null) {
                mOutputStreamThree.write(mBufferTemp);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    private class ReadThreadOne extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isStopOne && !isInterrupted()) {
                int size;
                try {
                    if (mInputStreamOne == null)
                        return;
                    byte[] buffer = new byte[512];
                    size = mInputStreamOne.read(buffer);
                    if (size > 0) {
//                          String str = new String(buffer, 0, size);
//                          Logger.d("length is:"+size+",data is:"+new String(buffer, 0, size));
                        if (null != onDataReceiveListener) {
                            onDataReceiveListener.onDataReceive(buffer, size);
                        }
                    }
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
    
    private class ReadThreadTwo extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isStopTwo && !isInterrupted()) {
                int size;
                try {
                    if (mInputStreamTwo == null)
                        return;
                    byte[] buffer = new byte[512];
                    size = mInputStreamTwo.read(buffer);
                    if (size > 0) {
//                          String str = new String(buffer, 0, size);
//                          Logger.d("length is:"+size+",data is:"+new String(buffer, 0, size));
                        if (null != onDataReceiveListener) {
                            onDataReceiveListener.onDataReceive(buffer, size);
                        }
                    }
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
    
    private class ReadThreadThree extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isStopThree && !isInterrupted()) {
                int size;
                try {
                    if (mInputStreamThree == null)
                        return;
                    byte[] buffer = new byte[512];
                    size = mInputStreamThree.read(buffer);
                    if (size > 0) {
//                          String str = new String(buffer, 0, size);
//                          Logger.d("length is:"+size+",data is:"+new String(buffer, 0, size));
                        if (null != onDataReceiveListener) {
                            onDataReceiveListener.onDataReceive(buffer, size);
                        }
                    }
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPortOne() {
        isStopOne = true;
        if (mReadThreadOne != null) {
            mReadThreadOne.interrupt();
        }
        if (mSerialPortOne != null) {
            mSerialPortOne.close();
        }
    }
    
    public void closeSerialPortTwo() {
        isStopTwo = true;
        if (mReadThreadTwo != null) {
            mReadThreadTwo.interrupt();
        }
        if (mSerialPortTwo != null) {
            mSerialPortTwo.close();
        }
    }
    
    public void closeSerialPortThree() {
        isStopThree = true;
        if (mReadThreadThree != null) {
            mReadThreadThree.interrupt();
        }
        if (mSerialPortThree != null) {
            mSerialPortThree.close();
        }
    }

}