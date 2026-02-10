package com.example.teammaravillaapp.page.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.yalantis.ucrop.UCrop
import java.io.File

/**
 * Crea y recuerda los launchers necesarios para:
 * - seleccionar una imagen desde el sistema (GetContent)
 * - recortarla con UCrop (StartActivityForResult)
 *
 * Objetivo:
 * - Mantener la pantalla contenedora [Profile] limpia y legible.
 * - Encapsular detalles Android/UCrop en un único lugar reutilizable.
 *
 * @param context Contexto usado para crear el destino de cache y el Intent de UCrop.
 * Restricciones: no nulo.
 * @param onCropped Callback invocado cuando UCrop finaliza correctamente.
 * Recibe el Uri resultante en formato String (ej. "content://...").
 * Restricciones: no nulo.
 * @param onCropError Callback invocado cuando UCrop falla o devuelve un resultado inválido.
 * Restricciones: no nulo.
 *
 * @return [ProfileImagePickers] con una función [ProfileImagePickers.pickImage] lista para usarse desde UI.
 *
 * @see UCrop Librería de recorte utilizada.
 *
 * Ejemplo de uso:
 * {@code
 * val pickers = rememberProfileImagePickers(
 *   context = context,
 *   onCropped = { uri -> vm.savePhoto(uri) },
 *   onCropError = { vm.onCropError() }
 * )
 *
 * // desde un botón:
 * pickers.pickImage()
 * }
 */
@Composable
internal fun rememberProfileImagePickers(
    context: Context,
    onCropped: (String) -> Unit,
    onCropError: () -> Unit
): ProfileImagePickers {

    fun createCropDestUri(): android.net.Uri =
        android.net.Uri.fromFile(
            File(context.cacheDir, "profile_${System.currentTimeMillis()}.jpg")
        )

    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data: Intent? = result.data

        when {
            result.resultCode == Activity.RESULT_OK && data != null -> {
                val output = UCrop.getOutput(data)
                if (output != null) onCropped(output.toString()) else onCropError()
            }

            result.resultCode == UCrop.RESULT_ERROR -> onCropError()
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { pickedUri ->
        if (pickedUri == null) return@rememberLauncherForActivityResult

        val destUri = createCropDestUri()

        val cropIntent = UCrop.of(pickedUri, destUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(700, 700)
            .getIntent(context)

        cropLauncher.launch(cropIntent)
    }

    return ProfileImagePickers(
        pickImage = { pickImageLauncher.launch("image/*") }
    )
}

/**
 * Contenedor simple para exponer acciones de pick/crop sin filtrar implementaciones.
 *
 * @property pickImage Lanza el selector del sistema para elegir una imagen y recortarla.
 */
internal data class ProfileImagePickers(
    val pickImage: () -> Unit
)