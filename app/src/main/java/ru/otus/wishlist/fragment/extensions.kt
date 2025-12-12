package ru.otus.wishlist.fragment

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.otus.wishlist.R

fun BottomSheetDialogFragment.dismissWithToast(resId: Int) {
    dismiss()
    Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
}

fun Fragment.setFragmentResult(fragment: String, key: String, value: String) =
    setFragmentResult(
        fragment,
        Bundle().apply { putString(key, value) }
    )

fun FragmentActivity.showErrorAlert(): AlertDialog =
    MaterialAlertDialogBuilder(this, R.style.Alert)
        .setTitle(getString(R.string.error))
        .setMessage(getString(R.string.something_went_wrong))
        .setPositiveButton(getString(R.string.ok)) { _, _ -> }
        .show()

inline fun <reified T : Parcelable> Bundle.getUniversalParcelable(key: String): T? =
    when {
        SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("deprecation") getParcelable(key)
    }
