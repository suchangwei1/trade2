package com.trade.service.admin;

import java.util.List;

import com.trade.dto.ArticleItemDTO;
import com.trade.model.Farticletype;
import com.trade.util.CollectionUtils;
import com.trade.util.HTMLSpirit;
import com.trade.dao.FarticleDAO;
import com.trade.model.Farticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
	@Autowired
	private FarticleDAO farticleDao;

	public Farticle findById(int id) {
		return this.farticleDao.findById(id);
	}

	public void saveObj(Farticle obj) {
		this.farticleDao.save(obj);
	}

	public void deleteObj(int id) {
		Farticle obj = this.farticleDao.findById(id);
		this.farticleDao.delete(obj);
	}

	public void updateObj(Farticle obj) {
		this.farticleDao.attachDirty(obj);
	}

	public List<Farticle> findByProperty(String name, Object value) {
		return this.farticleDao.findByProperty(name, value);
	}

	public List<Farticle> findAll() {
		return this.farticleDao.findAll();
	}

	public List<Farticle> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Farticle> all = this.farticleDao.list(firstResult, maxResults, filter,isFY);
		for (Farticle farticle : all) {
			farticle.getFarticletype().getFname();
			farticle.setFcontent(HTMLSpirit.delHTMLTag(farticle.getFcontent()));
			farticle.setEnContent(HTMLSpirit.delHTMLTag(farticle.getEnContent()));
			if(farticle.getFadminByFcreateAdmin() != null){
				farticle.getFadminByFcreateAdmin().getFname();
			}
			if(farticle.getFadminByFmodifyAdmin() != null){
				farticle.getFadminByFmodifyAdmin().getFname();
			}
		}
		return all;
	}

	public List<Farticle> findFarticle(int farticletype, int firstResult, int maxResult) {
		return this.farticleDao.findFarticle(farticletype, firstResult, maxResult);
	}

	public int findFarticleCount(int farticletype) {
		return this.farticleDao.findFarticleCount(farticletype);
	}

	public int findLastId(int curId, int type, int afterSize, int lang) {
		return this.farticleDao.findLastId(curId, type, afterSize, lang);
	}

	public List<ArticleItemDTO> findForMenu(int lastId, int type, int length ,int lang) {
		return this.farticleDao.findForMenu(lastId, type, length ,lang);
	}
}