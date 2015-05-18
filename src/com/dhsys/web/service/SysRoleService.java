package com.dhsys.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
 

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhsys.web.entity.TModule;
import com.dhsys.web.entity.TRole;
import com.dhsys.web.entity.TUser;
import com.dhsys.web.pagemodel.P_SysRole;
import com.dhsys.web.pagemodel.P_SysUser;
import com.dhsys.web.pagemodel.P_Tree;
import com.dhsys.web.persistent.SysModuleDao;
import com.dhsys.web.persistent.SysRoleDao;
import com.dhsys.web.utils.GlobalConstant;

/**
 * 系统角色服务层
 * @author wangyuxiang
 *
 */
@Service
public class SysRoleService {

	@Autowired
	private SysRoleDao sysRoleDao;
	
	@Autowired
	private SysModuleDao moduleDao;
	
	/**
	 * 获取角色列表
	 * @return
	 */
	public List<TRole> roleListService(){
		List treeList = new ArrayList();
		try{
		  List list = sysRoleDao.RoleListDao();
		  if(list.size() > 0){
			  P_Tree tree = null;
			  for(int i=0;i<list.size();i++){
				  tree = new P_Tree();
				  TRole r = (TRole) list.get(i);
				  tree.setId(r.getRoleId());
				  tree.setText(r.getRoleName());
				  treeList.add(tree);
			  }
		  }
		}catch(Exception e){
			e.printStackTrace();
		}
		return treeList;
	}
	
	/**
	 * 查询系统角色 支持分页查询
	 * @param page
	 * @param rows
	 * @param role
	 * @return
	 */
	public List<TRole> findSysRoleService(String page, String rows, P_SysRole role){
		List roleList = new ArrayList();
		List pageList = new ArrayList();
		try{
		  String sql = "select r.roleId,r.roleName,r.createDate,r.roleBak from DH_SYSROLE r where 1 = 1";	
		  if(role!=null){
			  if(role.getRoleName()!=null&&!role.getRoleName().equals("")){
				  sql += " and r.roleName = '" + role.getRoleName() + "'"; 
			  }
			  if(role.getCreateDate()!=null&&!role.getCreateDate().equals("")){
				  sql += " and r.createDate = '" + role.getCreateDate() + "'";
			  }
			  if(role.getRoleBak()!=null&&!role.getRoleBak().equals("")){
				  sql += " and r.roleBak = like '%" + role.getRoleBak() + "%'";
			  }
			   
			  sql += " order by r.seq";
			  int currentPage = Integer
						.parseInt((page == null || page == "0") ? "1" : page); 
			  int pageSize = Integer
						.parseInt((rows == null || rows == "0") ? "10" : rows);
			  roleList = sysRoleDao.findRolesDao(sql, currentPage, pageSize);
			  if(roleList.size() > 0){
				  P_SysRole p_role = null;
				  for(int i=0;i<roleList.size();i++){
					  p_role = new P_SysRole();
					  Object[] object = (Object[]) roleList.get(i);
					  p_role.setRoleId(Integer.valueOf(object[0].toString()));
					  p_role.setRoleName(object[1]==null?"":object[1].toString());
					  p_role.setCreateDate(object[2]==null?"":object[2].toString());
					  p_role.setRoleBak(object[3]==null?"":object[3].toString());
					  pageList.add(p_role);
				  }
			  }
		  }
		}catch(Exception e){
			e.printStackTrace();
		}
		return pageList;
		
	 }
	
	/**
	 * 获取应用模块列表
	 * @return
	 */
	public List getModuleListService(){
		return moduleDao.getModuleTreeDao(GlobalConstant.ADMIN);
	}
	
	/**
	 * 新增系统角色
	 * @param role
	 * @return
	 */
	public boolean addRoleService(P_SysRole role){
		TRole tRole = new TRole();
		
		tRole.setCreateDate(role.getCreateDate());
		tRole.setRoleBak(role.getRoleBak());
		tRole.setRoleName(role.getRoleName());
		tRole.setSeq(role.getSeq());
		
		return sysRoleDao.addRoleDao(tRole);
	}
	
	
	/**
	 * 删除系统角色
	 * @param roleId
	 * @return 删除标识,若删除失败需要说明原因
	 */
	public Map deleteRoleByIdService(String roleId){
	    Map respMap = new HashMap();
	    respMap = sysRoleDao.deleteRoleByIdDao(roleId);
		 
		return respMap;
	}
	
	/**
	 * 加载系统用户
	 * @return
	 */
	public TRole getRoleByIdService(Integer roleId){
		TRole r = null; 
		try{
		 r = sysRoleDao.getRoleById(roleId);
		 }catch(Exception e){
			e.printStackTrace();
		}
		return r;
	}
	
	public boolean updateRoleService(TRole role){
		 
		return sysRoleDao.updateRoleDao(role);
	}
	
	
	public boolean grantModuleService(P_SysRole role){
		boolean grantFlag = false;
		TRole t = sysRoleDao.getRoleById(role.getRoleId());
		if ((role.getModuleIds() != null) && !role.getModuleIds().equalsIgnoreCase("")) {
			String ids = "";
			boolean b = false;
			for (String id : role.getModuleIds().split(",")) {
				if (b) {
					ids += ",";
				} else {
					b = true;
				}
				ids += id;
			}
			t.setModuleSet((new HashSet<TModule>(moduleDao.getHibernateDao().find("select distinct t from TModule t where t.moduleId in ("
					+ ids + ")"))));
			grantFlag = true;
		}else {
			t.setModuleSet(null);
		}
		return grantFlag;
	}
}
