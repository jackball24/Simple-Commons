package com.simplemobiletools.commons.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.DialogProperties
import com.simplemobiletools.commons.R
import com.simplemobiletools.commons.compose.alert_dialog.AlertDialogState
import com.simplemobiletools.commons.compose.alert_dialog.rememberAlertDialogState
import com.simplemobiletools.commons.compose.extensions.MyDevices
import com.simplemobiletools.commons.compose.theme.AppThemeSurface
import com.simplemobiletools.commons.models.BlockedNumber

@Composable
fun AddOrEditBlockedNumberAlertDialog(
    blockedNumber: BlockedNumber?,
    alertDialogState: AlertDialogState,
    deleteBlockedNumber: (String) -> Unit,
    addBlockedNumber: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var textFieldValue by remember { mutableStateOf(blockedNumber?.number.orEmpty()) }

    AlertDialog(
        containerColor = dialogContainerColor,
        modifier = Modifier
            .dialogWidth
            .dialogBorder,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = alertDialogState::hide,
        confirmButton = {
            TextButton(onClick = {
                var newBlockedNumber = textFieldValue
                if (blockedNumber != null && newBlockedNumber != blockedNumber.number) {
                    deleteBlockedNumber(blockedNumber.number)
                }

                if (newBlockedNumber.isNotEmpty()) {
                    // in case the user also added a '.' in the pattern, remove it
                    if (newBlockedNumber.contains(".*")) {
                        newBlockedNumber = newBlockedNumber.replace(".*", "*")
                    }
                    addBlockedNumber(newBlockedNumber)
                }
                alertDialogState.hide()
            }) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                alertDialogState.hide()
            }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        shape = dialogShape,
        text = {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = textFieldValue,
                onValueChange = {
                    textFieldValue = it
                },
                supportingText = {
                    Text(
                        text = stringResource(id = R.string.add_blocked_number_helper_text),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                label = {
                    Text(text = stringResource(id = R.string.number))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
        },
        tonalElevation = dialogElevation
    )
    ShowKeyboardWhenDialogIsOpenedAndRequestFocus(focusRequester = focusRequester)
}

@Composable
@MyDevices
private fun AddOrEditBlockedNumberAlertDialogPreview() {
    AppThemeSurface {
        AddOrEditBlockedNumberAlertDialog(
            blockedNumber = null,
            deleteBlockedNumber = {},
            addBlockedNumber = {},
            alertDialogState = rememberAlertDialogState()
        )
    }
}
