/*fx */
/*第1步：创建临时表空间  */
create temporary tablespace fx_temp
tempfile 'e:\oradata\fx_temp.dbf' 
size 1000m  
autoextend on  
next 500m maxsize 3000m  
extent management local;  
 
/*第2步：创建数据表空间  */
create tablespace fx_data  
logging  
datafile 'e:\oradata\fx_data.dbf' 
size 5000m  
autoextend on  
next 500m maxsize 30000m  
extent management local;  
 
 
/*第3步：创建用户并指定表空间  */
create user fx identified by fx
default tablespace fx_data  
temporary tablespace fx_temp;
 
/*第4步：给用户授予权限  */
grant connect,resource,dba to fx