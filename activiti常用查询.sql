#流程表
select * from act_hi_procinst where  proc_inst_id_=2501
#当前任务表
select * from act_ru_task  where proc_inst_id_=2501
#历史流程信息表
select * from act_hi_actinst where proc_inst_id_=2501
#历史任务表
select * from act_hi_taskinst where proc_inst_id_=2501
#参数存储表，开启流程、审批时存入的参数(没有结束的流程)
select * from act_ru_variable  where proc_inst_id_=2501
#参数存储表，开启流程、审批时存入的参数
select * from act_hi_varinst where proc_inst_id_=2501
#目前流程模板信息
SELECT * from act_re_procdef