package com.android.internal.policy.impl.lewa.view;

import java.util.HashMap;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import android.util.Log;

public class Expression {

    private static final String TAG = "Expression";
    
    /**
     * =========================================================================
     * �鲿��ʵʱ��̬����
     */
    public static final String ACTUAL_X = "actual_x";
    public static final String ACTUAL_Y = "actual_y";
    
    public static final String MOVE_X = "move_x";//����ʱ��x�����ƶ�����
    public static final String MOVE_Y = "move_y";//����ʱ��y�����ƶ�����
    
    public static final String TOUCH_X = "touch_x";//��ǰ������ x
    public static final String TOUCH_Y = "touch_y";//��ǰ������ y
    
    public static final String MOVE_DIST = "move_dist";//����ʱ�ƶ�����
    
    public static final String STATE = "state";//����:0  ����:1   �������λ��:2
    
    public static final String MUSIC_STATE = "music_state";
    public static final int MUSIC_STATE_PLAY = 1;
    public static final int MUSIC_STATE_STOP = 0;
    
    /**
     * =========================================================================
     */
    
    public static final String AMPM = "ampm";//������ // 0 am, 1 pm
    public static final String HOUR12 = "hour12";//12Сʱ��
    public static final String HOUR24 = "hour24";//24Сʱ��
    public static final String MINUTE = "minute";//����
    public static final String MILLISECOND = "msec";//��
    public static final String DATE = "date";//��
    public static final String MONTH = "month";//��  //0-11
    public static final String YEAR = "year";//��
    public static final String DAY_OF_WEEK = "day_of_week";//����  // 1-7 �����յ�������
    public static final String GLOBAL = "global";

    public static final String BATTERY_LEVEL = "battery_level";//��ص��� 0-100
    public static final String BATTERY_STATE = "battery_state";//���״̬�� ����:0 ���:1 ������:2 �ѳ���:3
    
    public static final int BATTERY_STATE_CHARGING = 1;
    public static final int BATTERY_STATE_FULL = 3;
    public static final int BATTERY_STATE_LOW = 2;
    public static final int BATTERY_STATE_UNPLUGGED = 0;
    
    public static final String CALL_MISSED_COUNT = "call_missed_count";//δ�ӵ绰
    public static final String SMS_UNREAD_COUNT = "sms_unread_count";//δ������

    public static final String NEXT_ALARM_TIME = "next_alarm_time";
    
    public static final String SCREEN_HEIGHT = "screen_height";//��Ļ�߶�
    public static final String SCREEN_WIDTH = "screen_width";//��Ļ���
    
    public static final String SECOND = "second";
    

    public static final String TEXT_WIDTH = "text_width";
    public static final String TIME = "time"; //��ǰʱ�䣬long

    public static final int UNLOCKER_STATE_NORMAL = 0;
    public static final int UNLOCKER_STATE_PRESSED = 1;
    public static final int UNLOCKER_STATE_REACHED = 2;
    
    public static final String VISIBILITY = "visibility";
    public static final int VISIBILITY_FALSE = 0;
    public static final int VISIBILITY_TRUE = 1;
    
    /**
     * '|'ָ���������ַ�������values��ָ������ֵ������ʱ(��Ϊnull)���ô������ַ���ʱ�滻��
     * Ȼ���ڽ���replaceAllʱ����ԭʼ���ʽ������Ӱ��
     */
    private static final String SPECIAL_CHAR = "0";
    
    private String pattern = "^-?\\d+$";//����
    
    /**
     * values�Ǵ�����������ĸ��ֱ����ľ���ֵ�ģ��統ǰδ�ӵ绰Ϊ3
     */
    private static HashMap<String, String> values = new HashMap<String, String>();
    
    private HashMap<String, String> objStrs = new HashMap<String,String>();
    private HashMap<String, Double> objDous = new HashMap<String,Double>();
    
    private static HashMap<String, HashMap<String, String>> realTimeVars = new HashMap<String, HashMap<String,String>>();
    private static HashMap<String, String> realTimeVar;
    
    
    private static Evaluator evaluator = new Evaluator();
    
    /**
     * �����ֱ���ֵ���浽values���ݽṹ��
     * @param variable �������ƣ��磺sms_unread_count
     * @param value ����ֵ���磺2 ��2��δ�����ţ�
     */
    public static void put(String variable,String value){
        if(variable == null || variable.trim().equals("")){
            Log.e(TAG, "variable is invalid, variable == " + variable);
            return;
        }
        
        values.put(variable, value);
    }
    
