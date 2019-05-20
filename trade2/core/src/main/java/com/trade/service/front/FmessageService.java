package com.trade.service.front;

import com.trade.dao.FmessageDAO;
import com.trade.model.Fmessage;
import com.trade.model.Fuser;
import com.trade.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FmessageService extends BaseService {
    @Autowired
    private FmessageDAO fmessageDAO;

    public void saveMessage(Fmessage fmessage){
        fmessageDAO.save(fmessage);
    }

    public Fmessage findByUser(Fuser fuser){
        return fmessageDAO.findByUser(fuser);
    }

    public void deleteById(int id, int uid){
        fmessageDAO.deleteById(id, uid);
    }

    public void updateMarkMessage(int id, int uid){
        fmessageDAO.readMessage(id, uid);
    }

}
