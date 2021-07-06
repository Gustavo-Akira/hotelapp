package br.gustavoakira.hotel.common

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import br.gustavoakira.hotel.R

class AboutDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = DialogInterface.OnClickListener{_,i->
            if (i == DialogInterface.BUTTON_NEGATIVE){
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/gustavo-akira-uekita/"))
                startActivity(intent)
            }
        }
        return AlertDialog.Builder(requireContext())
            .setTitle("About")
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton("about", listener)
            .create()
    }
}