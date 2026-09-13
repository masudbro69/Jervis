package com.example.agent

import com.example.agent.models.AIThought
import com.example.agent.models.ActionType
import com.example.agent.models.AgentAction
import com.example.agent.models.ScreenFrame
import com.example.agent.models.WorkflowStep

class ReasoningAgent {

    fun determineNextAction(step: WorkflowStep, screen: ScreenFrame): Pair<AIThought, AgentAction> {
        val matchingNode = screen.nodes.find { node ->
            step.expectedTargetText?.let { target ->
                node.text.contains(target, ignoreCase = true) ||
                        (node.contentDescription?.contains(target, ignoreCase = true) == true)
            } ?: false
        } ?: screen.nodes.firstOrNull { it.isClickable }

        val thought = AIThought(
            stage = "Screen Observation & Reason",
            reasoning = "Target app '${step.appName}'. Goal '${step.title}'. Found element '${matchingNode?.text ?: step.expectedTargetText ?: "Primary Canvas"}' at bounds (${matchingNode?.bounds?.left ?: 50f}, ${matchingNode?.bounds?.top ?: 200f}). Confidence: 98.4%",
            confidence = 0.984f,
            detectedElements = screen.nodes.size
        )

        val action = AgentAction(
            type = step.actionType,
            targetNodeId = matchingNode?.id,
            xRatio = matchingNode?.bounds?.run { (left + right) / 2f / 400f } ?: 0.5f,
            yRatio = matchingNode?.bounds?.run { (top + bottom) / 2f / 700f } ?: 0.5f,
            inputText = step.inputText,
            appPackage = screen.packageName,
            description = "Execute ${step.actionType} on '${matchingNode?.text ?: step.title}'"
        )

        return Pair(thought, action)
    }
}
