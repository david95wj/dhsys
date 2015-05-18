package com.dhsys.web.persistent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.dhsys.web.entity.TRole;
import com.dhsys.web.entity.TUser;
 
@Repository
public class SysRoleDao extends IHibernateDao<TRole>{

	/**
	 * 获取角色列表
	 * @return
	 */
	public List<TRole> RoleListDao(){
		List<TRole> list = null;
		try{
           list = this.getHibernateDao().find("from TRole");   
			
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 加载角色信息
	 * @param roleId
	 * @return
	 */
	public TRole getRoleById(Integer roleId ){
		TRole role = new TRole();
		try{
		 role = this.get(roleId);	
			
		}catch(HibernateException e){
			e.printStackTrace();
		}
		return role;
	}
	
	/**
	 * 查询角色
	 * @param hql
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	 public List<TRole> findRolesDao(String hql, int currentPage, int pageSize) {
	    	List list = null;
			try {
				list = this.getHibernateDao().getSessionFactory().openSession()
						.createSQLQuery(hql)
						.setFirstResult((currentPage - 1) * pageSize)
						.setMaxResults(pageSize).list();
			} catch (HibernateException e) {
				
				e.printStackTrace();
			}

			return list;
	}
	 
	 /**
	  * 新增角色
	  * @param role
	  * @return
	  */
	 public boolean addRoleDao(TRole role){
		 boolean addFlag = false;
		 try{
			 this.getHibernateDao().save(role);
			 addFlag = true;
		 }catch(HibernateException e){
			 e.printStackTrace();
		 }
		return addFlag;
	 }
	 
	 /**
	  * 删除系统用户
	  * @param userId
	  * @return
	  */
	 public Map deleteRoleByIdDao(String roleId){
		 Map respMap = new HashMap();
		 try{
		   TRole role = this.getHibernateDao().get(TRole.class,Integer.valueOf(roleId));	 
		   if(!role.getModuleSet().isEmpty()&&role.getModuleSet().size() > 0){
			   respMap.put("success",false);
			   respMap.put("msg","角色删除失败");
			   respMap.put("reason","该角色下有模块信息");
		   }else if(!role.getUserSet().isEmpty()&&role.getUserSet().size() > 0){
			   respMap.put("success",false);
			   respMap.put("msg","角色删除失败");
			   respMap.put("reason","该角色下有用户信息");
		   }else{
			   this.getHibernateDao().delete(role);
			   respMap.put("success",true); 
			   respMap.put("msg","角色删除成功");
		   }
		 }catch(HibernateException e){
			 e.printStackTrace();
		 }
		return respMap;
	 }
	 
	 public boolean updateRoleDao(TRole r){
		 boolean flag = false;
		 try{
			 this.getHibernateDao().update(r);
		     flag = true;
		 }catch(HibernateException e){
			 e.printStackTrace();
		 }
		return flag;
	 }
	 
}
