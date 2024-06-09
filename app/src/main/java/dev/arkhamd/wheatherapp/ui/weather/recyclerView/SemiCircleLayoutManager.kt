package dev.arkhamd.wheatherapp.ui.weather.recyclerView

import androidx.recyclerview.widget.RecyclerView
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SemiCircularLayoutManager : RecyclerView.LayoutManager() {

    private var scrollOffset = 0f
    private val angleStep = PI / 4
    private val speed = 0.001f // bigger = faster (def: 0.001)
    private val radius = 500f

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)

        if (itemCount == 0) return

        val startPos = (scrollOffset / (2 * PI)).toInt() % itemCount

        val centerX = width / 2 - 200
        val centerY = height / 2

        for (i in startPos until itemCount) {
            val view = recycler.getViewForPosition(i)
            addView(view)
            measureChildWithMargins(view, 0, 0)

            val width = getDecoratedMeasuredWidth(view)
            val height = getDecoratedMeasuredHeight(view)

            val angle = angleStep * (i - startPos) - PI / 2 + (scrollOffset % (2 * PI))
            val x = (centerX + radius * cos(angle)).toInt()
            val y = (centerY + radius * sin(angle)).toInt()

            layoutDecorated(view, x - width / 2, y - height / 2, x + width / 2, y + height / 2)
        }
    }

    override fun canScrollVertically(): Boolean = true

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val pendingScrollOffset = scrollOffset + dy * speed

        val lastItemIndex = itemCount - 1
        val isBorderBottom =
            borderBottom(
                lastItemIndex,
                pendingScrollOffset,
                getDecoratedMeasuredWidth(recycler.getViewForPosition(lastItemIndex))
            )
        if (dy < 0 && isBorderBottom) {
            return 0 // Prevent further scrolling in this direction
        }

        val thirdItemIndex = 2
        val isBorderTop =
            borderTop(
                thirdItemIndex,
                pendingScrollOffset,
                getDecoratedMeasuredHeight(recycler.getViewForPosition(thirdItemIndex))
            )
        if (dy > 0 && isBorderTop) {
            return 0 // Prevent further scrolling in this direction
        }

        scrollOffset = pendingScrollOffset
        onLayoutChildren(recycler, state)
        return dy
    }

    private fun borderBottom(index: Int, scrollOffset: Float, itemWidth: Int): Boolean {
        val itemAngle = angleStep * index - PI / 2 + (scrollOffset % (2 * PI))
        val itemX = (width / 2 + radius * cos(itemAngle)).toInt()

        return itemX - itemWidth / 2 >= width / 2
    }

    private fun borderTop(index: Int, scrollOffset: Float, itemHeight: Int): Boolean {
        val itemAngle = angleStep * index - PI / 2 + (scrollOffset % (2 * PI))
        val itemY = (height / 2 + radius * sin(itemAngle)).toInt()

        return itemY - itemHeight / 2 >= height / 2
    }
}