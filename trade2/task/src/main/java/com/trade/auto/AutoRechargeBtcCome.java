package com.trade.auto;

import com.alibaba.fastjson.JSONObject;
import com.trade.Enum.VirtualCapitalOperationInStatusEnum;
import com.trade.Enum.VirtualCoinTypeStatusEnum;
import com.trade.comm.ConstantMap;
import com.trade.model.BTCInfo;
import com.trade.model.BTCMessage;
import com.trade.model.Fvirtualcaptualoperation;
import com.trade.model.Fvirtualcointype;
import com.trade.service.front.FrontVirtualCoinService;
import com.trade.util.BTCUtils;
import com.trade.util.Utils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.List;

public class AutoRechargeBtcCome {
	private static final Logger log = LoggerFactory
			.getLogger(AutoRechargeBtcCome.class);
	@Autowired
	private RechargeBtcData rechargeBtcData ;
	@Autowired
	private FrontVirtualCoinService frontVirtualCoinService ;
	@Autowired
	ConstantMap constantMap;

	public void work() {
//		System.out.println("AutoRechargeBtcCome work invoke");
		synchronized (this) {

//			System.out.println("AutoRechargeBtcCome work execute");


			try{


						//遍历现有的
						List<Fvirtualcointype> fvirtualcointypes = this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
						for (Fvirtualcointype fvirtualcointype : fvirtualcointypes) {
							try{
								if(fvirtualcointype==null || fvirtualcointype.getFstatus()== VirtualCoinTypeStatusEnum.Abnormal || !fvirtualcointype.isFIsRecharge()){
									continue ;
								}

//								System.out.println("come wallet loop begin on ip = " + fvirtualcointype.getFip() + " , port = " + fvirtualcointype.getFport());

								BTCMessage btcMessage = new BTCMessage() ;
								btcMessage.setACCESS_KEY(fvirtualcointype.getFaccess_key()) ;
								btcMessage.setIP(fvirtualcointype.getFip()) ;
								btcMessage.setPORT(fvirtualcointype.getFport()) ;
								btcMessage.setSECRET_KEY(fvirtualcointype.getFsecrt_key()) ;

								BTCUtils btcUtils = new BTCUtils(btcMessage) ;

								String[] tradeNumbers = this.rechargeBtcData.getSubKeys(fvirtualcointype.getFid()) ;
//								System.out.println("come tradeNumbers " + tradeNumbers.length);
								for (String tradeNo : tradeNumbers) {
									Fvirtualcaptualoperation fvirtualcaptualoperation = this.rechargeBtcData.subGet(fvirtualcointype.getFid(), tradeNo) ;
										if(fvirtualcaptualoperation!=null){
										fvirtualcaptualoperation = this.frontVirtualCoinService.findFvirtualcaptualoperationById(fvirtualcaptualoperation.getFid()) ;
										if(fvirtualcaptualoperation.getFstatus()!= VirtualCapitalOperationInStatusEnum.SUCCESS){
											BTCInfo btcInfo = null ;
											try {
												btcInfo = btcUtils.gettransactionValue(fvirtualcaptualoperation.getFtradeUniqueNumber(), "receive") ;
											} catch (Exception e1) {
//												e1.printStackTrace();
											}
											if(btcInfo==null){
												log.error("Fvirtualcaptualoperation:"+fvirtualcaptualoperation.getFid()+" cannot find in btcwallet!") ;
												continue ;
											}

											if(btcInfo.getConfirmations()>=0){
												fvirtualcaptualoperation.setFconfirmations(btcInfo.getConfirmations()) ;
												fvirtualcaptualoperation.setFamount(btcInfo.getAmount());
												if(fvirtualcointype.getConfirmTimes() == 0){
													fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.SUCCESS) ;
												}else{
													switch (btcInfo.getConfirmations()) {
														case VirtualCapitalOperationInStatusEnum.WAIT_0:
															fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.WAIT_0) ;
															break;
														case VirtualCapitalOperationInStatusEnum.WAIT_1:
															fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.WAIT_1) ;
															break;
														case VirtualCapitalOperationInStatusEnum.WAIT_2:
															fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.WAIT_2) ;
															break;
														default:
															if (fvirtualcaptualoperation.getFconfirmations() >= fvirtualcointype.getConfirmTimes()) {
																fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.SUCCESS) ;
															}
															break;
													}
												}
												fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp()) ;
												try{
													this.frontVirtualCoinService.updateFvirtualcaptualoperationCoinIn(fvirtualcaptualoperation) ;
													if(fvirtualcaptualoperation.getFstatus()== VirtualCapitalOperationInStatusEnum.SUCCESS){
														this.rechargeBtcData.subRemove(fvirtualcointype.getFid(), tradeNo) ;
														//CEC激活地址
														if(fvirtualcaptualoperation.getFvirtualcointype().getFid() == constantMap.getInt("cecId")){
															if(btcMessage.getACCESS_KEY()==null
																	||btcMessage.getIP()==null
																	||btcMessage.getPORT()==null
																	||btcMessage.getSECRET_KEY()==null){
																throw new Exception("rpc连接失败");
															}
															BTCUtils btcUtils2 = new BTCUtils(btcMessage);
															try{
																JSONObject json = btcUtils2.activeaddress(fvirtualcaptualoperation.getRecharge_virtual_address());
																System.out.println(json.toString());
															}catch (Exception e){
																throw new Exception(e.getMessage());
															}
														}
													}
												}catch(Exception e){
													e.printStackTrace() ;
												}
											}

										}else{
											this.rechargeBtcData.subRemove(fvirtualcointype.getFid(), tradeNo) ;
										}
									}else{
										this.rechargeBtcData.subRemove(fvirtualcointype.getFid(), tradeNo) ;
									}
					 			}

//								System.out.println("come loop over " + id);

							}catch(Exception e){
								e.printStackTrace() ;
							}

						}

				}catch (Exception e) {
					e.printStackTrace() ;
				}

		}


	}













	//加密
	private static final String KEY_ALGORITHM = "AES";
	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static Key toKey(byte[] key) throws Exception{
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}
	private static String encrypt(String data, String key) throws Exception{
		Key k = toKey(Base64.decodeBase64(key.getBytes()));                           //还原密钥
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);              //实例化Cipher对象，它用于完成实际的加密操作
		cipher.init(Cipher.ENCRYPT_MODE, k);                               //初始化Cipher对象，设置为加密模式
		return new String(Base64.encodeBase64(cipher.doFinal(data.getBytes()))); //执行加密操作。加密后的结果通常都会用Base64编码进行传输
	}
	private static String decrypt(String data, String key) throws Exception{
		Key k = toKey(Base64.decodeBase64(key.getBytes()));
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);                          //初始化Cipher对象，设置为解密模式
		return new String(cipher.doFinal(Base64.decodeBase64(data.getBytes()))); //执行解密操作
	}
}
