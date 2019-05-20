package com.trade.Enum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BankTypeEnum {
    public static final int GONGHANG = 1;//工行
    public static final int NONGHANG = 2;//农行
    public static final int JIANHANG = 3;//建行
    public static final int ZHAOHANG = 4;//招行
    public static final int JIAOTONG = 5;//交通银行
    public static final int SHIYE = 6;//中信银行
    public static final int PINGAN = 7;//平安银行
    public static final int ZHONGGUO = 8;//中国银行
    public static final int PUFA = 9;//浦发银行
    public static final int XINGYE = 10;//兴业银行
    public static final int GUANGDA = 11;//光大银行
    public static final int ZHIFUBAO = 100;//支付宝
    public static final int MINSHENG = 12;//民生银行
    public static final int YOUZHENG = 13;//中国邮政储蓄银行

	public final static List<String> bankNames = new ArrayList<>(Arrays.asList(
			"工商银行",
			"农业银行",
			"建设银行",
			"招商银行",
			"交通银行",
			"中信银行",
			"平安银行",
			"中国银行",
			"浦发银行",
			"兴业银行",
			"光大银行",
			"民生银行",
			"中国邮政储蓄银行"/*,
			"其他银行"*/));

    public static String getEnumString(int value) {
		String name = "";
		switch (value) {
		case GONGHANG:
			name = "工商银行";
			break;
		case NONGHANG:
			name = "农业银行";
			break;
		case JIANHANG:
			name = "建设银行";
			break;
		case ZHAOHANG:
			name = "招商银行";
			break;
		case JIAOTONG:
			name = "交通银行";
			break;
		case SHIYE:
			name = "中信银行";
			break;
		case PINGAN:
			name = "平安银行";
			break;
		case ZHONGGUO:
			name = "中国银行";
			break;
		case PUFA:
			name = "浦发银行";
			break;
		case XINGYE:
			name = "兴业银行";
			break;
		case GUANGDA:
			name = "光大银行";
			break;
		case MINSHENG:
			name = "民生银行";
			break;
		case YOUZHENG:
			name = "中国邮政储蓄银行";
			break;
//		case ZHIFUBAO:
//			name = "支付宝";
//			break;
		default:
			name = "其他银行";
			break;
		}
		return name;
	}
    
}
