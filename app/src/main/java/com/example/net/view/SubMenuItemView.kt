package com.example.net.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.widget.ImageView
import coil.load
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.net.databinding.SubMenuItemViewBinding
import com.example.net.entity.SubMenuEntity
import com.example.net.extentions.backgroundModifier
import com.example.net.extentions.toDp
import com.example.net.R

class SubMenuItemView(context: Context) : ConstraintLayout(context) {

    private var viewBinding: SubMenuItemViewBinding? = null

    init {
        SubMenuItemViewBinding.inflate(LayoutInflater.from(context), this, true)
            .apply { viewBinding = this }
    }

    fun initSubMenu(
        itemSubMenu: SubMenuEntity,
        endOfSection: Boolean = false
    ) {
        viewBinding?.run {
//            val iconUrl = 0 //todo change this back
            val arrowIcon = R.drawable.arrow
            mainConstraintLayout.contentDescription = itemSubMenu.tag
//            iconImageView.setIcon(active = itemSubMenu.active, drawableId = iconUrl) //todo change this back
            arrowImage.setIcon(active = itemSubMenu.active, drawableId = arrowIcon)
            mainConstraintLayout.backgroundModifier(
                cornerRadius = 16f,
                backgroundColor = getRightColor(
                    active = itemSubMenu.active,
                    color = "#F6F6F6"
                )
            )
            menuText.text = itemSubMenu.text
            if (endOfSection) {
                val menuItemContainer =
                    viewBinding?.mainConstraintLayout?.layoutParams as LayoutParams
                menuItemContainer.setMargins(
                    16.toDp(resources.displayMetrics.density),
                    0.toDp(resources.displayMetrics.density),
                    16.toDp(resources.displayMetrics.density),
                    38.toDp(resources.displayMetrics.density)
                )
                viewBinding?.mainConstraintLayout?.layoutParams = menuItemContainer
            }
        }
    }

    private fun ImageView.setIcon(active: String?, drawableId: Int) {
        if (active == "0") imageAlpha = 77
        setImageResource(drawableId)
    }

    private fun getRightColor(active: String?, color: String?): String? {
        return if (active == "0") "#4D" + color?.replace(
            "#",
            ""
        ) else color
    }

    fun initSetOnClick(
        itemSubMenu: SubMenuEntity,
        callback: (String?, String?, String?, String?) -> Unit
    ) {
        viewBinding?.root?.setOnClickListener {
            callback(itemSubMenu.tag, itemSubMenu.ifEmpty, itemSubMenu.active, itemSubMenu.text)
        }
    }
}