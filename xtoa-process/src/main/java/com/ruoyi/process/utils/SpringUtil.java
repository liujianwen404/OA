package com.ruoyi.process.utils;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.process.todoitem.domain.BizTodoItem;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringUtil  implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }


    public static String getHandleNmae(String instanceId, Integer auditStatus) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (StringUtils.isNotEmpty(instanceId)){
            if (auditStatus > 1 ){
                /*IBizTodoItemService bizTodoItemService = getBean(IBizTodoItemService.class);
                String name = bizTodoItemService.getEndHandleName(instanceId);
                stringBuilder.append(StringUtils.isNotEmpty(name) ? name : "");*/
                return "-";
            }else {
                IBizTodoItemService bizTodoItemService = getBean(IBizTodoItemService.class);
                TaskService taskService = getBean(TaskService.class);
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(instanceId)
                        .list();
                for (Task task : taskList) {
                    List<BizTodoItem> bizTodoItems = bizTodoItemService.selectByTaskId(task.getId());
                    for (BizTodoItem bizTodoItem : bizTodoItems) {
                        stringBuilder.append(bizTodoItem.getTodoUserName() + " - ");
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

}