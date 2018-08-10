package ru.croc.ctp.mts.listener.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.croc.ctp.jxfw.core.domain.DomainObject;
import ru.croc.ctp.jxfw.core.store.events.AfterStoreEvent;
import ru.croc.ctp.jxfw.core.store.events.AfterStoreEventListener;
import ru.croc.ctp.mts.domain.*;
import ru.croc.ctp.mts.domain.repo.EditeTaskRepository;
import ru.croc.ctp.mts.domain.repo.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class deletes task if it's changes didn't confirm.
 * 
 * @author team1
 *
 */
@Component
public class DeleteTaskListener implements AfterStoreEventListener {

	@Autowired
	TaskRepository taskRepository;
	@Autowired
	EditeTaskRepository editeTaskRepository;

	/**
	 * Method deletes task if it's changes didn't confirm.
	 */
	@Override
	public void onApplicationEvent(AfterStoreEvent event) {
		List<? extends DomainObject<?>> domainObjects = event.getStoreContext().getDomainObjects();

		domainObjects.forEach(domainObject -> {
			if (!domainObject.isNew() && (domainObject instanceof EditeTask)) {
				EditeTask editetask = (EditeTask) domainObject;
				Task task = taskRepository.findOne(editetask.getTaskId());
				if (editetask.getIsCheck()) {

					if (editetask.getDescription() != null && !editetask.getDescription().equals("")) {
						String temp = editetask.getDescription();
						task.setDescription(temp);
					}
					if (editetask.getDeadline() != null && !editetask.getDeadline().equals("")) {
						LocalDateTime temp = editetask.getDeadline();
						task.setDeadline(temp);
					}
					if (editetask.getType() != null && !editetask.getType().equals("")) {
						TaskType temp = editetask.getType();
						task.setType(temp);
					}
					if (editetask.getProject() != null && !editetask.getProject().equals("")) {
						Project temp = editetask.getProject();
						task.setProject(temp);
					}
					if (editetask.getTaskPerformer() != null && !(editetask.getTaskPerformer().equals(""))) {
						User temp = editetask.getTaskPerformer();
						task.setTaskPerformer(temp);
					}
					task.setIsCheck(true);
				} else {
					taskRepository.delete(taskRepository.findOne(editetask.getTaskId()));
					editeTaskRepository.delete(editeTaskRepository.findOne(editetask.getId()));
				}
			}
		});
	}
}
