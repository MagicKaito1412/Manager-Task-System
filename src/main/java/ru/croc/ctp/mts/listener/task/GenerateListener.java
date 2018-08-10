package ru.croc.ctp.mts.listener.task;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ru.croc.ctp.jxfw.core.domain.DomainObject;
import ru.croc.ctp.jxfw.core.store.events.BeforeStoreEvent;
import ru.croc.ctp.jxfw.core.store.events.BeforeStoreEventListener;
import ru.croc.ctp.jxfw.core.store.events.DomainObjectStoreEvent;
import ru.croc.ctp.mts.domain.EditeTask;
import ru.croc.ctp.mts.domain.Project;
import ru.croc.ctp.mts.domain.Status;
import ru.croc.ctp.mts.domain.Task;
import ru.croc.ctp.mts.domain.TaskType;
import ru.croc.ctp.mts.domain.User;
import ru.croc.ctp.mts.domain.repo.EditeTaskRepository;
import ru.croc.ctp.mts.domain.repo.TaskRepository;
import ru.croc.ctp.mts.security.CustomUserDetails;

import java.util.Set;

/**
 * Class creates record if task changes.
 * 
 * @author team1
 *
 */
@Component
public class GenerateListener implements BeforeStoreEventListener {

	@Autowired
	TaskRepository taskRepository;

	/**
	 * Method generates count of tasks in project.
	 * 
	 * @param event
	 */
	@EventListener(condition = "#event.domainObject.new")
	public void onApplicationEventNew(DomainObjectStoreEvent<Task> event) {
		Task domainObject = event.getDomainObject();
		Integer count = domainObject.getProject().getTaskCounter() + 1;
		domainObject.getProject().setTaskCounter(count);
		domainObject.setNumberTaskInProject(domainObject.getProject().getCodeProject() + "-" + count);
	}

	/**
	 * Method generates last editor and last change of current task.
	 * 
	 * @param event
	 */
	@EventListener(condition = "#event.domainObject.new")
	public void onApplicationEventNewTaskStartline(DomainObjectStoreEvent<Task> event) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final CustomUserDetails principal = ((CustomUserDetails) authentication.getPrincipal());
		Task domainObject = event.getDomainObject();
		LocalDateTime lDT = LocalDateTime.now();
		domainObject.setStartline(lDT);
		domainObject.setChangedDate(lDT);
		domainObject.setAuthor(principal.getUser());
		domainObject.setLastEditor(principal.getUser());
		if (domainObject.getDeadline().isBefore(lDT))
			throw new IllegalArgumentException("Дата дедлайна должна быть позднее сегодняшнего дня");
	}

	/**
	 * Method creates record if task changes.
	 */
	@Override
	public void onApplicationEvent(BeforeStoreEvent event) {
		List<? extends DomainObject<?>> domainObjects = event.getStoreContext().getDomainObjects();

		domainObjects.forEach(domainObject -> {
			if (!domainObject.isNew() && (domainObject instanceof Task)) {
				Task task = (Task) domainObject;

				final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				final CustomUserDetails principal = ((CustomUserDetails) authentication.getPrincipal());
				@SuppressWarnings("unused")
				String author = principal.getUsername();
				task.setLastEditor(principal.getUser());

				if (!task.getStatus().equals(Status.CLOSED)) {
					Period period = Period.between(LocalDateTime.now().toLocalDate(),
							task.getStartline().toLocalDate());
					task.setWastedTime(period.getYears() + "y " + period.getMonths() + "m " + period.getDays() + "d");
				}

			}
		});

	}

	/**
	 * Method sets flag of changes.
	 * 
	 * @param event
	 */
	@EventListener(condition = "#event.domainObject.new")
	public void onApplicationEventControllRights(DomainObjectStoreEvent<Task> event) {
		Task task = event.getDomainObject();

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final CustomUserDetails principal = ((CustomUserDetails) authentication.getPrincipal());
		User cs = principal.getUser();
		if (!cs.getIsAdmin()) {
			task.setIsCheck(false);
		} else {
			task.setIsCheck(true);
		}
	}

	@Autowired
	EditeTaskRepository editeTaskRepository;

	/**
	 * Method creates record about changes.
	 * 
	 * @param event
	 */
	@EventListener(condition = "!#event.domainObject.new")
	public void onApplicationEventLookForUpdate(DomainObjectStoreEvent<Task> event) {
		EditeTask editeTask = new EditeTask();
		editeTask.setId(UUID.randomUUID().toString());
		Task task = event.getDomainObject();

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final CustomUserDetails principal = ((CustomUserDetails) authentication.getPrincipal());
		User cs = principal.getUser();
		if (cs.getIsAdmin()) {
			return;
		}

		Set<String> set = task.getDirtyAttributes();
		@SuppressWarnings("rawtypes")
		Iterator iterator1 = set.iterator();
		List<String> list = new ArrayList<>();
		while (iterator1.hasNext()) {
			list.add((String) iterator1.next());
		}

		if (list.size() == 1 && list.contains("status")) {
			return;
		}

		@SuppressWarnings("rawtypes")
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			String temp = (String) iterator.next();
			switch (temp) {
			case "description":
				editeTask.setDescription((String) task.getCurrentValue(temp).get(0));
				break;
			case "deadline":
				editeTask.setDeadline((LocalDateTime) task.getCurrentValue(temp).get(0));
				break;
			case "type":
				editeTask.setType((TaskType) task.getCurrentValue(temp).get(0));
				break;
			case "project":
				editeTask.setProject((Project) task.getCurrentValue(temp).get(0));
				break;
			case "taskPerformer":
				editeTask.setTaskPerformer((User) task.getCurrentValue(temp).get(0));
				break;
			}
		}
		editeTask.setTaskId(task.getId());
		editeTaskRepository.save(editeTask);
		if (!cs.getIsAdmin()) {
			task.setIsCheck(false);
			task.setStatus(Status.CLOSED);
		} else {
			task.setIsCheck(true);
		}
	}
}