    /**
     * ���ݱ������ƻ�ñ���ֵ�����Ϊnull���򷵻�Ĭ��ֵ
     * @param variable
     * @param defaultValue
     * @return
     */
    public static String get(String variable,String defaultValue){
        String value = values.get(variable);
        if(value == null){
            value = defaultValue;
        }
        return value;
    }
    
    /**
     * 
     * @param mName "phone_unlocker"
     * @param attrName "state"
     * @param expValue "1"
     */
   public static void putRealTimeVar(String mName,String attrName,String expValue){
       realTimeVar = realTimeVars.get(mName);
       if(realTimeVar == null){
           realTimeVar = new HashMap<String, String>();
           realTimeVars.put(mName, realTimeVar);
       }
       realTimeVar.put(attrName, expValue);
    }
    
    /**
     * 
     * @param mName "phone_unlocker"
     * @param attrName "state"
     * @param expValue "1"
     */
    public static String getRealTimeVar(String mName,String attrName,String defaultValue){
        realTimeVar = realTimeVars.get(mName);
        if(realTimeVar == null){
            return defaultValue;
        }
        String realVar = realTimeVar.get(attrName);
        
        if(realVar == null){
            return defaultValue;
        }
        return realVar;
    }
    
    /**
     * ������xml���ʽ�еı�����ֵ,���Һ��б��ʽ
     * @param attrName ���������Ե�����
     * @param expValue ���������Ե�ֵ
     */
    public void putDou(String attrName, String expValue){
        Double dou = 0d;
        if(expValue.matches(pattern)){
            dou = Double.valueOf(expValue);
        }else {
            
            try {
                dou = Double.valueOf(evaluator.evaluate(transform(expValue)));
                
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Log.e(TAG, "NumberFormatException result == " + dou);
                dou = 0d;
            } catch (EvaluationException e) {
                e.printStackTrace();
                Log.e(TAG, "EvaluationException expValue == " + expValue);
            }
        }
        
        objDous.put(attrName, dou);
        
    }
    
    private static String getVar(String variable){
        return getVar(variable, "0");
    }
    
    /**
     * ���ݱ��ʽ�����еı�����������Ӧ��ֵ
     * @param expName
     * @param defaultValue
     * @return
     */
    private static String getVar(String variable,String defaultValue){
        if(variable == null || variable.trim().equals("")){
            Log.e(TAG, "variable is invalid, variable == " + variable + " ,return defaultValue == " + defaultValue);
            return defaultValue;
        }
        if(values.get(variable) == null){
            
            return SPECIAL_CHAR;
        }
      
        return values.get(variable);
    }
    
