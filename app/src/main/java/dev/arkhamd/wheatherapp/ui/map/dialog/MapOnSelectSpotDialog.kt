package dev.arkhamd.wheatherapp.ui.map.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import dev.arkhamd.wheatherapp.ui.WeatherActivity

class MapOnSelectSpotDialog: DialogFragment() {

    companion object {
        fun newInstance(cords: FloatArray): MapOnSelectSpotDialog {
            val fragment = MapOnSelectSpotDialog()
            val args = Bundle()
            args.putFloatArray("cords", cords)
            fragment.arguments = args

            return fragment
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val data = arguments?.getFloatArray("cords")

        val builder = AlertDialog.Builder(requireActivity())

        builder
            .setTitle("Выбор места")
            .setMessage("Вы уверены?")
            .setPositiveButton("Верно!") { _, _ ->
                val intent = Intent(requireContext(), WeatherActivity::class.java)
                intent.putExtra("cords", data)
                startActivity(intent)
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.cancel()
            }

        val alertDialog = builder.create()

        // window to bottom of screen
        val window = alertDialog.window
        val layoutParams = window?.attributes
        layoutParams?.gravity = Gravity.BOTTOM
        window?.attributes = layoutParams
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        return alertDialog
    }
}