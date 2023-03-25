package com.javahere.reachyourgoal.mapper;

import com.javahere.reachyourgoal.dao.TaskResponse;
import com.javahere.reachyourgoal.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {
    TaskResponse toTaskResponse(Task task);

    Task toTask(TaskResponse taskResponse);
}
