package com.dhsys.web.servlet;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhsys.web.entity.TModule;
import com.dhsys.web.entity.TRole;
 
import com.dhsys.web.pagemodel.P_SysRole;
 
import com.dhsys.web.service.ModuleService;
import com.dhsys.web.service.SysRoleService;
 
import com.dhsys.web.utils.CommonsUtils;
import com.dhsys.web.utils.RespJson;
import com.dhsys.web.utils.SessionInfo;
 
 
 
 
 

@RequestMapping("/dhsys/sysrole")
@Controller
public class SysRoleServlet {
    
	 @Autowired
	 private SysRoleService sysRoleService;
	 
	 @Autowired
	 private ModuleService moduleService;
	 
	 private String moduleIds = ""; 
	 /**
	  * 获取角色信息列表
	  * @return
	  */
	 @RequestMapping("/roleList.html")
	 @ResponseBody
	 public List<TRole> roleList(){
	     List list = null;
		 try{
			 list = sysRoleService.roleListService();
		 }catch(HibernateException e){
			 e.printStackTrace();
		 }
		return list;
	 }
	 
	 /**
	  * 查询系统角色
	  * @param req
	  * @param role
	  * @return
	  */
	 @RequestMapping("/findSysRoles.html")
	 @ResponseBody
	 public List<TRole> findSysRoles(HttpServletRequest req,P_SysRole role){
		 List roleList = null;
		 try{
			 String page = req.getParameter("page");
			 String rows = req.getParameter("rows");
			 roleList = sysRoleService.findSysRoleService(page, rows, role); 
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		return roleList;
	 }
	 
	   /**
		 * 系统角色页面初始化
		 * @return
		 */
		@RequestMapping("/initSysRole.html")
	    public String initSysRole(){
			
		   return "sys/sysrole/role";
			
		}
		
		/**
		 * 新增初始化
		 * @param req
		 * @return
		 */
		@RequestMapping("/addInit.html")
		public String addInit(HttpServletRequest req){
			 String nowDate = CommonsUtils.getNowDate(new Date());
			 req.setAttribute("nowDate", nowDate);
			 return "sys/sysrole/addrole";
		   	
		}
		
		/**
		 * 新增系统角色
		 * @param role
		 * @return
		 */
		@RequestMapping("/addrole.html")
		@ResponseBody
		public Map addRole(P_SysRole role){
			Map respMap = new HashMap();
			try{
			  boolean flag = sysRoleService.addRoleService(role);	
			  if(flag){
				  respMap.put("msg","新增角色成功");
				  respMap.put("success",true);
			  }else{
				  respMap.put("msg","新增角色失败");
				  respMap.put("success",false);
			  }
			}catch(Exception e){
				e.printStackTrace();
			}
			return respMap;
		}
		
		/**
		 * 获取应用模块列表
		 * @return
		 */
		@RequestMapping("/moduleList.html")
		@ResponseBody
		public List moduleList(){
			
			List moduleList = sysRoleService.getModuleListService();
			return moduleList;
			
		}
		
		/**
		 * 删除系统角色
		 * @param req
		 * @return 
		 */
		@RequestMapping("/deleteRoleById.html")
		@ResponseBody
		public Map deleteRoleById(HttpServletRequest req){
			Map respMap = new HashMap();
			try{
		      String roleId = req.getParameter("roleId");
		      respMap = sysRoleService.deleteRoleByIdService(roleId);
		     }catch(Exception e){
		    	e.printStackTrace();
		    }
			  return respMap;
		 }
		
		@RequestMapping("/updateInit.html")
		public String updateInit(HttpServletRequest req){
			 
			try{
			  String roleId = req.getParameter("roleId");	
			  TRole role = sysRoleService.getRoleByIdService(Integer.valueOf(roleId));
			  req.setAttribute("role",role);
			}catch(Exception e){
				e.printStackTrace();
			}
			return "sys/sysrole/editrole";
		}
		
		/**
		 * 修改用户
		 *  
		 * @return
		 */
		@RequestMapping("/updaterole.html")
		@ResponseBody
		public Map updateRole(P_SysRole role){
			Map respMap = new HashMap();
			try{
				TRole r = sysRoleService.getRoleByIdService(role.getRoleId());
			    BeanUtils.copyProperties(role, r) ;
				boolean updateFlag = sysRoleService.updateRoleService(r);
			    if(updateFlag){
			    	respMap.put("msg","系统角色变更成功")	;
			        respMap.put("success",true);
			    }else{
			    	respMap.put("msg","系统角色变更失败")	;
			        respMap.put("success",false);
			    }
			}catch(Exception e){
				e.printStackTrace();
			}
			return respMap;
		}
	
	@RequestMapping("/grantModule.html")
	public String grantModule(HttpServletRequest req){
		
		String roleId = req.getParameter("roleId");
	    try{
	    	TRole role = sysRoleService.getRoleByIdService(Integer.valueOf(roleId));
		    Set<TModule> set = role.getModuleSet();
		    if(set!= null && set.size() > 0){
		    	for(TModule rs :set){
		    		moduleIds += rs.getModuleId().toString() + ",";
		    	}
		    	moduleIds = moduleIds.substring(0, moduleIds.length() - 1);
		    }
		    req.setAttribute("role",role);
		    req.setAttribute("moduleIds",moduleIds);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return "/sys/sysrole/grantmodule";	
	}
	
	@RequestMapping("/grantModuleToRole.html")
	@ResponseBody
	public Map grantModuleToRole(P_SysRole role){
		boolean flag = false;
		Map respMap = new HashMap();
		System.out.println("-------------->>" + role.getModuleIds());
		if(role.getModuleIds() != null && !"".equals(role.getModuleIds())){
			List<TModule> moduleList = new ArrayList<TModule>();
			String rds[] = role.getModuleIds().split(",");
			for(int i=0;i<rds.length;i++){
				TModule m = moduleService.getModuleByIdService(Integer.valueOf(rds[i].trim()));
				moduleList.add(m);
			}
			TRole grantRole = sysRoleService.getRoleByIdService(role.getRoleId());
			grantRole.setModuleSet(new HashSet<TModule>(moduleList));
			 
			flag = sysRoleService.updateRoleService(grantRole);
		}
		if (flag) {
			respMap.put("msg","应用模块分配成功")	;
	        respMap.put("success",true); 
			 
		} else {
			respMap.put("msg","应用模块分配失败")	;
	        respMap.put("success",false);
		}
		return respMap;
	}
}
