package com.example.agent

import com.example.agent.models.TaskSlot
import com.example.agent.models.TaskStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Manages parallel execution of multiple automation tasks.
 * Supports task queuing, prioritization, and concurrent pipelines.
 */
class MultitaskOrchestrator(
    private val automationEngine: AutomationEngine
) {
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    private val _taskSlots = MutableStateFlow<List<TaskSlot>>(emptyList())
    val taskSlots: StateFlow<List<TaskSlot>> = _taskSlots.asStateFlow()

    private val _activeTaskCount = MutableStateFlow(0)
    val activeTaskCount: StateFlow<Int> = _activeTaskCount.asStateFlow()

    private val _maxConcurrentTasks = 3
    private val taskJobs = mutableMapOf<String, Job>()
    private val taskQueue = mutableListOf<TaskSlot>()

    /**
     * Submit a new task for execution.
     * If under the concurrency limit, starts immediately.
     * Otherwise, queues it.
     */
    fun submitTask(name: String, command: String, priority: Int = 0) {
        val task = TaskSlot(
            id = "task_${System.currentTimeMillis()}_${(0..9999).random()}",
            name = name,
            command = command,
            status = TaskStatus.QUEUED,
            priority = priority
        )

        taskQueue.add(task)
        taskQueue.sortByDescending { it.priority }
        updateSlots()

        if (_activeTaskCount.value < _maxConcurrentTasks) {
            executeNextFromQueue()
        }
    }

    /**
     * Execute the next task from the queue.
     */
    private fun executeNextFromQueue() {
        val next = taskQueue.firstOrNull { it.status == TaskStatus.QUEUED } ?: return
        if (_activeTaskCount.value >= _maxConcurrentTasks) return

        // Update status to running
        updateTaskStatus(next.id, TaskStatus.RUNNING)
        _activeTaskCount.value++

        val job = scope.launch {
            try {
                // Run the automation engine for this task
                val steps = automationEngine.plannerAgent.planWorkflow(next.command)

                updateTaskStatus(next.id, TaskStatus.RUNNING, progress = 0f)

                for ((index, step) in steps.withIndex()) {
                    // Check if task was cancelled
                    val currentTask = _taskSlots.value.find { it.id == next.id }
                    if (currentTask?.status == TaskStatus.CANCELLED) return@launch

                    val progress = (index + 1).toFloat() / steps.size
                    updateTaskStatus(next.id, TaskStatus.RUNNING, progress = progress)
                    delay(1500) // Simulated step execution
                }

                updateTaskStatus(next.id, TaskStatus.COMPLETED, progress = 1.0f)
            } catch (e: Exception) {
                updateTaskStatus(next.id, TaskStatus.FAILED, error = e.message)
            } finally {
                _activeTaskCount.value--
                taskJobs.remove(next.id)
                // Start next queued task
                delay(500)
                executeNextFromQueue()
            }
        }

        taskJobs[next.id] = job
    }

    /**
     * Cancel a specific task by ID.
     */
    fun cancelTask(taskId: String) {
        taskJobs[taskId]?.cancel()
        taskJobs.remove(taskId)
        updateTaskStatus(taskId, TaskStatus.CANCELLED)
        _activeTaskCount.value = (_activeTaskCount.value - 1).coerceAtLeast(0)
        executeNextFromQueue()
    }

    /**
     * Cancel all running and queued tasks.
     */
    fun cancelAll() {
        taskJobs.values.forEach { it.cancel() }
        taskJobs.clear()
        _taskSlots.value = _taskSlots.value.map {
            if (it.status == TaskStatus.RUNNING || it.status == TaskStatus.QUEUED)
                it.copy(status = TaskStatus.CANCELLED)
            else it
        }
        _activeTaskCount.value = 0
        taskQueue.clear()
    }

    /**
     * Clear completed/cancelled/failed tasks from the list.
     */
    fun clearFinishedTasks() {
        _taskSlots.value = _taskSlots.value.filter {
            it.status == TaskStatus.RUNNING || it.status == TaskStatus.QUEUED
        }
    }

    // ── Internal Helpers ──────────────────────────────────────────
    private fun updateTaskStatus(
        taskId: String,
        status: TaskStatus,
        progress: Float? = null,
        error: String? = null
    ) {
        _taskSlots.value = _taskSlots.value.map { task ->
            if (task.id == taskId) task.copy(
                status = status,
                progress = progress ?: task.progress,
                error = error,
                completedAt = if (status == TaskStatus.COMPLETED || status == TaskStatus.FAILED)
                    System.currentTimeMillis() else null
            ) else task
        }
    }

    private fun updateSlots() {
        _taskSlots.value = taskQueue.toList()
    }
}