    /**
     * ���ʽ��ת��
     * @param expValue
     * @return
     */
    private static String transform(String expValue){
        
        if(expValue == null || expValue.trim().equals("")){
            Log.e(TAG, "expValue is invalid, expValue == " + expValue);
            return null;
        }
        
        if(expValue.indexOf("#ampm") != -1){
            expValue = expValue.replaceAll("#ampm",getVar(AMPM));
        }
        if(expValue.indexOf("#hour12") != -1){
            expValue = expValue.replaceAll("#hour12",getVar(HOUR12));
        }
        if(expValue.indexOf("#hour24") != -1){
            expValue = expValue.replaceAll("#hour24",getVar(HOUR24));
        }
        if(expValue.indexOf("#minute") != -1){
            expValue = expValue.replaceAll("#minute",getVar(MINUTE));
        }
        if(expValue.indexOf("#msec") != -1){
            expValue = expValue.replaceAll("#msec",getVar(MILLISECOND));
        }
        if(expValue.indexOf("#date") != -1){
            expValue = expValue.replaceAll("#date",getVar(DATE));
        }
        if(expValue.indexOf("#month") != -1){
            expValue = expValue.replaceAll("#month",getVar(MONTH));
        }
        if(expValue.indexOf("#year") != -1){
            expValue = expValue.replaceAll("#year",getVar(YEAR));
        }
        if(expValue.indexOf("#day_of_week") != -1){
            expValue = expValue.replaceAll("#day_of_week",getVar(DAY_OF_WEEK));
        }
        if(expValue.indexOf("#battery_level") != -1){
            expValue = expValue.replaceAll("#battery_level",getVar(BATTERY_LEVEL));
        }
        if(expValue.indexOf("#battery_state") != -1){
            expValue = expValue.replaceAll("#battery_state",getVar(BATTERY_STATE));
        }
        if(expValue.indexOf("#call_missed_count") != -1){
            expValue = expValue.replaceAll("#call_missed_count",getVar(CALL_MISSED_COUNT));
        }
        if(expValue.indexOf("#sms_unread_count") != -1){
            expValue = expValue.replaceAll("#sms_unread_count",getVar(SMS_UNREAD_COUNT));
        }
        if(expValue.indexOf("#screen_height") != -1){
            expValue = expValue.replaceAll("#screen_height",getVar(SCREEN_HEIGHT));
        }
        if(expValue.indexOf("#screen_width") != -1){
            expValue = expValue.replaceAll("#screen_width",getVar(SCREEN_WIDTH));
        }
        if(expValue.indexOf("#text_width") != -1){
            expValue = expValue.replaceAll("#text_width",getVar(TEXT_WIDTH));
        }
        if(expValue.indexOf("#time") != -1){
            expValue = expValue.replaceAll("#time",getVar(TIME));
        }
        
        if(expValue.indexOf(".move_y") != -1){
            int position = expValue.indexOf(".move_y");
            String subStr = expValue.substring(0, position);
            String name = subStr.substring(subStr.lastIndexOf("#") + 1, position);
            expValue = expValue.replaceAll(new StringBuilder().append("#").append(name).append(".move_y").toString(),getRealTimeVar(name, MOVE_Y, "0"));
        }
        if(expValue.indexOf(".move_x") != -1){
            int position = expValue.indexOf(".move_x");
            String subStr = expValue.substring(0, position);
            String name = subStr.substring(subStr.lastIndexOf("#") + 1, position);
            expValue = expValue.replaceAll(new StringBuilder().append("#").append(name).append(".move_x").toString(),getRealTimeVar(name, MOVE_X, "0"));
        }
        if(expValue.indexOf(".state") != -1){
            int position = expValue.indexOf(".state");
            String subStr = expValue.substring(0, position);
            String name = subStr.substring(subStr.lastIndexOf("#") + 1, position);
            expValue = expValue.replaceAll(new StringBuilder().append("#").append(name).append(".state").toString(),getRealTimeVar(name, STATE, "0"));
        }
        if(expValue.indexOf(".music_state") != -1){
            int position = expValue.indexOf(".music_state");
            String subStr = expValue.substring(0, position);
            String name = subStr.substring(subStr.lastIndexOf("#") + 1, position);
            expValue = expValue.replaceAll(new StringBuilder().append("#").append(name).append(".music_state").toString(),getRealTimeVar(name, MUSIC_STATE, "0"));
        }
        if(expValue.indexOf(".move_dist") != -1){
            int position = expValue.indexOf(".move_dist");
            String subStr = expValue.substring(0, position);
            String name = subStr.substring(subStr.lastIndexOf("#") + 1, position);
            expValue = expValue.replaceAll(new StringBuilder().append("#").append(name).append(".move_dist").toString(),getRealTimeVar(name, MOVE_DIST, "0"));
        }
        if(expValue.indexOf(".visibility") != -1){
            int position = expValue.indexOf(".visibility");
            String subStr = expValue.substring(0, position);
            String name = subStr.substring(subStr.lastIndexOf("#") + 1, position);
            expValue = expValue.replaceAll(new StringBuilder().append("#").append(name).append(".visibility").toString(),getRealTimeVar(name, VISIBILITY, "0"));
        }
        return expValue;
       
    }
    
    /**
     * ���ݲ�����������������Ӧ��ֵ.���㲢���ؽ��
     * @param attrName ������
     * @param defaultValue ����Ĭ��ֵ
     * @return
     */
    public Double getDou(String attrName,Double defaultValue){
        if(attrName == null || attrName.trim().equals("")){
            Log.e(TAG, "attrName is invalid, attrName == " + attrName + " ,return defaultValue == " + defaultValue);
            return defaultValue;
        }
        Double result = objDous.get(attrName);
        if(result == null){
            result = defaultValue;
        }
        return result;
    }
    
    /**
     * ������xml���ʽ�еı�����ֵ,����ֵΪString����,���Һ��б��ʽ
     * @param elementName ����������
     * @param attrName ���������Ե�����
     * @param expValue ���������Ե�ֵ
     */
    public void putStr(String attrName, String expValue){
        objStrs.put(attrName, expValue);
    }
    
    public String getStr(String attrName){
        
        return objStrs.get(attrName);
    }

	static public int caculateInt(String exp)
	{
		try{
			Double d;
			d = Double.valueOf(evaluator.evaluate(transform(exp)));
			return d.intValue(); 
		} catch (EvaluationException e) {
                e.printStackTrace();
                Log.e(TAG, "EvaluationException expValue == " + exp);
				return 0;
        }
	}
    
}
