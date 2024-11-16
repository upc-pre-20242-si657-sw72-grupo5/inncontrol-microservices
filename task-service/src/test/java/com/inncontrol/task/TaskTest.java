package com.inncontrol.task;

import com.inncontrol.task.domain.model.aggregates.Task;
import com.inncontrol.task.domain.model.valueobjects.EmployeeIdentifier;
import com.inncontrol.task.domain.model.valueobjects.TaskInformation;
import com.inncontrol.task.domain.model.valueobjects.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;
    private TaskInformation taskInformation;
    private EmployeeIdentifier employee;

    @BeforeEach
    void setUp() {
        taskInformation = new TaskInformation("Task Title", "Task Description");
        employee = new EmployeeIdentifier(123L);
        task = new Task(taskInformation, TaskStatus.SCHEDULED, employee, "employee@example.com");
        task.setDueDate(new Date(System.currentTimeMillis() + 86400000)); // 1 day in the future
    }

    // Scenario: Complete a task
    @Test
    void testComplete() {
        // Given: A task is in a scheduled state
        // When: The task is completed
        task.complete();
        // Then: The task status should be COMPLETED
        assertEquals(TaskStatus.COMPLETED, task.getStatus());
    }

    // Scenario: Start a task
    @Test
    void testStart() {
        // Given: A task is in a scheduled state
        // When: The task is started
        task.start();
        // Then: The task status should be IN_PROGRESS
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    // Scenario: Check if task is completed
    @Test
    void testIsCompleted() {
        // Given: A task is completed
        task.complete();
        // When: We check if the task is completed
        // Then: The task should be marked as completed
        assertTrue(task.isCompleted());
    }

    // Scenario: Check if task is in progress
    @Test
    void testIsInProgress() {
        // Given: A task is started
        task.start();
        // When: We check if the task is in progress
        // Then: The task should be marked as in progress
        assertTrue(task.isInProgress());
    }

    // Scenario: Check if task is expired (due date in the past)
    @Test
    void testIsExpired() {
        // Given: A task is scheduled with a past due date
        task.setDueDate(new Date(System.currentTimeMillis() - 86400000)); // 1 day in the past
        // When: We check if the task is expired
        // Then: The task should be marked as expired
        assertTrue(task.isExpired());
    }

    // Scenario: Get nice formatted due date
    @Test
    void testGetNiceDueDate() {
        // Given: A task has a due date
        String niceDueDate = task.getNiceDueDate();
        // When: We fetch the nice formatted due date
        // Then: The due date should not be null or empty
        assertNotNull(niceDueDate);
        assertFalse(niceDueDate.isEmpty());
    }

    // Scenario: Attempt to complete a task that is already completed
    @Test
    void testCannotCompleteAlreadyCompletedTask() {
        // Given: A task is already completed
        task.complete();
        // When: We attempt to complete the task again
        // Then: An IllegalStateException should be thrown
        assertThrows(IllegalStateException.class, task::complete);
    }

    // Scenario: Verify task cannot start after being completed
    @Test
    void testCannotStartCompletedTask() {
        // Given: A task is completed
        task.complete();
        // When: We attempt to start the completed task
        // Then: An IllegalStateException should be thrown
        assertThrows(IllegalStateException.class, task::start);
    }

    // Scenario: Ensure task is not expired when it’s due in the future
    @Test
    void testIsNotExpiredFuture() {
        // Given: A task is scheduled with a due date in the future
        task.setDueDate(new Date(System.currentTimeMillis() + 86400000)); // 1 day in the future
        // When: We check if the task is expired
        // Then: The task should not be expired
        assertFalse(task.isExpired());
    }
}
