package com.example.agent

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.agent.models.UINode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class JarvisAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        _isServiceActive.value = true
        captureCurrentWindow()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            val pkg = it.packageName?.toString() ?: ""
            if (pkg.isNotBlank()) {
                _currentPackage.value = pkg
            }
            captureCurrentWindow()
        }
    }

    override fun onInterrupt() {
        _isServiceActive.value = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (instance == this) {
            instance = null
        }
        _isServiceActive.value = false
    }

    fun captureCurrentWindow() {
        rootInActiveWindow?.let { root ->
            val nodes = mutableListOf<UINode>()
            parseNodeTree(root, nodes)
            _capturedNodes.value = nodes
        }
    }

    private fun parseNodeTree(node: AccessibilityNodeInfo, list: MutableList<UINode>) {
        val bounds = Rect()
        node.getBoundsInScreen(bounds)
        val text = node.text?.toString() ?: node.contentDescription?.toString() ?: ""
        if (text.isNotBlank() || node.isClickable || node.isEditable || node.isScrollable) {
            val androidxRect = androidx.compose.ui.geometry.Rect(
                bounds.left.toFloat(),
                bounds.top.toFloat(),
                bounds.right.toFloat(),
                bounds.bottom.toFloat()
            )
            list.add(
                UINode(
                    id = node.viewIdResourceName ?: "node_${list.size}",
                    text = text,
                    contentDescription = node.contentDescription?.toString(),
                    className = node.className?.toString() ?: "android.view.View",
                    bounds = androidxRect,
                    isClickable = node.isClickable,
                    isEditable = node.isEditable,
                    isScrollable = node.isScrollable,
                    packageName = node.packageName?.toString() ?: ""
                )
            )
        }
        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                parseNodeTree(child, list)
                child.recycle()
            }
        }
    }

    fun performTapAt(x: Float, y: Float): Boolean {
        val path = Path().apply { moveTo(x, y) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 100))
            .build()
        return dispatchGesture(gesture, null, null)
    }

    fun performSwipe(startX: Float, startY: Float, endX: Float, endY: Float, duration: Long = 300): Boolean {
        val path = Path().apply {
            moveTo(startX, startY)
            lineTo(endX, endY)
        }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, duration))
            .build()
        return dispatchGesture(gesture, null, null)
    }

    fun performScrollForward(): Boolean {
        return rootInActiveWindow?.findAccessibilityNodeInfosByText("")?.firstOrNull { it.isScrollable }
            ?.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD) ?: false
    }

    fun performGlobalBackAction(): Boolean {
        return performGlobalAction(GLOBAL_ACTION_BACK)
    }

    fun performGlobalHomeAction(): Boolean {
        return performGlobalAction(GLOBAL_ACTION_HOME)
    }

    fun performGlobalRecentsAction(): Boolean {
        return performGlobalAction(GLOBAL_ACTION_RECENTS)
    }

    fun inputTextToFocusedNode(text: String): Boolean {
        val arguments = Bundle().apply {
            putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text)
        }
        return rootInActiveWindow?.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
            ?.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments) ?: false
    }

    companion object {
        private val _isServiceActive = MutableStateFlow(false)
        val isServiceActive: StateFlow<Boolean> = _isServiceActive.asStateFlow()

        private val _currentPackage = MutableStateFlow("com.example")
        val currentPackage: StateFlow<String> = _currentPackage.asStateFlow()

        private val _capturedNodes = MutableStateFlow<List<UINode>>(emptyList())
        val capturedNodes: StateFlow<List<UINode>> = _capturedNodes.asStateFlow()

        var instance: JarvisAccessibilityService? = null
            private set
    }
}

