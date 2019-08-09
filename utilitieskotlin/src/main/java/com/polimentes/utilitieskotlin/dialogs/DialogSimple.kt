package com.polimentes.utilitieskotlin.dialogs

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.polimentes.utilitieskotlin.Constants
import com.polimentes.utilitieskotlin.enums.PerformBy

/**
 * Clase utilizada para mostrar un dialogo en pantalla con opción de continuar y cancelar
 * recibe como parametro
 * @param xmlView : identificador del xml a mostrar
 * @param title: Any el titulo a mostrar
 * @param body: Any el cuerpo a mostrar
 * @param image: Int el cuerpo a mostrar
 */
open class DialogSimple : DialogFragment(), IDialogView {
    /**
     *
     */
    private var dismissBy: PerformBy = PerformBy.USER

    protected val listener: IDialogListener.Simple? by lazy {
        when {
            targetFragment is IDialogListener.Simple -> targetFragment as IDialogListener.Simple
            activity is IDialogListener.Simple -> activity as IDialogListener.Simple
            parentFragment is IDialogListener.Simple -> parentFragment as IDialogListener.Simple
            childFragmentManager is IDialogListener -> childFragmentManager as IDialogListener.Simple
            else -> null
        }
    }

    companion object {
        fun create(xmlView: Int, title: Any, body: Any, image: Any?, ids: HashMap<String, Int>): DialogFragment {
            val dialog: DialogSimple = DialogSimple()
            val arguments: Bundle = Bundle()
            when (title) {
                is Int -> arguments.putInt(Constants.DIALOG_KEY_TITLE, title)
                is String -> arguments.putString(Constants.DIALOG_KEY_TITLE, title)
            }
            when (body) {
                is Int -> arguments.putInt(Constants.DIALOG_KEY_BODY, body)
                is String -> arguments.putString(Constants.DIALOG_KEY_BODY, body)
            }
            arguments.putInt(Constants.DIALOG_KEY_XML, xmlView)
            arguments.putSerializable(Constants.DIALOG_KEY_IDS, ids)
            when (image) {
                is Int -> arguments.putInt(Constants.DIALOG_KEY_IMAGE, image)
                is String -> arguments.putString(Constants.DIALOG_KEY_IMAGE, image)
            }
            dialog.arguments = arguments
            return dialog
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val xmlView: Int? = arguments?.getInt(Constants.DIALOG_KEY_XML)
        return if (xmlView != null) inflater.inflate(xmlView, container, false) else super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        setListeners()
    }

    override fun setData() {
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val title: Any? = arguments?.get(Constants.DIALOG_KEY_TITLE)
        val body: Any? = arguments?.get(Constants.DIALOG_KEY_BODY)
        val image: Int? = arguments?.getInt(Constants.DIALOG_KEY_IMAGE)
        title?.let { setTitle(it) }

        body?.let { setBody(it) }

    }

    override fun setListeners() {
        val ids: HashMap<String, Int> = arguments?.getSerializable(Constants.DIALOG_KEY_IDS) as HashMap<String, Int>
        if (ids.containsKey(Constants.DIALOG_ID_CANCEL)) {
            val idCancel: Int = ids[Constants.DIALOG_ID_CANCEL]!!
            val cancelOption: View? = view?.findViewById(idCancel)
            cancelOption?.setOnClickListener {
                dismissBy = PerformBy.SYSTEM
                listener?.onDialogCanceled(tag!!)
            }
        }
        Log.d("listener del dialogo",""+ listener)

        if (ids.containsKey(Constants.DIALOG_ID_ACCEPT)) {
            val idConfirm: Int = ids[Constants.DIALOG_ID_ACCEPT]!!
            Log.d("listener del dialogo",""+ listener)
            val confirmOption: View? = view?.findViewById(idConfirm)
            confirmOption?.setOnClickListener {
                dismissBy = PerformBy.SYSTEM
                listener?.onAcceptClickListener(tag!!)
            }
        }
    }

    override fun hideDialog(performBy: PerformBy) {
        this.dismissBy = performBy
        dismiss()
    }

    override fun setBody(body: Any) {
        val ids: HashMap<String, Int> = arguments?.getSerializable(Constants.DIALOG_KEY_IDS) as HashMap<String, Int>
        val idView: Int = ids[Constants.DIALOG_ID_BODY]!!
        val tvBody: TextView? = view?.findViewById(idView)
        when (body) {
            is Int -> tvBody?.setText(body)
            is String -> tvBody?.text = body
        }
    }

    override fun setTitle(title: Any) {
        val ids: HashMap<String, Int> = arguments?.getSerializable(Constants.DIALOG_KEY_IDS) as HashMap<String, Int>
        val idView: Int = ids[Constants.DIALOG_ID_TITLE]!!
        val tvBody: TextView? = view?.findViewById(idView)
        when (title) {
            is Int -> tvBody?.setText(title)
            is String -> tvBody?.text = title
        }
    }

    override fun setImage(image: Int) {
        val ids: HashMap<String, Int> = arguments?.getSerializable(Constants.DIALOG_KEY_IDS) as HashMap<String, Int>
        val idView: Int = ids[Constants.DIALOG_ID_IMAGE]!!
        val image: ImageView? = view?.findViewById(idView)
//        image?.setImageResource(idView)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        when (dismissBy) {
            PerformBy.USER -> {
                tag?.let {
                    listener?.onDialogCanceled(it)
                }
            }
            PerformBy.SYSTEM -> {
            }
        }
    }


}