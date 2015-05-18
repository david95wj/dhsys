<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">

$(function(){
	  $('#grantModuleForm').form({
 	     url : '${ctx}/dhsys/sysrole/grantModuleToRole.html',
			onSubmit : function() {
				//progressLoad();
				var isValid = $(this).form('validate');
	            //if (!isValid) {
	            	//$.messager.show({
						   //title:'系统提示',
						   //msg:'角色信息属性没有填写完整'
				   //});
				//}
				return isValid;
			},
			success : function(result) {
			     progressClose();
			     result = $.parseJSON(result);
			     
			    if (result.success) {
			    	parent.$.messager.alert('系统提示', result.msg, 'info');
			    	parent.$.modalDialog.openner_dataGrid.datagrid('reload'); 
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('系统提示', result.msg, 'error');
				}
			}
		});
		
		
		//加载应用模块列表
		$('#module').combotree({
			url : '${ctx}/dhsys/module/moduleList.html',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto',
			multiple : true,
			required: false,
			cascadeCheck : false,
			width: 150,
			value : $.stringToList('${moduleIds}')
		});
		
		
		
   });
</script>
<div class="easyui-layout" data-options="fit:true,border:false" >
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;" >
			<form id="grantModuleForm" method="post">
				<table id="grant" cellspacing="13" cellpadding="4" class="grid">
					 <tr>
					    <input type="hidden" name="roleId" value="${role.roleId}"/>
					    <td>角色名称:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="role.roleName" class="easyui-textbox"  value="${role.roleName}"  disabled="disabled"/></td>   
					    <td>模块列表:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="module" name="moduleIds"/></td>
					 </tr>
				 </table>
			</form>
		</div>
	</div>