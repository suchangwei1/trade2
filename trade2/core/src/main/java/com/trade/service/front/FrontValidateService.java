package com.trade.service.front;

import com.trade.Enum.CountLimitTypeEnum;
import com.trade.dao.FcountlimitDAO;
import com.trade.dao.FvalidateemailDAO;
import com.trade.dao.FvalidatemessageDAO;
import com.trade.model.Fcountlimit;
import com.trade.model.Fvalidateemail;
import com.trade.model.Fvalidatemessage;
import com.trade.mq.MessageQueueService;
import com.trade.service.BaseService;
import com.trade.util.Constants;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontValidateService extends BaseService{
	@Autowired
	protected MessageQueueService messageQueueService ;
	@Autowired
	protected FvalidateemailDAO validateemailsDAO ;
	@Autowired
	private FcountlimitDAO fcountlimitDAO ;
	@Autowired
	private FvalidatemessageDAO fvalidatemessageDAO ;
	

	
	//是否可以重试
	public int getLimitCount(String ip,int type){
		int maxLimit = Constants.ErrorCountLimit ;
		if(type==CountLimitTypeEnum.AdminLogin){
			maxLimit = Constants.ErrorCountAdminLimit ;
		}
		
		List<Fcountlimit> list = this.fcountlimitDAO.findByIpType(ip, type) ;
		if(list.size()==0){
			return maxLimit ;
		}else{
			Fcountlimit fcountlimit = list.get(0) ;
			if(Utils.getTimestamp().getTime()- fcountlimit.getFcreateTime().getTime()<2L*60*60*1000){
				return maxLimit - fcountlimit.getFcount() ;
			}else{
				return maxLimit ;
			}
		}
	}
	
	//记录一次错误记录
	public void updateLimitCount(String ip,int type){
		if(Constants.closeLimit){return;}
		List<Fcountlimit> list = this.fcountlimitDAO.findByIpType(ip, type) ;
		if(list.size()==0){
			Fcountlimit fcountlimit = new Fcountlimit() ;
			fcountlimit.setFcount(1) ;
			fcountlimit.setFcreateTime(Utils.getTimestamp()) ;
			fcountlimit.setFip(ip) ;
			fcountlimit.setFtype(type) ;
			this.fcountlimitDAO.save(fcountlimit) ;
		}else{
			Fcountlimit fcountlimit = list.get(0) ;
			if(Utils.getTimestamp().getTime()- fcountlimit.getFcreateTime().getTime()<2*60*60*1000L){
				fcountlimit.setFcount(fcountlimit.getFcount()+1) ;
				fcountlimit.setFcreateTime(Utils.getTimestamp()) ;
				this.fcountlimitDAO.attachDirty(fcountlimit) ;
			}else{
				fcountlimit.setFcount(1) ;
				fcountlimit.setFcreateTime(Utils.getTimestamp()) ;
				this.fcountlimitDAO.attachDirty(fcountlimit) ;
			}
		}
	}
	
	public void deleteCountLimite(String ip,int type){
		List<Fcountlimit> list = this.fcountlimitDAO.findByIpType(ip, type) ;
		for(int i=0;i<list.size();i++){
			this.fcountlimitDAO.delete(list.get(i)) ;
		}
	}
	
	public void addFvalidateMessage(Fvalidatemessage fvalidatemessage){
		this.fvalidatemessageDAO.save(fvalidatemessage) ;
	}
	
	public void addFvalidateemail(Fvalidateemail fvalidateemail){
		this.validateemailsDAO.save(fvalidateemail);
	}

}
