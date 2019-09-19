import request from '@/utils/request'
import axios from 'axios'

export function loginByUsername(username, password) {
  const data = {
    username,
    password,
    grant_type: 'password'
  }
  
  // console.log('loginByUsername===', data);
  return request({
    url: '/api/oauth/oauth/token',
    method: 'post',
    data,
    transformRequest: [function(data) {
      // Do whatever you want to transform the data
      let ret = ''
      for (const it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}

export function getResource(access_token) {
  return request({
    url: '/portal-v2/custom-resource/all'   
  });
}

export function logout(access_token) {
  return request({
    url: '/api/oauth/oauth/tokens',
    method: 'delete',
    data: {
      access_token
    }
  });
}

export function getInfo(params) {
  // return request({
  //   url: '/admin/user/getLoginUserInfo',
  //   method: 'get',
  //   params
  // });
  let  response={};
  response.status=200;
  response.data={};
  response.data.name="管理员";

    
  return new Promise((resolve, reject) => {
    resolve(response);
   }); 
}


export function getAppGroup(id) {  
   return request({
        url: `/api/maxbase-portal-admin/portal/application-group-item/get-list-by-web-navigation-id`,
        params:{
            webNavigationId:id
        }
      })
}

export  function getMenus(appgroupdata) {
  let  webnavicati=[];
  let  isSuccess=[];
  let mennudata={data:[]};  
  
    webnavicati=appgroupdata;      
    webnavicati.forEach((item,index)=>{
      debugger
      let  tmpmenu={};
      tmpmenu.children=[];
      tmpmenu.code="";
      tmpmenu.description="";
      tmpmenu.enabled=true;
      tmpmenu.functionResourceEntity={};
      tmpmenu.icon=item.iconPath;
      tmpmenu.id=`group${index}`;
      tmpmenu.menuShortcut="";
      tmpmenu.name=item.applicationGroupItemName;
      tmpmenu.order=0;
      tmpmenu.pid=-1;
      tmpmenu.subsql="";
      tmpmenu.type=0; 
      mennudata.data.push(tmpmenu);
      isSuccess.push(false);  

    }); 
   
    webnavicati.forEach((item,index)=>{         
       GetApplictionMneu(item,index,mennudata,isSuccess);
    })  
  
  return  new Promise(function(resolve, reject){   
    setTimeout(() => {      
      isSuccess=isSuccess.map(r=>{r=true;return r;});      
    }, 15000);  

    let  time= setInterval(r=>{  
    if(isSuccess.every(r=> {return r==true})){
        clearInterval(time);
        resolve(mennudata); 
      }            
    },100);      
  });
 
}

 function   GetApplictionMneu(item,index,mennudata,isSuccess){    
  request({
    url: '/admin/permission/selectUserFunctionResources',
    method: 'get',
    headers:{
      'Application-Code':item.applicationCode
    }
  }).then(result=>{ 
    //目前只支持3级菜单    
    result.data.forEach((menuitem,menuindex)=>{ 
      console.log("menuitem",menuitem);
      if(menuitem){  
         let  sencendmneu={};
         sencendmneu.children=[];
         sencendmneu.functionResourceEntity={};
         sencendmneu.enabled=true;
         debugger
         sencendmneu.icon=menuitem.icon;
         sencendmneu.id=`${menuitem.id}`;
         sencendmneu.name=menuitem.name;
         sencendmneu.pid=`group${index}`;
         sencendmneu.type=0; 
          //menu.functionResourceEntity.pagePath=item.url+ menu.functionResourceEntity.routePath+'?iframe=true'; 
          //menu.functionResourceEntity.pagePath="http://172.16.33.123:701/#/serviceManager/swaggerServiceManager";
          mennudata.data[index].children.push(sencendmneu);
          mennudata.data[index].children[menuindex].children=[];
          console.log("3级",menuitem.children);
          if(menuitem.children){
            menuitem.children.forEach(grandsonmenu=>{
              console.log("ddd",grandsonmenu);
              debugger
              grandsonmenu.functionResourceEntity.pagePath=item.url+ grandsonmenu.functionResourceEntity.routePath+'?iframe=true'; 
              mennudata.data[index].children[menuindex].children.push(grandsonmenu);
            })            
          }    
      }      
    })
    console.log("ggg",mennudata);
    isSuccess[index]=true;   
  }).catch(error=>{
    isSuccess[index]=true;  
  });

}

export function getAllMenus() {
  return request({
    url: '/admin/menu-resource/all',
    method: 'get'
  })
}

export function getAuthories() {
  return request({
    url: '/admin/user/authorities',
    method: 'get'
  })
  
}