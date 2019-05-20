package com.trade.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.trade.Enum.EntrustTypeEnum;
import com.trade.comm.KeyValues;
import com.trade.dto.FentrustData;

public class DeepMergeUtil {

    public static Comparator<FentrustData> prizeComparatorDESC = new Comparator<FentrustData>() {
		public int compare(FentrustData o1, FentrustData o2) {
			boolean flag = o1.getFid().intValue()==o2.getFid().intValue() && o1.getFid().intValue()!=0 ;
			if(flag){
				return 0 ;
			}
			int ret = o2.getFprize().compareTo(o1.getFprize()) ;
			if(ret==0){
				return o1.getFid().compareTo(o2.getFid()) ;
			}else{
				return ret ;
			}
		}
	} ;
	
	public static Comparator<FentrustData> prizeComparatorASC = new Comparator<FentrustData>() {
		public int compare(FentrustData o1, FentrustData o2) {
			boolean flag = o1.getFid().intValue()==o2.getFid().intValue() && o1.getFid().intValue()!=0 ;
			if(flag){
				return 0 ;
			}
			int ret = o1.getFprize().compareTo(o2.getFprize()) ;
			if(ret==0){
				return o1.getFid().compareTo(o2.getFid()) ;
			}else{
				return ret ;
			}
		}
	} ;

    public static TreeSet<FentrustData> deepCaculate(int fvid,int type,Set<FentrustData> current_fentrustDatas ,int deep){
    	TreeSet<FentrustData> fentrustDatas;
    	if(type== EntrustTypeEnum.BUY){
    		fentrustDatas = new TreeSet<FentrustData>(prizeComparatorDESC) ;
    	}else{
    		fentrustDatas = new TreeSet<FentrustData>(prizeComparatorASC);
    	}
		Object[] objs = current_fentrustDatas.toArray() ;
		Map<String, KeyValues> map = new HashMap<String, KeyValues>() ;
		String format = deepFormat(deep);
		for (Object obj : objs) {
			if(obj==null){
				continue ;
			}
			FentrustData fentrust = (FentrustData)obj ;
			String key = String.valueOf(StringUtils.doubleToString(fentrust.getFprize(),format)) ;
			KeyValues keyValues = map.get(key) ;
			if(keyValues==null){
				keyValues = new KeyValues();
				keyValues.setKey(StringUtils.doubleToString(fentrust.getFprize(),format)) ;
				keyValues.setValue(fentrust.getFleftCount()) ;
			}else{
				keyValues.setValue((Double)keyValues.getValue()+fentrust.getFleftCount()) ;
			}
			map.put(key, keyValues);
		}
		int id=0;
		for (Map.Entry<String, KeyValues> entry : map.entrySet()) {
			id++;
			FentrustData fentrustdata = new FentrustData() ;
			fentrustdata.setFid(id);
			fentrustdata.setFprize(Double.parseDouble(entry.getValue().getKey().toString())) ;
			fentrustdata.setFleftCount((Double) entry.getValue().getValue()) ;
			fentrustdata.setDeep(deep);
			fentrustdata.setFviFid(fvid);
			fentrustdata.setFentrustType(type);
			fentrustDatas.add(fentrustdata) ;
		}
		return fentrustDatas;
    }

    public static String deepFormat(int i){
    	String format="#";
		switch(i){
			case 0:format="#";break;
			case 1:format="#.#";break;
			case 2:format="#.##";break;
			case 3:format="#.###";break;
			case 4:format="#.####";break;
			default:format="#.####";break;
		}
		return format;
    }
    public static String deepFormat2(int i){
    	String format="0";
    	i = i + 4;
		switch(i){
			case 0:format="0";break;
			case 1:format="0.0";break;
			case 2:format="0.00";break;
			case 3:format="0.000";break;
			case 4:format="0.0000";break;
			case 5:format="0.00000";break;
			case 6:format="0.000000";break;
			case 7:format="0.0000000";break;
			case 8:format="0.00000000";break;
			default:format="0.0000";break;
		}
		return format;
    }
    /*public static void main(String[] args) {
    	Set<FentrustData> current_fentrustDatas = new TreeSet<FentrustData>();
    	for(int i=0;i<20;i++){
    		FentrustData fentrustData=new FentrustData();
    		fentrustData.setFid(i);
    		fentrustData.setFviFid(1);
    		fentrustData.setFentrustType(1);;
    		fentrustData.setFprize(i*0.0001);
    		fentrustData.setFleftCount(1.00);
    		current_fentrustDatas.add(fentrustData);
    	}
    	FentrustData fentrustData=new FentrustData();
		fentrustData.setFid(20);
		fentrustData.setFviFid(1);
		fentrustData.setFentrustType(1);;
		fentrustData.setFprize(0.0001);
		fentrustData.setFleftCount(1.00);
		current_fentrustDatas.add(fentrustData);
    	TreeSet<FentrustData> fens=deepCaculate(1,current_fentrustDatas,3);
    	for(FentrustData fen :fens){
    		System.out.println(fen.getFprize());
    		System.out.println(fen.getFleftCount());
    		System.out.println("-----");
    	}
    	
	}*/
}
